package samstnet.com.kaz.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;

import samstnet.com.kaz.Service.ExampleService;

public class NonDisturb extends Service {

    static public int startTime,_startTime;
    static public int endTime,_endTime;


    Calendar calendar;
    Intent intent;
    Intent _intent;
    AlarmManager mAlarmManager;
    PendingIntent operation;
    int hour;
    int _minute;
    int hourDelay=1;
    int temp;
    static boolean[] isAlarm= new boolean[ExampleService.operationNum];


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate() {
        super.onCreate();

        calendar = Calendar.getInstance();

        startTime=22;
        endTime=9;

        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("HH");
        SimpleDateFormat mi=new SimpleDateFormat("mm");
        String getTime = sdf.format(date);
        String getmi=mi.format(date);
        hour = Integer.valueOf(getTime);
        _minute=Integer.valueOf(getTime);

        intent = new Intent(getApplicationContext(),//현재제어권자
                AlarmDisturb.class); // 이동할 컴포넌트
        _intent=new Intent(getApplicationContext(),//현재제어권자
                AlarmBroadcastReceiver.class); // 이동할 컴포넌트
        mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        operation= PendingIntent.getBroadcast(this, 0, intent, 0);

        _startTime=startTime;
        if(endTime<startTime) {
            _endTime = endTime+24;
        }

//        //지금 1시간단위로 알람 설정중
//        for(int i=0;i<ExampleService.operationNum;i++){
//            isAlarm[i]=true;
//        }

        for(int i = startTime ;i < _endTime;i+=hourDelay){
            temp = (i / hourDelay);
            if(temp>=24){
                temp-=24;
            }
            Log.d("setTime",String.valueOf(temp));
        }

        Log.d("NonDisturb_minute", String.valueOf(hour) );
        Log.d("NonDisturb_startTime", String.valueOf(startTime) );


        //만약 지금 설정한 시간이라면 방해금지 키기

        //시작시간은 22시인에 끝나는건 9시 같은 경우
        if(endTime < startTime){
            Log.d(String.valueOf(startTime),String.valueOf(hour));
            // 만약 설정 시간이 2시
            if(hour<startTime){
                hour+=24;
            }
        }
        Log.d(String.valueOf(_endTime),String.valueOf(hour));
        if( hour>=startTime && hour<=_endTime ){
            Log.d("NonDisturb", "onNonDisturb" );
            //startService(intent);
            sendBroadcast(intent);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        for (int i = startTime; i < _endTime; i+=hourDelay) {
            temp = (i / hourDelay);
            if(temp>=24){
                temp-=24;
            }
            Log.d("temp",String.valueOf(temp));
            Log.d("temp",String.valueOf(_minute));
            calendar.set(Calendar.HOUR_OF_DAY,temp);
            calendar.set(Calendar.MINUTE,ExampleService._minute);
            //calendar.set(Calendar.MINUTE,38);
            //ExampleService.getAlarmManager().setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000 * 60 *60 *24 , ExampleService.operation[temp]);
            isAlarm[temp]=true;
        }
       sendBroadcast(_intent);

        Log.d("NonDisturb", "서비스의 onDestroy");
    }
}

