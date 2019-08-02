package samstnet.com.kaz.weekweather;

import android.content.Context;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class WeekWeatherParser {

    String city;
    ArrayList<WeekWeatherInfo> arr_wwif;
    WeekWeatherInfo wwif;
    URL url;
    String urlpath="http://www.kma.go.kr/weather/forecast/mid-term-xml.jsp?stnId=108";


    public WeekWeatherParser(String city){

        this.city = city;
        arr_wwif = new ArrayList();

        //url 제작.
        try {
            url = new URL(urlpath);
        } catch (MalformedURLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public WeekWeatherParser(double x, double y){

        FindShortestReigon fsr = new FindShortestReigon();
        Double[] target = new Double[2];
        target[0]=x;
        target[1]=y;
        this.city = fsr.ShortestDistance(target);
        Log.d("aklafklkla", this.city);
        arr_wwif = new ArrayList();

        //url 제작.
        try {
            url = new URL(urlpath);
        } catch (MalformedURLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

    }

    private boolean bsearchcity;
    private boolean bstop;
    private boolean bcity;
    private boolean btmEf;
    private boolean bwf;
    private boolean btmn;
    private boolean btmx;
    private boolean breliability;

    public void StartParsing(){
        //파서준비.
        try {
            XmlPullParserFactory xppf;
            xppf = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = xppf.newPullParser();
            xpp.setInput(url.openStream(), null);
            int et = xpp.getEventType();

            //파싱 스타트
            while (et != XmlPullParser.END_DOCUMENT && !bstop) {
                switch (et) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.END_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        if (xpp.getName().equals("city")) {
                            bcity = true;
                        }
                        if(xpp.getName().equals("tmEf") && bsearchcity){
                            //일자.
                            btmEf = true;
                        }
                        if(xpp.getName().equals("wf") && bsearchcity){
                            //날씨. 예) 흐림, 비, 눈 등등..
                            bwf = true;
                        }
                        if(xpp.getName().equals("tmn") && bsearchcity){
                            //최저기온.
                            btmn = true;
                        }
                        if(xpp.getName().equals("tmx") && bsearchcity){
                            //최고기온.
                            btmx = true;
                        }
                        if(xpp.getName().equals("reliability") && bsearchcity){
                            //신뢰도. 이 정보를 믿을수 있는가?!
                            breliability = true;
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        if(xpp.getName().equals("location")){
                            //다시 잠그기.
                            bcity = false;
                            bsearchcity = false;
                        }
                        if(xpp.getName().equals("wid")){
                            bstop = true;
                        }
                        break;
                    case XmlPullParser.TEXT:
                        //잠금이 풀리는순간 그 아래에 있는 녀석들을 싸그리 담아버립니다.
                        if(bsearchcity){

                            if(btmEf){

                                //자료를 담을 클래스를 만들어주고.
                                wwif = new WeekWeatherInfo();

                                wwif.mTmEf = xpp.getText().toString();
                                btmEf = false;
                            }

                            if(bwf){
                                wwif.mWf = xpp.getText().toString();
                                bwf = false;
                            }

                            if(btmn){
                                wwif.mTmn = xpp.getText().toString();
                                btmn = false;
                            }

                            if(btmx){
                                wwif.mTmx = xpp.getText().toString();
                                btmx = false;
                            }

                            if(breliability){
                                wwif.mReliability = xpp.getText().toString();
                                breliability = false;

                                //마지막 자료까지 다 담았다면 이제 자료를 넣어줍니다.
                                arr_wwif.add(wwif);
                            }
                        }
                        if(bcity){
                            //걸러낸 라인에서 텍스트를 가져다가 우리가 지정한 시티, 서울과 같은지 비교합니다.
                            if(xpp.getText().toString().equals(city)){
                                //드디어 원하던 라인에 도착했군요! 잠금을 풀어줍니다.
                                // 이 다음라인부터 열심히 자료를 수집합니다.
                                 bsearchcity = true;
                            }
                        }
                        break;

                    }
                    et = xpp.next();
            }
        }catch (XmlPullParserException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }

    }

    public ArrayList GetArr_wwif(){

        for(int i = 0 ; i < arr_wwif.size() ; i++){
            Log.d("june", "------ city : " + city + " 주간날씨 " + i + "번 ------- \n" +
                    "  일자 : " + arr_wwif.get(i).mTmEf +
                    "  날씨 : " + arr_wwif.get(i).mWf +
                    "  최저기온 : " + arr_wwif.get(i).mTmn +
                    "  최고기온 : " + arr_wwif.get(i).mTmx +
                    "  신뢰성 : " + arr_wwif.get(i).mReliability);
        }

        return arr_wwif;
    }

}
