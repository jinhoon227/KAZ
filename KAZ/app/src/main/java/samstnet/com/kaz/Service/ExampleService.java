package samstnet.com.kaz.Service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;

import samstnet.com.kaz.MainActivity;
import samstnet.com.kaz.R;
import samstnet.com.kaz.alarm.AlarmBroadcastReceiver;
import samstnet.com.kaz.alarm.AlarmDisturb;

import static samstnet.com.kaz.eventbus.Customer.CHANNEL_ID;


public class ExampleService extends Service {

    static AlarmManager mAlarmManager;
    static public PendingIntent[] operation;
    int _minute;
    static int minute;
    static PendingIntent pendingIntent;
    static public PendingIntent pendingIntent1;
    Intent intent;
    Intent intent1;
    static Calendar calendar;
    static public int time = 0;
    static public int operationNum = 24;

    String AlarmTitle=new String();
    String AlarmText=new String();

    //온도
    String temp;
    int tem=-1;

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d("test", "서비스의 onCreate");

        //알람이 실행될 때 실행되었으면 하는 액티비티
        intent = new Intent(this, AlarmBroadcastReceiver.class);
        intent1 = new Intent(this, AlarmDisturb.class);
        mAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        calendar = Calendar.getInstance();
        operation = new PendingIntent[operationNum];

        time = 0;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            onTimeSet();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onTimeSet() {
        // 사용자가 시간을 선택하였을 때, 실행됨, 유저가 설정한 시간과 분이 이곳에서 설정됨
        _minute=0;

        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("mm");
        String getTime = sdf.format(date);
        minute = Integer.valueOf(getTime);

        pendingIntent=PendingIntent.getBroadcast(this, 25, intent, 0);
        pendingIntent1=PendingIntent.getBroadcast(this,26,intent1,0);

        //sendBroadcast(intent);

        for (int i = 0; i < operationNum; i++) {

            calendar.set(Calendar.HOUR_OF_DAY,time);
            calendar.set(Calendar.MINUTE,_minute);

            Log.d(String.valueOf(time), String.valueOf(_minute));


            operation[i] = PendingIntent.getBroadcast(this, i, intent, 0);

            //알람 반복
            // 10분
            //mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000 * 60 *10 , operation[i]);
            // 24시간
            mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000 * 60 * 60 * 24 , operation[i]);

            //_minute++;
            time+=1;
        }

        calendar.set(Calendar.HOUR_OF_DAY,7);
        calendar.set(Calendar.MINUTE,30);

        //calendar.set(Calendar.HOUR_OF_DAY,1);
        //calendar.set(Calendar.MINUTE,minute+1);

        //아침알람
        mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000 * 60 * 60 * 24 , pendingIntent1);
    }

    //알람 서비스 시작
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void sendAlarm(){
        //calendar.set(calendar.MINUTE,minute);
       // mAlarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent );
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String input = intent.getStringExtra("inputExtra");


        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        setText();

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(AlarmTitle)
                .setContentText(AlarmText)
                .setSmallIcon(R.drawable.bean1)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        for (int i = 0; i < operationNum; i++) {
            mAlarmManager.cancel(operation[i]);
        }
        Log.d("test", "서비스의 onDestroy");
    }

    public static AlarmManager getAlarmManager(){
        return mAlarmManager;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setText(){
        temp = MainActivity.tempor.get(0);
        tem=Integer.parseInt(temp);

        if(MainActivity.tempor.size()!=0) {
            AlarmTitle = MainActivity.cityInfo+"의 날씨는"+temp+"도";
        }
        else {
            AlarmTitle="저는 집에 갈 수 없어요ㅜㅜ";
        }

        if(MainActivity.wtstate.size()==0){
            AlarmText="새로고침해라";
        }
        else if(MainActivity.wtstate.get(0)=="manycloud"){
            //AlarmTitle="흐림";
            if(tem>=30)
                AlarmText=heat();
            else if(tem<=0)
                AlarmText=cold();
            else
                AlarmText = "구름이 많아요! 흐린 날씨에 주의하세요";
        }
        else if(MainActivity.wtstate.get(0)=="fewcloud"){
            //AlarmTitle="구름";
            if(tem>=30)
                AlarmText=heat();
            else if(tem<=0)
                AlarmText=cold();
            else
                AlarmText="날씨가 좋아요. 산책하러갈까요?";
        }
        else if(MainActivity.wtstate.get(0)=="sun"){
            //AlarmTitle="태양";
            if(tem>=30)
                AlarmText=heat();
            else if(tem<=0)
                AlarmText=cold();
            else
                AlarmText="화창한 날씨에요. 함께 나가요";
        }
        else if(MainActivity.wtstate.get(0)=="rain"){
            //AlarmTitle="비";
            if(tem>=30)
                AlarmText="지금은 덥고 습한 날씨에요!. 잊지말고 우산 꼭 챙기세요";
            else if(tem<=0)
                AlarmText="비예보도 있는데 춥기까지하네. 우산 챙기세요";
            else
                AlarmText="비 예보가 있어요. 잊지말고 우산 챙기세요!";
        }
        else if(MainActivity.wtstate.get(0)=="snow"){
            //AlarmTitle="눈";
            if(tem>=30)
                AlarmText="날씨가 미쳤어";
            else if(tem<=0)
                AlarmText="영하 온도에 눈까지 와요. 야외 활동을 자제하세요";
            else
                AlarmText="눈 예보가 있어요. 따뜻하게 입고 나가요!";
        }
        else {
            AlarmText="아 속안좋아";
        }

    }

    public String heat(){
        return "날씨가 많이 더워요. 물 많이 마시고 야외활동을 자세하세요!";
    }

    public String cold(){
        return "으슬으슬 너무 추워요. 따뜻하게 입고나가요~";
    }


}