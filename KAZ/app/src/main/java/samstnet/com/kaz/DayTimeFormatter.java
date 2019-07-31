package samstnet.com.kaz;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DayTimeFormatter {
    //기상청 api를 이용을 위해 만든 함수, 3시간 간격의 시간을 찾아 조정
    String nowTime_str;
    String baseTime;
    int fostBase;
    int nowTime;
    String today[];
    String yesterday[];
    DayTimeFormatter(){

        //오늘 날짜 계산
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String getTime = sdf.format(date);
        today = getTime.split("-");

        //어제날짜계산
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);  // 오늘 날짜에서 하루를 뺌.
        String getYesterday = sdf.format(calendar.getTime());
        yesterday = getYesterday.split("-");

        this.baseTime = today[0]+today[1]+today[2];
        this.nowTime_str = today[3];
        this.nowTime=Integer.parseInt(this.nowTime_str);
        Log.d("dafaagf",Integer.toString(nowTime));
        caclTime();
    }

    //기준날짜와 시간계산
    void caclTime(){
        int tagetTime[]={2,5,8,11,14,17,20,23};
        if(nowTime<tagetTime[1]){
            //오늘 베이스타임이 업데이트 되지않았다면 이전날 데이터를 이용
            baseTime=yesterday[0]+yesterday[1]+yesterday[2];
            if(nowTime>=tagetTime[0])
                nowTime=23;
            else
                nowTime=20;
        }else {
            for (int i = 2; i < tagetTime.length; i++) {
                if(nowTime<=tagetTime[i]){
                    nowTime=tagetTime[i-2];
                    break;
                }
            }
        }

        //기상청 제공시간계산
        int fostTime[] = {0,3,6,9,12,15,18,21};
        for(int i=1;i<fostTime.length;i++){
            if(nowTime<fostTime[i]){
                fostBase=fostTime[i-1];
                break;
            }
        }


    }

    String getNowTime(){
        if(nowTime<10){
            nowTime_str="0"+nowTime+"00";
        }else{
            nowTime_str=nowTime+"00";
        }
        return nowTime_str;
    }

    String getBaseTime(){
        return this.baseTime;
    }
}
