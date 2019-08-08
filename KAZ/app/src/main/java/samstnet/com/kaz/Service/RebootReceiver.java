package samstnet.com.kaz.Service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import samstnet.com.kaz.lockscreen.LockScreenActivity;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static samstnet.com.kaz.eventbus.Customer.CHANNEL_ID;

public class RebootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            Toast.makeText(context, "Booting",Toast.LENGTH_LONG).show();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel serviceChannel = new NotificationChannel(
                        CHANNEL_ID,
                        "Example Service Channel",
                        NotificationManager.IMPORTANCE_DEFAULT
                );

                NotificationManager manager = context.getSystemService(NotificationManager.class);
                manager.createNotificationChannel(serviceChannel);
            }

                Intent rebootIntent = new Intent(context, ExampleService.class);
                Intent i = new Intent(context, LockScreenActivity.class);

                i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

                context.startActivity(i.addFlags(FLAG_ACTIVITY_NEW_TASK));

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(rebootIntent);
                } else {
                    context.startService(rebootIntent);
                }

            }
    }

}
