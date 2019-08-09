package samstnet.com.kaz.alarm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
    static public int count=0;
    Uri ringtoneUri;
    NotificationCompat.Builder builder;
    Customer cus;
    static boolean error;

    //온도
    String temp;
    int tem=-1;

    //시간
    int _hour;  //현재 시간
    int hour; //24시간 더한 버전

    Bitmap largeIcon;

    // 1. 맑음     2. 구름   3. 흐림   4. 비    5. 눈    6. 밤
    int nImg=0;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("AlarmBroadcastReceiver","onReceive");

        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("HH");
        String getTime = sdf.format(date);
        hour = Integer.valueOf(getTime);
        _hour=Integer.valueOf(getTime);

        builder=new NotificationCompat.Builder(context,"default");
        Intent _intent=new Intent(context, MainActivity.class);
        PendingIntent pendingIntent= (PendingIntent) PendingIntent.getActivity(context,
                0,
                _intent,
                PendingIntent.FLAG_UPDATE_CURRENT); //알람이 이미 켜져있다면 내용을 업데이트해줌

        NotificationManager manager=(NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);
        //오레오 이상에서만 동작, 오레오 이상에서 notificationChannel이 없으면 동작하지 않음
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) {
            manager.createNotificationChannel(new NotificationChannel("default", "기본 채널",
                    NotificationManager.IMPORTANCE_DEFAULT));
        }

        builder.setContentIntent(pendingIntent);    //notification을 누르면 pendingIntent안에 있는게 실행된다.

        if(NonDisturb.startTime>NonDisturb.endTime)
            hour+=24;

        BusProvider.getInstance().register(this);

        Log.e("AlarmBroadcastReceiver",String.valueOf(count));

        if(AlarmBroadcastReceiver.count>9) {
            if(!cus.setting1.isSoundevent())
                if (!(NonDisturb.startTime < hour && NonDisturb._endTime >= hour)) {
                    builder.setSmallIcon(R.drawable.ssun);
                    builder.setContentTitle("날씨 데이터를 받아오지 못했어요");
                    builder.setContentText("WIFI를 확인해주세요");
                    manager.notify(1, builder.build());
                    return;
                }
        }

        setText();

        builder.setContentTitle(AlarmTitle);
        builder.setContentText(AlarmText);


        switch (nImg){
            case 1:
                builder.setSmallIcon(R.drawable.ssun);
                break;
            case 2:
                builder.setSmallIcon(R.drawable.sfewcloudy);
                break;
            case 3:
                builder.setSmallIcon(R.drawable.scloudy);
                break;
            case 4:
                builder.setSmallIcon(R.drawable.srain);
                break;
            case 5:
                builder.setSmallIcon(R.drawable.ssnow);
                break;
            case 6:
                builder.setSmallIcon(R.drawable.smoon);
                break;
            case 0:
                builder.setSmallIcon(R.drawable.kong);
                break;
        }

        builder.setColor(Color.GREEN);

        ringtoneUri= RingtoneManager.getActualDefaultRingtoneUri(context,
                RingtoneManager.TYPE_NOTIFICATION); //기본 알람 효과음, 내가 원하는 음악의 uri를 지정할 수 있음

        builder.setSound(ringtoneUri);

        //builder.setSmallIcon(R.drawable.ssun);

        long[] vibrate={0,100,200,300};     //진동
        builder.setVibrate(vibrate);
        builder.setAutoCancel(true); //notification을 클릭을 하면 notification이 날라가게 할 것인가

        error=false;

        Log.d("검사들어가유",String.valueOf(hour));


            if (!cus.setting1.isSoundevent()) {
                if (!(NonDisturb.startTime < hour && NonDisturb._endTime >= hour)) {
                    manager.notify(1, builder.build());
                    if (error) {
                        Log.d("Alarm", "아 에러;;");
                        ExampleService.sendAlarm();
                    }
                } else {
                    Log.d("방해금지시간이에유", "옹");
                }
            } else {
                manager.notify(1, builder.build());
                if (error) {
                    Log.d("Alarm", "또 에러;;");
                    ExampleService.sendAlarm();
                }
            }


    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setText(){
        temp = MainActivity.tempor.get(count);
        tem=Integer.parseInt(temp);

        if(MainActivity.tempor.size()!=0) {
            AlarmTitle = "현재 "+MainActivity.cityInfo+"의 날씨는"+temp+"도 입니다.";
        }
        else {
            error=true;
            AlarmTitle="에러가 났어요";
        }

        if(MainActivity.wtstate.size()==0){
            AlarmText="앱을 다시 켜주세요";
        }
        else if(MainActivity.wtstate.get(count)=="manycloud"){
            //AlarmTitle="흐림";
            nImg=3;
            if(tem>=30&&(_hour<=20&&_hour>=6))
                AlarmText = heat();
            else if(tem>=28&&(_hour>20||_hour<6)) {
                AlarmText = nightHeat();
                nImg=6;
            }
            else if(tem<=0)
                AlarmText=cold();
            else if(_hour>20||_hour<6) {
                AlarmText = "좋은 밤이에요. 오늘 하루는 어땠나요?";
                nImg=6;
            }
            else
                AlarmText = "구름이 많아요! 날씨가 흐려도 기운내세요";

        }
        else if(MainActivity.wtstate.get(count)=="fewcloud"){
            //AlarmTitle="구름";
            nImg=2;
            if(tem>=30&&(_hour<=20&&_hour>=6))
                AlarmText=heat();
            else if(tem>=28&&(_hour>20||_hour<6)) {
                AlarmText = nightHeat();
                nImg=6;
            }
            else if(tem<=0)
                AlarmText=cold();
            else if(_hour>20||_hour<6) {
                AlarmText = "좋은 밤이에요. 오늘 밤은 좋은 꿈 꾸세요";
                nImg=6;
            }
            else
                AlarmText="날씨가 좋아요. 산책하러갈까요?";
        }
        else if(MainActivity.wtstate.get(count)=="sun"){
            //AlarmTitle="태양";
            nImg=1;
            if(tem>=30&&(_hour<=20&&_hour>=6))
                AlarmText=heat();
            else if(tem>=28&&(_hour>20||_hour<6)) {
                AlarmText = nightHeat();
                nImg=6;
            }
            else if(tem<=0)
                AlarmText=cold();
            else if(_hour>20||_hour<6) {
                AlarmText = "좋은 밤이에요. 오늘은 달이 잘 보이겠어요";
                nImg=6;
            }
            else
                AlarmText="화창한 날씨에요. 함께 나가요";
        }
        else if(MainActivity.wtstate.get(count)=="rain"){
            //AlarmTitle="비";
            nImg=4;
            if(tem>=30)
                AlarmText="덥고 습한 날이에요. 비예보가 있으니 잊지말고 우산 꼭 챙기세요";
            else if(tem<=0)
                AlarmText="비예보도 있는데 춥기까지! 꼭 우산 챙기세요";
            else
                AlarmText="비 예보가 있어요. 잊지말고 우산 챙기세요!";
        }
        else if(MainActivity.wtstate.get(count)=="snow"){
            //AlarmTitle="눈";
            nImg=5;
            if(tem>=30)
                AlarmText="날씨가 미쳤어!";
            else if(tem<=0)
                AlarmText="영하 온도에 눈까지 와요. 야외 활동을 자제하세요";
            else
                AlarmText="눈 예보가 있어요. 따뜻하게 입고 나가요!";
        }
        else {
            error=true;
            AlarmText="알람을 껐다 켜주세요";
        }

    }

    public String heat(){
        return "날씨가 많이 더워요. 물 많이 마시고 야외활동을 자제하세요!";
    }

    public String cold(){
        return "으슬으슬 너무 추워요. 따뜻하게 입고나가요";
    }

    public String nightHeat(){
        return "더운 밤이에요. 잠자리에 들 때 주의하세요";
    }

}
