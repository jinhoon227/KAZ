package samstnet.com.kaz.lockscreen;

import android.app.Activity;
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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import samstnet.com.kaz.MainActivity;
import samstnet.com.kaz.R;
import samstnet.com.kaz.eventbus.BusProvider;

public class Menu4FragConfig extends Fragment {

    private Button onBtn, offBtn;
    TextView tx;
    MainActivity activity;
    private Switch create;
    private Switch sound;
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

        Log.d("yeaahna","yeah");
        //getActivity().setContentView(R.layout.fragment_menu4_frag_config);




    }
    @Nullable

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_menu4_frag_config, container, false);

        onBtn = (Button) rootView.findViewById(R.id.startService);

        offBtn = (Button) rootView.findViewById(R.id.stopService);


        onBtn.setOnClickListener(new OnClickListener() {

            @Override

            public void onClick(View v) {

                Intent intent = new Intent(getActivity().getApplication(), ScreenService.class);

                getActivity().startService(intent);

//                tx=(TextView) getActivity().findViewById(R.id.textView4);
//                tx.setText("0");

            }
        });
        offBtn.setOnClickListener(new OnClickListener() {

            @Override

            public void onClick(View v) {

                Intent intent = new Intent(getActivity().getApplication(),ScreenService.class);

                getActivity().stopService(intent);

            }

        });
        create = rootView.findViewById(R.id.create);
        sound=rootView.findViewById(R.id.sound);
        create.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    createNotification();
                } else {
                    // The toggle is disabled
                    removeNotification();
                    sound.setChecked(false);


                }
            }
        });


        return rootView;
    }
//    class soundSwitchListner implements CompoundButton.OnCheckedChangeListener{
//        @Override
//        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
//            if(isChecked){
//
//            }
//        }
//
//    }

    private void createNotification() {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getView().getContext(),"default");

        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("마!이게 알림이다!");
        builder.setContentText("알람 세부 텍스트");
        builder.setColor(Color.RED);
        // 사용자가 탭을 클릭하면 자동 제거
        builder.setAutoCancel(true);

        // 알림 표시
        NotificationManager notificationManager = (NotificationManager) getView().getContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(new NotificationChannel("default", "기본 채널", NotificationManager.IMPORTANCE_DEFAULT));
            NotificationChannel notificationChannel = new NotificationChannel("channel_id", "channel_name", NotificationManager.IMPORTANCE_DEFAULT); notificationChannel.setDescription("channel description"); notificationChannel.enableLights(true); notificationChannel.setLightColor(Color.GREEN); notificationChannel.enableVibration(true); notificationChannel.setVibrationPattern(new long[]{100, 200, 100, 200}); notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE); notificationManager.createNotificationChannel(notificationChannel);


        }
        // id값은
        // 정의해야하는 각 알림의 고유한 int값
        notificationManager.notify(1, builder.build());
    }
    private void removeNotification() {
        // Notification 제거

        NotificationManagerCompat.from(getView().getContext()).cancel(1);
    }
}