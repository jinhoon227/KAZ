package samstnet.com.kaz;

public class WeatherItem {
    String time;
    String tempor;
    String wtstate;
    int resId;
    public WeatherItem(String time, String tempor,String wtstate, int resId) {
        this.time = time;
        this.tempor = tempor;
        this.wtstate=wtstate;
        this.resId=resId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTempor() {
        return tempor;
    }

    public void setTempor(String tempor) {
        this.tempor = tempor;
    }

    public String getWtstate() {
        return wtstate;
    }

    public void setWtstate(String wtstate) {
        this.wtstate = wtstate;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    @Override
    public String toString() {
        return "WeatherItem{" +
                "time='" + time + '\'' +
                ", tempor='" + tempor + '\'' +
                ", wtstate='" + wtstate + '\'' +
                '}';
    }
}
