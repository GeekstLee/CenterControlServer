package xyz.smartrec.center;

public class Product {
    public int Id;
    public String MediaType;
    public String MediaUniqueID;
    public String SpuName;
    public double Price;
    public int SellCount;
    public String ImgURL;
    public int FirstCategoryId;
    public int SecondCategoryId;
    public String MediaURL;

    public Product(int id, String mediaType, String mediaUniqueID, String spuName, double price, int sellCount,
                   String imgURL, int firstCategoryId, int secondCategoryId, String mediaURL) {
        Id = id;
        MediaType = mediaType;
        MediaUniqueID = mediaUniqueID;
        SpuName = spuName;
        Price = price;
        SellCount = sellCount;
        ImgURL = imgURL;
        FirstCategoryId = firstCategoryId;
        SecondCategoryId = secondCategoryId;
        MediaURL = mediaURL;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getMediaType() {
        return MediaType;
    }

    public void setMediaType(String mediaType) {
        MediaType = mediaType;
    }

    public String getMediaUniqueID() {
        return MediaUniqueID;
    }

    public void setMediaUniqueID(String mediaUniqueID) {
        MediaUniqueID = mediaUniqueID;
    }

    public String getSpuName() {
        return SpuName;
    }

    public void setSpuName(String spuName) {
        SpuName = spuName;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }

    public int getSellCount() {
        return SellCount;
    }

    public void setSellCount(int sellCount) {
        SellCount = sellCount;
    }

    public String getImgURL() {
        return ImgURL;
    }

    public void setImgURL(String imgURL) {
        ImgURL = imgURL;
    }

    public int getFirstCategoryId() {
        return FirstCategoryId;
    }

    public void setFirstCategoryId(int firstCategoryId) {
        FirstCategoryId = firstCategoryId;
    }

    public int getSecondCategoryId() {
        return SecondCategoryId;
    }

    public void setSecondCategoryId(int secondCategoryId) {
        SecondCategoryId = secondCategoryId;
    }

    public String getMediaURL() {
        return MediaURL;
    }

    public void setMediaURL(String mediaURL) {
        MediaURL = mediaURL;
    }
}
