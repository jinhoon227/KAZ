package samstnet.com.kaz.eventbus;
//이거슨 아이탬 정보
public class Item_type {

    String name;
    String mobile;
    int resId;
    boolean wear;

    public Item_type(String name, String mobile, int resId,boolean wear) {
        this.name = name;
        this.mobile = mobile;
        this.resId = resId;
        this.wear = wear;
    }

    public boolean isWear() {
        return wear;
    }

    public void setWear(boolean wear) {
        this.wear = wear;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    @Override
    public String toString() {
        return "Item_type{" +
                "name='" + name + '\'' +
                ", mobile='" + mobile + '\'' +
                '}';
    }
}