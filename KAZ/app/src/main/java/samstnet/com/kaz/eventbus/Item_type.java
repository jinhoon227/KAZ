package samstnet.com.kaz.eventbus;
//이거슨 아이탬 정보
public class Item_type {

    String name;    //이름
    String mobile;  //내용
    int resId;      //그림
    boolean wear;   //장착여부
    int price;      //가격
    boolean buy;    //구매여부


    public Item_type(String name, String mobile, int resId,int price, boolean wear,boolean buy) {
        this.name = name;
        this.mobile = mobile;
        this.resId = resId;
        this.wear = wear;
        this.price= price;
        this.buy = buy;

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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public boolean isBuy() {
        return buy;
    }

    public void setBuy(boolean buy) {
        this.buy = buy;
    }

    @Override
    public String toString() {
        return "Item_type{" +
                "name='" + name + '\'' +
                ", mobile='" + mobile + '\'' +
                '}';
    }
}
