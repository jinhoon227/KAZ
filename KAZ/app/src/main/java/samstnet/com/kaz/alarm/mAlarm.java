package samstnet.com.kaz.alarm;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import samstnet.com.kaz.Service.ExampleService;
import samstnet.com.kaz.eventbus.Customer;

public class mAlarm extends Service {

    static AlarmBroadcastReceiver alarmBroadcastReceiver;
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
        intent = new Intent(getApplicationContext(),//현재제어권자
                ExampleService.class); // 이동할 컴포넌트
        //cus=new Customer();

        Log.d("mAlarm","onCreate");

        startAlarm();

    }

    //알람 서비스 시작
    public void startAlarm(){
        startService(intent);
    }

    //알람 서비스 종료
    public void stopAlarm(){
        stopService(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopAlarm();
    }

}
