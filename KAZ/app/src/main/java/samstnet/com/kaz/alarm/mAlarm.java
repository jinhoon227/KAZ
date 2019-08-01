package samstnet.com.kaz.alarm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import samstnet.com.kaz.MainActivity;
import samstnet.com.kaz.eventbus.Customer;

public class mAlarm extends Service {

    AlarmBroadcastReceiver alarmBroadcastReceiver;
    TimePickerFragment timePickerFragment;
    static NotificationManager manager;
    //알람 서비스
    static Intent intent;
    Customer cus;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        alarmBroadcastReceiver=new AlarmBroadcastReceiver();
        timePickerFragment=new TimePickerFragment();
        manager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        intent = new Intent(getApplicationContext(),//현재제어권자
                TimePickerFragment.class); // 이동할 컴포넌트
        cus=new Customer();

        //오레오 이상에서만 동작, 오레오 이상에서 notificationChannel이 없으면 동작하지 않음
        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.O) {
            manager.createNotificationChannel(new NotificationChannel("default", "기본 채널",
                    NotificationManager.IMPORTANCE_DEFAULT));
        }

        Log.d("mAlarm","onCreate");

        //앱을 키자마자 알람 설정, 후에 알람 온오프에 따라서 설정을 바꿔줘야함
        startAlarm();
    }

    //알람 서비스 시작
    public void startAlarm(){
        startService(intent);
    }

    //알람 서비스 종료
    public void stopAlarm(){
        TimePickerFragment.time=0;
        AlarmBroadcastReceiver.timeCount=0;
        stopService(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopAlarm();
        if(!cus.setting1.isCreateevent()){
        }
    }
}