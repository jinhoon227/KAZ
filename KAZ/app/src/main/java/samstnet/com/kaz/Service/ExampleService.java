package samstnet.com.kaz.Service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.media.AudioManager;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;

import samstnet.com.kaz.Lovestate;
import samstnet.com.kaz.MainActivity;
import samstnet.com.kaz.R;
import samstnet.com.kaz.alarm.AlarmBroadcastReceiver;
import samstnet.com.kaz.alarm.AlarmDisturb;
import samstnet.com.kaz.alarm.updateWeather;
import samstnet.com.kaz.eventbus.Customer;

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
    MainActivity activity=new MainActivity();
    String AlarmTitle=new String();
    String AlarmText=new String();
    Customer cus;
    long now1;
    Date date;
    SimpleDateFormat sdf1;
    String getTime;
    //온도
    String temp;
    int tem=-1;

    //시간
    int _hour;
    int hour;

    int nImg=0;

    AudioManager mAudioManager;

    Intent intent2;
    Intent intent3;
    PendingIntent operation1;
    PendingIntent operation2;

    static public int startTime,_startTime;
    static public int endTime,_endTime;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate() {
        super.onCreate();

        startTime = 22;
        endTime = 7;

        Log.d("test", "서비스의 onCreate");

        //알람이 실행될 때 실행되었으면 하는 액티비티
        intent = new Intent(this, AlarmBroadcastReceiver.class);
        intent1 = new Intent(this, AlarmDisturb.class);
        intent2=new Intent(this, Lovestate.class);
        intent3=new Intent(this, updateWeather.class);

        mAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        operation = new PendingIntent[operationNum];

        now1 = System.currentTimeMillis();
        date = new Date(now1);
        sdf1 = new SimpleDateFormat("HH");//1시간단위
        getTime = sdf1.format(date);
        int timing1=cus.getStateTime();//로컬시간 가져오기
        operation1=PendingIntent.getBroadcast(this,30, intent2,0);
        operation2=PendingIntent.getBroadcast(this,31, intent3,0);

        _startTime=startTime;
        if(endTime<startTime) {
            _endTime = endTime+24;
        }
        else{
            _endTime=endTime;
        }


        time = 0;
        Log.e("알림버튼누른시간",getTime);
        activity.lovestatetime();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            onTimeSet();
            onTimeSet1();
        }

    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onTimeSet1() {
        // 사용자가 시간을 선택하였을 때, 실행됨, 유저가 설정한 시간과 분이 이곳에서 설정됨

        calendar = Calendar.getInstance();

        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("mm");
        SimpleDateFormat hdf = new SimpleDateFormat("HH");
        String getTime = sdf.format(date);
        _minute=Integer.valueOf(getTime);
        getTime = hdf.format(date);
        int _hour=Integer.valueOf(getTime);

        _hour=(_hour/3)*3+3;

        if(_hour>=24){
            _hour=0;
            Log.d("Alarm","Lpvestate1");
            calendar.add(Calendar.DAY_OF_YEAR,1);
            calendar.set(Calendar.HOUR_OF_DAY,_hour);
            calendar.set(Calendar.MINUTE,0);
        }
        else{
            Log.d("Alarm","Lpvestate2");
            calendar.set(Calendar.HOUR_OF_DAY,_hour);
            calendar.set(Calendar.MINUTE,0);
        }


        Log.e("ExampleService 3",String.valueOf( calendar.get(Calendar.DAY_OF_YEAR)));
        Log.d(String.valueOf(_hour), String.valueOf(minute));

        mAlarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),1000*60*60*3,operation1);
        mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),1000*60*60*3,operation2);
       // mAlarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),1000*60*60*3,operation2);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onTimeSet() {
        // 사용자가 시간을 선택하였을 때, 실행됨, 유저가 설정한 시간과 분이 이곳에서 설정됨

        calendar = Calendar.getInstance();
        _minute=10;

        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("mm");
        String getTime = sdf.format(date);
        minute = Integer.valueOf(getTime);

        pendingIntent=PendingIntent.getBroadcast(this, 25, intent, 0);
        pendingIntent1=PendingIntent.getBroadcast(this,26,intent1,0);

        sdf=new SimpleDateFormat("HH");
        hour=Integer.valueOf(sdf.format(date));
        _hour=Integer.valueOf(sdf.format(date));

        hour=((hour/3)*3)+3;

        if(hour>=24){
            hour=0;
            Log.d("Alarm","Alarm1");
            calendar.add(Calendar.DAY_OF_YEAR,1);
            calendar.set(Calendar.HOUR_OF_DAY,hour);
            calendar.set(Calendar.MINUTE,_minute);
        }
        else{
            Log.d("Alarm","Alarm2");
            calendar.set(Calendar.HOUR_OF_DAY,hour);
            calendar.set(Calendar.MINUTE,_minute);
        }

        Log.e("ExampleService 1",String.valueOf( calendar.get(Calendar.DAY_OF_YEAR)));
        Log.d(String.valueOf(hour), String.valueOf(minute));

        mAlarmManager.setInexactRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), 1000 * 60 * 60 * 3 , pendingIntent);
        //mAlarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), 1000 * 60 * 2 , pendingIntent);

        //현재 시간 리셋
        calendar = Calendar.getInstance();
        if(_hour>endTime){
            calendar.add(Calendar.DAY_OF_YEAR,1);
            Log.d("AlarmDisturb 1","NonDisturb");
        }
        else{
            Log.d("AlarmDisturb 2","NonDisturb");
        }

        calendar.set(Calendar.HOUR_OF_DAY, endTime);
        calendar.set(Calendar.MINUTE,30);

        //calendar.set(Calendar.HOUR_OF_DAY,1);
        //calendar.set(Calendar.MINUTE,minute+1);

        Log.e("ExampleService 2",String.valueOf( calendar.get(Calendar.DAY_OF_YEAR)));
        Log.d(String.valueOf(endTime), String.valueOf(_hour));

        //아침알람
        mAlarmManager.setInexactRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), 1000 * 60 * 60 * 24 , pendingIntent1);

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
                .setSmallIcon(nImg)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
         mAlarmManager.cancel(pendingIntent);
         mAlarmManager.cancel(pendingIntent1);
        Log.d("test", "서비스의 onDestroy");
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
            AlarmTitle = "현재 "+MainActivity.cityInfo+"의 날씨는"+temp+"도 입니다.";
        }
        else {
            AlarmTitle="에러가 났어요";
        }

        if(MainActivity.wtstate.size()==0){
            AlarmText="앱을 다시 켜주세요";
        }
        else if(MainActivity.wtstate.get(0)=="manycloud"){
            //AlarmTitle="흐림";
            nImg= R.drawable.sfewcloudy;
            if(tem>=30&&(_hour<=20&&_hour>=6))
                AlarmText = heat();
            else if(tem>=25&&(_hour>20||_hour<6)) {
                AlarmText = nightHeat();
                nImg= R.drawable.smoon;
            }
            else if(tem<=0)
                AlarmText=cold();
            else if(_hour>20||_hour<6) {
                AlarmText = "좋은 밤이에요. 오늘 하루는 어땠나요?";
                nImg= R.drawable.smoon;
            }
            else
                AlarmText = "구름이 많아요! 날씨가 흐려도 기운내세요";

        }
        else if(MainActivity.wtstate.get(0)=="fewcloud"){
            //AlarmTitle="구름";
            nImg= R.drawable.sfewcloudy;
            if(tem>=30&&(_hour<=20&&_hour>=6))
                AlarmText=heat();
            else if(tem>=25&&(_hour>20||_hour<6)) {
                AlarmText = nightHeat();
                nImg= R.drawable.smoon;
            }
            else if(tem<=0)
                AlarmText=cold();
            else if(_hour>20||_hour<6) {
                AlarmText = "좋은 밤이에요. 오늘 밤은 좋은 꿈 꾸세요";
                nImg= R.drawable.smoon;
            }
            else
                AlarmText="날씨가 좋아요. 산책하러갈까요?";
        }
        else if(MainActivity.wtstate.get(0)=="sun"){
            //AlarmTitle="태양";
            nImg= R.drawable.ssun;
            if(tem>=30&&(_hour<=20&&_hour>=6))
                AlarmText=heat();
            else if(tem>=25&&(_hour>20||_hour<6)) {
                AlarmText = nightHeat();
                nImg= R.drawable.smoon;
            }
            else if(tem<=0)
                AlarmText=cold();
            else if(_hour>20||_hour<6) {
                AlarmText = "좋은 밤이에요. 오늘은 달이 잘 보이겠어요";
                nImg= R.drawable.smoon;
            }
            else
                AlarmText="화창한 날씨에요. 함께 나가요";
        }
        else if(MainActivity.wtstate.get(0)=="rain"){
            //AlarmTitle="비";
            nImg= R.drawable.srain;
            if(tem>=30)
                AlarmText="지금은 덥고 습한 날씨에요!. 잊지말고 우산 꼭 챙기세요";
            else if(tem<=0)
                AlarmText="비예보도 있는데 춥기까지! 꼭 우산 챙기세요";
            else
                AlarmText="비 예보가 있어요. 잊지말고 우산 챙기세요!";
        }
        else if(MainActivity.wtstate.get(0)=="snow"){
            //AlarmTitle="눈";
            nImg= R.drawable.ssnow;
            if(tem>=30)
                AlarmText="날씨가 미쳤어!";
            else if(tem<=0)
                AlarmText="영하 온도에 눈까지 와요. 야외 활동을 자제하세요";
            else
                AlarmText="눈 예보가 있어요. 따뜻하게 입고 나가요!";
        }
        else {
            AlarmText="알람을 껐다 켜주세요";
        }

    }

    public String heat(){
        return "날씨가 많이 더워요. 물 많이 마시고 야외활동을 자제하세요!";
    }

    public String cold(){
        return "으슬으슬 너무 추워요. 따뜻하게 입고나가요";
    }

    public String nightHeat(){
        return "더운 밤이에요. 잠자리에 들 때 주의하세요";
    }


}