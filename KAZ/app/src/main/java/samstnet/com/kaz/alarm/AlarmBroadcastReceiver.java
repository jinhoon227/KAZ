package samstnet.com.kaz.alarm;

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
import samstnet.com.kaz.Service.ExampleService;
import samstnet.com.kaz.eventbus.BusProvider;
import samstnet.com.kaz.eventbus.Customer;


public class AlarmBroadcastReceiver extends BroadcastReceiver {

    String AlarmTitle=new String();
    String AlarmText=new String();
    static int timeCount=0;
    Uri ringtoneUri;
    NotificationCompat.Builder builder;
    Customer cus;
    static boolean error;

    //온도
    String temp;
    int tem=-1;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("AlarmBroadcastReceiver","onReceive");

        builder=new NotificationCompat.Builder(context,"default");
        cus=new Customer();
        if(cus.plant1.getLove()>0)
        {
            cus.plant1.setLove(cus.plant1.getLove()-5);
        }
        Log.d("love",String.valueOf(cus.plant1.getLove()));
        builder.setSmallIcon(R.mipmap.ic_launcher);
        BusProvider.getInstance().register(this);

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

        ringtoneUri= RingtoneManager.getActualDefaultRingtoneUri(context,
                RingtoneManager.TYPE_NOTIFICATION); //기본 알람 효과음, 내가 원하는 음악의 uri를 지정할 수 있음

        builder.setSound(ringtoneUri);

        long[] vibrate={0,100,200,300};     //진동
        builder.setVibrate(vibrate);
        builder.setAutoCancel(true); //notification을 클릭을 하면 notification이 날라가게 할 것인가

        error=false;

        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("HH");
        String getTime = sdf.format(date);
        int hour = Integer.valueOf(getTime);

        if(NonDisturb.startTime>NonDisturb.endTime)
            hour+=24;

        Log.d("검사들어가유",String.valueOf(hour));

        if(!cus.setting1.isSoundevent()) {
            if (!(NonDisturb.startTime < hour && NonDisturb._endTime >= hour)) {
                mAlarm.manager.notify(1, builder.build());
                if(error) {
                    Log.d("Alarm","아 에러;;");
                    ExampleService.sendAlarm();
                }
            }
            else {
                Log.d("방해금지시간이에유", "옹");
            }
        }
        else {
            mAlarm.manager.notify(1, builder.build());
            if(error) {
                Log.d("Alarm","또 에러;;");
                ExampleService.sendAlarm();
            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setText(){
        temp = MainActivity.tempor.get(0);
        tem=Integer.parseInt(temp);

        if(MainActivity.tempor.size()!=0) {
            AlarmTitle = "현재 "+MainActivity.cityInfo+"의 날씨는"+temp+"도";
        }
        else {
            error=true;
            AlarmTitle="저는 집에 갈 수 없어요ㅜㅜ";
        }

        if(MainActivity.wtstate.size()==0){
            AlarmText="새로고침해라";
        }
        else if(MainActivity.wtstate.get(0)=="manycloud"){
            //AlarmTitle="흐림";
            if(tem>=30)
                AlarmText=heat();
            else if(tem<=0)
                AlarmText=cold();
            else
                AlarmText = "구름이 많아요! 흐린 날씨에 주의하세요";
        }
        else if(MainActivity.wtstate.get(0)=="fewcloud"){
            //AlarmTitle="구름";
            if(tem>=30)
                AlarmText=heat();
            else if(tem<=0)
                AlarmText=cold();
            else
                AlarmText="날씨가 좋아요. 산책하러갈까요?";
        }
        else if(MainActivity.wtstate.get(0)=="sun"){
            //AlarmTitle="태양";
            if(tem>=30)
                AlarmText=heat();
            else if(tem<=0)
                AlarmText=cold();
            else
                AlarmText="화창한 날씨에요. 함께 나가요";
        }
        else if(MainActivity.wtstate.get(0)=="rain"){
            //AlarmTitle="비";
            if(tem>=30)
                AlarmText="지금은 덥고 습한 날씨에요!. 잊지말고 우산 꼭 챙기세요";
            else if(tem<=0)
                AlarmText="비예보도 있는데 춥기까지하네. 우산 챙기세요";
            else
                AlarmText="비 예보가 있어요. 잊지말고 우산 챙기세요!";
        }
        else if(MainActivity.wtstate.get(0)=="snow"){
            //AlarmTitle="눈";
            if(tem>=30)
                AlarmText="날씨가 미쳤어";
            else if(tem<=0)
                AlarmText="영하 온도에 눈까지 와요. 야외 활동을 자제하세요";
            else
                AlarmText="눈 예보가 있어요. 따뜻하게 입고 나가요!";
        }
        else {
            error=true;
            AlarmText="아 속안좋아";
        }

    }

    public String heat(){
        return "날씨가 많이 더워요. 물 많이 마시고 야외활동을 자세하세요!";
    }

    public String cold(){
        return "으슬으슬 너무 추워요. 따뜻하게 입고나가요~";
    }

}
