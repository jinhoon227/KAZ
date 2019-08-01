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

import static samstnet.com.kaz.eventbus.Customer.CHANNEL_ID;


public class ExampleService extends Service {

    AlarmManager mAlarmManager;
    PendingIntent[] operation;
    Intent intent;
    Calendar calendar;
    static int time = 0;
    int operationNum = 8;

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d("test", "서비스의 onCreate");

        //알람이 실행될 때 실행되었으면 하는 액티비티
        intent = new Intent(this, AlarmBroadcastReceiver.class);
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
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("mm");
        String getTime = sdf.format(date);
        int _minute = Integer.valueOf(getTime);

        for (int i = 0; i < operationNum; i++) {
            Log.d("Time", String.valueOf(_minute + time));
            calendar.set(Calendar.MINUTE, _minute + time);

            operation[i] = PendingIntent.getBroadcast(this, i, intent, 0);
            //mAlarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), operation[i]);
            mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000 * 60 * 60 * 3 , operation[i]);

            time++;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String input = intent.getStringExtra("inputExtra");


        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("어플을 켜주셔서 감사합니다.")
                .setContentText(input)
                .setSmallIcon(R.drawable.bean1)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);

        //do heavy work on a background thread
        //stopSelf();

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

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}