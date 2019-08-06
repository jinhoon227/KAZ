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
import java.util.Timer;
import java.util.TimerTask;

import samstnet.com.kaz.Service.ExampleService;
import samstnet.com.kaz.eventbus.Customer;

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
    Customer customer=new Customer();

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
        endTime=7;

        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("HH");
        SimpleDateFormat mi=new SimpleDateFormat("mm");
        String getTime = sdf.format(date);
        String getmi=mi.format(date);
        hour = Integer.valueOf(getTime);
        _minute=Integer.valueOf(getmi);

        intent = new Intent(getApplicationContext(),//현재제어권자
                AlarmDisturb.class); // 이동할 컴포넌트
        mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        operation= PendingIntent.getBroadcast(this, 50, intent, 0);

        _startTime=startTime;
        if(endTime<startTime) {
            _endTime = endTime+24;
         }
        else{
            _endTime=endTime;
        }

        for(int i = startTime ;i < _endTime;i+=hourDelay){
            temp = (i / hourDelay);
            if(temp>=24){
                temp-=24;
            }
        }
        Log.d("NonDisturb_minute", "onCreate" );

        //기상 알람
        calendar.set(Calendar.HOUR_OF_DAY,hour);
        calendar.set(Calendar.MINUTE,_minute+5);

        //calendar.set(Calendar.HOUR_OF_DAY,endTime);
        //calendar.set(Calendar.MINUTE,30);

        Log.d("__NonDisturb_hour", String.valueOf(hour));
        Log.d("__NonDisturb_hour",String.valueOf(_minute));

        //mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000 * 60 * 60 * 24 , operation);
        //mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000 * 60 , operation);
        //mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000 * 60 , ExampleService.pendingIntent1);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onDestroy() {
        super.onDestroy();
        //mAlarmManager.cancel(operation);
        if(!customer.setting1.isSoundevent())
            registerRestartAlarm();
        else
            mAlarmManager.cancel(ExampleService.pendingIntent1);

        Log.d("NonDisturb", "서비스의 onDestroy");
    }

    /**
     * 알람 매니져에 서비스 등록
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void registerRestartAlarm(){

        Log.i("000 PersistentService" , "registerRestartAlarm" );

        intent = new Intent(getApplicationContext(),//현재제어권자
                AlarmDisturb.class); // 이동할 컴포넌트

        intent.setAction("ACTION.RESTART.PersistentService");
        PendingIntent sender = PendingIntent.getBroadcast(this,0,intent,0);

        calendar = Calendar.getInstance();
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("HH");
        SimpleDateFormat mi=new SimpleDateFormat("mm");
        String getTime = sdf.format(date);
        String getmi=mi.format(date);
        hour = Integer.valueOf(getTime);
        _minute=Integer.valueOf(getmi);

        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        calendar.set(Calendar.MINUTE,_minute+1);

        /**
         * 알람 등록
         */
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),1000 * 60,ExampleService.pendingIntent1);

    }

    Timer timer;

    public void Start_Period() {
        timer = new Timer();
        //timer.schedule(adTast , 5000);  // 5초후 실행하고 종료
        //timer.schedule(adTast, 0, 300000); // 0초후 첫실행, 3초마다 계속실행
        timer.schedule(addTask, 0, (60 * 1000)); //// 0초후 첫실행, Interval분마다 계속실행
    }

    public void Stop_Period() {
        //Timer 작업 종료
        if(timer != null) timer.cancel();
    }

    TimerTask addTask = new TimerTask() {
        @Override
        public void run() {
            //주기적으로 실행할 작업 추가
            Log.d("NonDisturb","start");
            sendBroadcast(intent);
        }
    };

}

