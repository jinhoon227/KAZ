package samstnet.com.kaz.lockscreen;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import samstnet.com.kaz.Lovestatetime;
import samstnet.com.kaz.MainActivity;
import samstnet.com.kaz.R;
import samstnet.com.kaz.alarm.NonDisturb;
import samstnet.com.kaz.eventbus.BusProvider;
import samstnet.com.kaz.eventbus.Customer;

public class Menu4FragConfig extends Fragment {

    private Button onBtn, offBtn;
    TextView tx;
    MainActivity activity;

    private Switch create;
    private Switch sound;
    private Switch screen;

    Customer cus;
    NotificationCompat.Builder builder;
    Intent intent3;
    Intent intent;
    Intent intent1;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) getActivity();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BusProvider.getInstance().unregister(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;

    }
    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        intent = new Intent(getActivity().getApplication(), ScreenService.class);
        intent1 = new Intent(getContext().getApplicationContext(),NonDisturb.class);
        intent3=new Intent(getContext().getApplicationContext(), Lovestatetime.class);
        cus=(Customer)getActivity().getApplication();
        Log.d("yeaahna","yeah");

        /*
        if (!cus.setting1.isCreateevent()) {
            getContext().startService(MainActivity.intent);
            if(!cus.setting1.isScreen()) {
                getActivity().startService(intent);
            }
            if(!cus.setting1.isSoundevent()) {
                getContext().startService(intent1);
            }
        }*/

    }
    @Nullable

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_menu4_frag_config, container, false);
        //onBtn= (Button) rootView.findViewById(R.id.startService);
        //offBtn = (Button) rootView.findViewById(R.id.stopService);
        create = rootView.findViewById(R.id.create);
        sound=rootView.findViewById(R.id.sound);
        screen=rootView.findViewById(R.id.screen);

        //알림
        if (!cus.setting1.isCreateevent()) {
            Log.d("알림","on");

            create.setChecked(true);
            screen.setEnabled(true);
            sound.setEnabled(true);
            //getContext().startService(MainActivity.intent);

        }else{
            Log.d("알림","off");
//            getContext().sendBroadcast(intent3);
            Log.e("Menu4에서 알림 off", String.valueOf(cus.plant1.getLove()));
            create.setChecked(false);
            screen.setEnabled(false);
            sound.setEnabled(false);
        }

        //잠금 설정
        if(!cus.setting1.isScreen()){
            Log.d("잠금","on");
            screen.setChecked(true);
        }else{
            Log.d("잠금","off");
            screen.setChecked(false);
        }

        //방해 금지 모드 설정
        if(!cus.setting1.isSoundevent()){
            Log.d("방해 금지 모드","on");
            sound.setChecked(true);
        }else{
            Log.d("방해 금지 모드","off");
            sound.setChecked(false);
        }



        create.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //알림을 켰을때
                if (cus.setting1.isCreateevent()) {
                    // The toggle is enabled
                    Log.d("알림 버튼","on");
                    cus.setting1.setCreateevent(false);
                    getContext().startService(MainActivity.intent);

                    sound.setEnabled(true);
                    screen.setEnabled(true);

                    //잠금화면
                    if(!cus.setting1.isScreen()){
                        getActivity().startService(intent);
                    }
                    //방해금지
                    if(!cus.setting1.isScreen()){
                        getContext().startService(intent1);
                    }
                }
                //알림을 껐을때
                else {
                    // The toggle is disabled
                    Log.d("알림 버튼","off");
                    cus.setting1.setCreateevent(true);
                    getContext().stopService(MainActivity.intent);
                    sound.setEnabled(false);
                    screen.setEnabled(false);

                    if(!cus.setting1.isScreen()){
                        getActivity().stopService(intent);
                    }
                    if(!cus.setting1.isSoundevent()){
                        getActivity().stopService(intent1);
                    }

                }
            }
        });

        sound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //방해 금지 모드 온
                if(cus.setting1.isSoundevent()){
                    Log.d("Menu4FragConfig","sound on");
                    cus.setting1.setSoundevent(false);
                    //getContext().startService(intent1);
                }
                //방해 금지 모드 오프
                else{
                    Log.d("Menu4FragConfig","sound off");
                    cus.setting1.setSoundevent(true);
                    //getContext().stopService(intent1);
                }
            }
        });

        screen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //잠금 온
                if(cus.setting1.isScreen()){
                    Log.d("Menu4FragConfig","screen on");
                    cus.setting1.setScreen(false);
                    getActivity().startService(intent);
               }
                //잠금 오프
                else{
                    getActivity().stopService(intent);
                    Log.d("Menu4FragConfig","screen off");
                    cus.setting1.setScreen(true);
                }
            }
        });


        return rootView;
    }


    private void createNotification() {
//        audioManager=(AudioManager)getContext().getSystemService(Context.AUDIO_SERVICE);
        builder = new NotificationCompat.Builder(getView().getContext(),"default");
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("마!이게 알림이다!");
        builder.setContentText("알람 세부 텍스트");
        builder.setColor(Color.RED);
        builder.setDefaults(Notification.DEFAULT_ALL);
        //audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        // 사용자가 탭을 클릭하면 자동 제거
        builder.setAutoCancel(true);

        // 알림 표시
        NotificationManager notificationManager = (NotificationManager) getView().getContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(new NotificationChannel("default", "기본 채널", NotificationManager.IMPORTANCE_DEFAULT));
            NotificationChannel notificationChannel = new NotificationChannel("channel_id", "channel_name", NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.GREEN);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 100, 200});
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        // id값은
        // 정의해야하는 각 알림의 고유한 int값
        notificationManager.notify(1, builder.build());
    }
    private void removeNotification() {
        // Notification 제거
        NotificationManagerCompat.from(getView().getContext()).cancel(1);
    }
//    private void soundNotification(){
//        builder.setDefaults(Notification.DEFAULT_SOUND|Notification.DEFAULT_VIBRATE);
//    }
}