package samstnet.com.kaz.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import samstnet.com.kaz.MainActivity;


@RequiresApi(api = Build.VERSION_CODES.N)
public class updateWeather extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("updateWeather","onReceive");

        NetworkInfo mNetworkState= ((MainActivity)MainActivity.mContext).getNetworkInfo();
        if(mNetworkState!=null&&mNetworkState.isConnected()) {
            ((MainActivity) MainActivity.mContext).UsingGps();
            Log.e("AlarmBroadcastReceiverA", String.valueOf(AlarmBroadcastReceiver.count));
            AlarmBroadcastReceiver.count = 0;
        }else {
            AlarmBroadcastReceiver.count++;
            Log.e("AlarmBroadcastReceiverB", String.valueOf(AlarmBroadcastReceiver.count));
        }
    }
}
