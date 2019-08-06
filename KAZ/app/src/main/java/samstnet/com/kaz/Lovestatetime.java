package samstnet.com.kaz;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;

public class Lovestatetime extends Service {
    AlarmManager mAlarmManager;
    //PendingIntent[] operation;
    PendingIntent operation;
    Intent intent;
    Calendar calendar;
    static int time;
    static int _minute;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("test", "애정도의Lovestatetime");
        //알람이 실행될 때 실행되었으면 하는 액티비티
       intent=new Intent(this, Lovestate.class);
        mAlarmManager=(AlarmManager)getSystemService(Context.ALARM_SERVICE);
        calendar=Calendar.getInstance();
        operation=PendingIntent.getBroadcast(this,30, intent,0);
        time=0;

        onTimeSet();
        Log.d("되냐?","안되냐");
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onTimeSet() {
        // 사용자가 시간을 선택하였을 때, 실행됨, 유저가 설정한 시간과 분이 이곳에서 설정됨
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("mm");
        String getTime = sdf.format(date);
        _minute=Integer.valueOf(getTime);

        Log.d("LovestateTime", String.valueOf(_minute+1));
        calendar.set(Calendar.MINUTE,1);
        mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),1000*60*60*1,operation);

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("test", "서비스의 onDestroy");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}