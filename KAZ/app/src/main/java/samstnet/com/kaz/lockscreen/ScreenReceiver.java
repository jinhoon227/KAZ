package samstnet.com.kaz.lockscreen;


import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import samstnet.com.kaz.Service.ExampleService;

public class ScreenReceiver extends BroadcastReceiver {
    private KeyguardManager km = null;

    private KeyguardManager.KeyguardLock keyLock = null;




    @Override

        public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)||intent.getAction().equals(intent.ACTION_BOOT_COMPLETED)) {
            if (km == null)

                km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);


            if (keyLock == null)

                keyLock = km.newKeyguardLock(Context.KEYGUARD_SERVICE);



            disableKeyguard();


            Intent i = new Intent(context, LockScreenActivity.class);

            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(i);

            Intent j = new Intent(context , ExampleService.class);

        }

    }



    public void reenableKeyguard() {

        keyLock.reenableKeyguard();

    }



    public void disableKeyguard() {

        keyLock.disableKeyguard();

    }



}
