package xyz.smartrec.center.mongodb_client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.csvreader.CsvReader;
import com.mongodb.*;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * MongoDB client
 */
public enum Client {
    //定义一个枚举元素
    instance;

    public static final String IP = "";
    public static final int PORT = 27017;
    public static final String UserName = "";
    public static final String PassWord = "";
    public static final String DBName = "";
    public static final String ProductsCollectionName = "";
    public static final String FirstCategoryCollectionName = "";
    public static final String SecondCategoryCollectionName = "";
    public static final String SpiderJsonPath = "";
    public static final String JDSuccessSpiderDataPath = "";

    private static MongoClient mongoClient;

    static {
        try {
            mongoClient = new MongoClient(IP, PORT);

            DB db = mongoClient.getDB(DBName);
            boolean isAuth = db.authenticate(UserName, PassWord.toCharArray());
            if (!isAuth) {
                System.out.println("认证失败");
                throw new Exception("MongoDB权限认证失败");
            }
            System.out.println("认证通过");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 商品入库
     * Collections name：products
     */
    public static void uploadProducts() {
        long start = System.currentTimeMillis();
        DB db = mongoClient.getDB(DBName);
        DBCollection table = db.getCollection(ProductsCollectionName);
        File file = new File(JDSuccessSpiderDataPath);
        File[] files = file.listFiles();
        assert files != null;
        int index = 1;
        int invalid = 0;
        int count = 0;
        List<DBObject> productList = new ArrayList<>(10000);
        for (File f : files) {
            String filePath = f.getAbsolutePath();
            try {
                // 创建CSV读对象
                CsvReader csvReader = new CsvReader(filePath, ',', StandardCharsets.UTF_8);
                while (csvReader.readRecord()) {
                    String Id = csvReader.get(0);
                    String mediaType = csvReader.get(1);
                    String mediaUniqueID = csvReader.get(2);
                    String spuName = csvReader.get(3);
                    String price = csvReader.get(4);
                    String sellCount = csvReader.get(5);
                    String img = csvReader.get(6);
                    String firstCatId = csvReader.get(7);
                    String secondCatId = csvReader.get(8);
                    String mediaURL = csvReader.get(10);
                    if (price.equals("待发布") || price.equals("暂无报价")) {
                        invalid++;
                        continue;
                    }
                    try {
                        DBObject object = new BasicDBObject();
                        object.put("id", Integer.parseInt(Id));
                        object.put("media_type", mediaType);
                        object.put("media_unique_id", mediaUniqueID);
                        object.put("spu_name", spuName);
                        object.put("price", Double.parseDouble(price));
                        object.put("sell_count", Integer.parseInt(sellCount));
                        object.put("img_url", img);
                        object.put("first_category_id", Integer.parseInt(firstCatId));
                        object.put("second_category_id", Integer.parseInt(secondCatId));
                        object.put("media_url", mediaURL);
                        productList.add(object);
                    } catch (Exception e) {
                        System.out.println(Id + "==" + mediaType + "==" + mediaUniqueID + "===" +
                                spuName + "===" + price + "==" + sellCount + "===" + img
                                + "===" + firstCatId + "===" + secondCatId + "===" + mediaURL);
                        e.printStackTrace();
                        return;
                    }
                    if (productList.size() >= 10000) {
                        System.out.println("add count:" + count);
                        count++;
                        table.insert(productList);
                        productList.clear();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.printf("【%d/%d】\n", index, files.length);
            index++;

        }
        System.out.println("非法字段数量为" + invalid);
        System.out.println("一共用时：" + (System.currentTimeMillis() - start) + "毫秒");
    }

    /**
     * 类目信息入库
     */
    public static void uploadCategory() {
        long start = System.currentTimeMillis();
        DB db = mongoClient.getDB(DBName);
        DBCollection firstCatTable = db.getCollection(FirstCategoryCollectionName);
        DBCollection secondCatTable = db.getCollection(SecondCategoryCollectionName);
        File file = new File(SpiderJsonPath);
        File[] files = file.listFiles();
        assert files != null;
        //禁用的一级类目ID
        int[] invalidIds = new int[]{38, 58, 70, 71, 72, 78};
        Set<Integer> idSet = new HashSet<>(6);
        for (int id : invalidIds) {
            idSet.add(id);
        }
        int firstCatIndex = 1;
        int secondCatIndex = 1;
        int index = 1;
        for (File f : files) {
            String filePath = f.getAbsolutePath();
            InputStream in;
            try {
                in = new BufferedInputStream(new FileInputStream(filePath));
                String jsonStr = IOUtils.toString(in, StandardCharsets.UTF_8);
                JSONObject jsonObject = JSON.parseObject(jsonStr);
                Integer first_category_id = (Integer) jsonObject.get("first_category_id");
                String first_category_name = (String) jsonObject.get("first_category_name");
                DBObject firstObject = new BasicDBObject();
                firstObject.put("id", firstCatIndex);
                firstObject.put("first_category_id", first_category_id);
                firstObject.put("first_category_name", first_category_name);
                if (idSet.contains(first_category_id)) {
                    System.out.println("invalidID==>" + first_category_id);
                    //0代表禁用
                    firstObject.put("is_use", 0);
                } else {
                    //1代表可以使用
                    firstObject.put("is_use", 1);
                }
                firstCatTable.insert(firstObject);

                //解析SecondCat数组
                List<SecondCategory> secondCategories = JSON.parseArray(
                        JSON.parseObject(jsonStr).getString("second_category"), SecondCategory.class);

                List<DBObject> secondCats = new ArrayList<>();
                for (SecondCategory secondCategory : secondCategories) {
                    DBObject object = new BasicDBObject();
                    object.put("id", secondCatIndex);
                    object.put("second_category_id", secondCategory.getSecondCategoryId());
                    object.put("second_category_name", secondCategory.getSecondCategoryName());
                    object.put("first_category_id", first_category_id);
                    secondCats.add(object);
                    secondCatIndex++;
                }
                secondCatTable.insert(secondCats);
                firstCatIndex++;
                System.out.printf("【%d/%d】\n", index, files.length);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    public static void main(String[] args) {
        uploadCategory();
    }


}
