package samstnet.com.kaz.eventbus;

public class Customer {
    Item_type[] item;
    public Item_type[] getItem() {
        return item;
    }

    public void setItem(Item_type[] item) {
        this.item = item;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "item=" + item +
                '}';
    }

}