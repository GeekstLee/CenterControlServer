package xyz.smartrec.center.mongodb_client;

public class SecondCategory {
    public int SecondCategoryId;
    public String SecondCategoryName;

    public int getSecondCategoryId() {
        return SecondCategoryId;
    }

    public void setSecondCategoryId(int secondCategoryId) {
        SecondCategoryId = secondCategoryId;
    }

    public String getSecondCategoryName() {
        return SecondCategoryName;
    }

    public void setSecondCategoryName(String secondCategoryName) {
        SecondCategoryName = secondCategoryName;
    }

    @Override
    public String toString() {
        return "SecondCategory{" +
                "SecondCategoryId=" + SecondCategoryId +
                ", SecondCategoryName='" + SecondCategoryName + '\'' +
                '}';
    }
}
