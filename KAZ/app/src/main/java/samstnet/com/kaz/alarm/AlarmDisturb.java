package samstnet.com.kaz.alarm;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import samstnet.com.kaz.MainActivity;
import samstnet.com.kaz.R;
import samstnet.com.kaz.eventbus.BusProvider;
import samstnet.com.kaz.eventbus.Customer;

public class AlarmDisturb extends BroadcastReceiver {

    Uri ringtoneUri;
    NotificationCompat.Builder builder;
    Customer cus;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("AlarmBroadcastReceiver","onReceive");

        builder=new NotificationCompat.Builder(context,"default");
        cus=new Customer();

        builder.setSmallIcon(R.mipmap.ic_launcher);
        BusProvider.getInstance().register(this);

        builder.setContentTitle("방해금지모드가 켜져있습니다.");
        builder.setContentText("");

       Intent _intent=new Intent(context, MainActivity.class);
        PendingIntent pendingIntent= (PendingIntent) PendingIntent.getActivity(context,
                0,
                _intent,
                PendingIntent.FLAG_UPDATE_CURRENT); //알람이 이미 켜져있다면 내용을 업데이트해줌

        builder.setContentIntent(pendingIntent);    //notification을 누르면 pendingIntent안에 있는게 실행된다.

        mAlarm.manager.notify(1,builder.build());
    }
}
