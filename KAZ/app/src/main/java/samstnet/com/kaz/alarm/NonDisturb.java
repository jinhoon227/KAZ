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
        operation= PendingIntent.getBroadcast(this, 0, intent, 0);

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
        calendar.set(Calendar.HOUR_OF_DAY,endTime);
        calendar.set(Calendar.MINUTE,0);

        Log.d("__NonDisturb_hour", String.valueOf(endTime));

        mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000 * 60 * 60 * 24 , operation);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        customer.setting1.setSoundevent(true);
        mAlarmManager.cancel(operation);
        Log.d("NonDisturb", "서비스의 onDestroy");
    }
}

