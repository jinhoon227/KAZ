package samstnet.com.kaz.lockscreen;


import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import samstnet.com.kaz.Service.ExampleService;
import samstnet.com.kaz.eventbus.Customer;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class ScreenReceiver extends BroadcastReceiver {
    private KeyguardManager km = null;
    private KeyguardManager.KeyguardLock keyLock = null;
    Customer cus;
    @Override

        public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)||intent.getAction().equals(intent.ACTION_BOOT_COMPLETED)) {
            if (km == null)

                km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);


            if (keyLock == null)

                keyLock = km.newKeyguardLock(Context.KEYGUARD_SERVICE);

            disableKeyguard();

                Intent i = new Intent(context, LockScreenActivity.class);

                i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

            i.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

            context.startActivity(i);

            //Intent j = new Intent(context , ExampleService.class);

        }

    }

    public void reenableKeyguard() {

        keyLock.reenableKeyguard();

    }

    public void disableKeyguard() {

        keyLock.disableKeyguard();

    }

}
