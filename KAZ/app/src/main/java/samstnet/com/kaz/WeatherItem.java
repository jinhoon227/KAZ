package samstnet.com.kaz;

public class WeatherItem {
    String name;
    String mobile;
    int resId;
    public WeatherItem(String name, String mobile, int resId) {
        this.name = name;
        this.mobile = mobile;
        this.resId=resId;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
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

    @Override
    public String toString() {
        return "WeatherItem{" +
                "name='" + name + '\'' +
                ", mobile='" + mobile + '\'' +
                '}';
    }
}
