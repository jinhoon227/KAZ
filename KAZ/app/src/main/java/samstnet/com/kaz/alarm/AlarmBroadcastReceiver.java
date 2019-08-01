package samstnet.com.kaz.alarm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.Date;

import samstnet.com.kaz.MainActivity;
import samstnet.com.kaz.R;

import static android.content.Context.NOTIFICATION_SERVICE;


public class AlarmBroadcastReceiver extends BroadcastReceiver {

    String AlarmTitle=new String();
    String AlarmText=new String();
    static int timeCount=0;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("AlarmBroadcastReceiver","onReceive");

        NotificationCompat.Builder builder=new NotificationCompat.Builder(context,"default");

        builder.setSmallIcon(R.mipmap.ic_launcher);

        //제목 설정
        setTitle();
        setText();

        builder.setContentTitle(AlarmTitle);
        builder.setContentText(AlarmText);

        Intent _intent=new Intent(context, MainActivity.class);
        PendingIntent pendingIntent= (PendingIntent) PendingIntent.getActivity(context,
                0,
                _intent,
                PendingIntent.FLAG_UPDATE_CURRENT); //알람이 이미 켜져있다면 내용을 업데이트해줌

        builder.setContentIntent(pendingIntent);    //notification을 누르면 pendingIntent안에 있는게 실행된다.

        Bitmap largeIcon= BitmapFactory.decodeResource(context.getResources(),R.mipmap.ic_launcher);
        builder.setLargeIcon(largeIcon);
        builder.setColor(Color.RED);

        Uri ringtoneUri= RingtoneManager.getActualDefaultRingtoneUri(context,
                RingtoneManager.TYPE_NOTIFICATION); //기본 알람 효과음, 내가 원하는 음악의 uri를 지정할 수 있음
        builder.setSound(ringtoneUri);

        long[] vibrate={0,100,200,300};     //진동
        builder.setVibrate(vibrate);
        builder.setAutoCancel(true); //notification을 클릭을 하면 notification이 날라가게 할 것인가

        NotificationManager manager;
        manager=(NotificationManager)context.getSystemService(NOTIFICATION_SERVICE);

        //오레오 이상에서만 동작, 오레오 이상에서 notificationChannel이 없으면 동작하지 않음
        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.O) {
            manager.createNotificationChannel(new NotificationChannel("default", "기본 채널",
                    NotificationManager.IMPORTANCE_DEFAULT));
        }

        mAlarm.manager.notify(1,builder.build());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setText(){
        switch (timeCount){
            case 1:
                AlarmText="두번째 알람";
                break;
            case 2:
                AlarmText="세번째 알람";
                break;
            case 3:
                AlarmText="네번째 알람";
                break;
            case 4:
                AlarmText="다섯번째 알람";
                break;
            case 5:
                AlarmText="여섯번째 알람";
                break;
            case 6:
                AlarmText="일곱번째 알람";
                break;
            case 7:
                AlarmText="마지막 알람";
                timeCount=0;
                break;
            case 0:
                AlarmText="첫번째 알람";
                break;
        }
        timeCount++;

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setTitle(){
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("HH시 mm분");
        String getTime = sdf.format(date);

        AlarmTitle=getTime;
    }

}
