package samstnet.com.kaz;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.util.Date;

import samstnet.com.kaz.eventbus.Customer;

public class Lovestate extends BroadcastReceiver {
    Customer cus;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onReceive(Context context, Intent intent) {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("mm");
        String getTime = sdf.format(date);
        int _minute=Integer.valueOf(getTime);

        if (cus.plant1.getLove() > 0) {
            cus.plant1.setLove(cus.plant1.getLove() - 10);
        }
        Log.e( String.valueOf(_minute)+"분 love", String.valueOf(cus.plant1.getLove()));

        //아이템 사용 기록 리셋
        resetItem();
    }

    public void resetItem(){
        for(int i=0;i<cus.plant1.itemNum;i++){
            cus.plant1.items[i]=false;
        }
    }

}
