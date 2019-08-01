package samstnet.com.kaz.weekweather;

import android.util.Log;

public class WeekWeatherInfo {
    String mTmEf; //해당일.
    String mWf; //날씨.
    String mTmn; // 최저기온.
    String mTmx; //최고기온.
    String mReliability; //신뢰성.

    public String getmTmEf() {
        return mTmEf;
    }

    public void setmTmEf(String mTmEf) {
        this.mTmEf = mTmEf;
    }

    public String getmWf() {
        return mWf;
    }

    public void setmWf(String mWf) {
        this.mWf = mWf;
    }

    public String getmTmn() {
        return mTmn;
    }

    public void setmTmn(String mTmn) {
        this.mTmn = mTmn;
    }

    public String getmTmx() {
        return mTmx;
    }

    public void setmTmx(String mTmx) {
        this.mTmx = mTmx;
    }

    public String getmReliability() {
        return mReliability;
    }

    public void setmReliability(String mReliability) {
        this.mReliability = mReliability;
    }

    // 해당일 잘라서 날짜얻기
    public String getDay(){
        String day[] = this.mTmEf.split("-");
        return day[2];
    }
}
