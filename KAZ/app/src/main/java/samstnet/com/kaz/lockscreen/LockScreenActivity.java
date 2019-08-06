package samstnet.com.kaz.lockscreen;

import android.app.Activity;
import android.app.Fragment;
import android.app.KeyguardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import ng.max.slideview.SlideView;
import samstnet.com.kaz.MainActivity;
import samstnet.com.kaz.R;
import samstnet.com.kaz.eventbus.BusProvider;
import samstnet.com.kaz.eventbus.Customer;
import samstnet.com.kaz.eventbus.WeatherEvent;
import samstnet.com.kaz.eventbus.plant_info;


public class LockScreenActivity extends Activity {


    int n = 5; //아이템 개수    ( 우산, 비료 등등)
    int indexMax = 5; //상태 개수 (추움, 목마름 뭐 이런거)

    MainActivity activity;
    Button button, Nolock;
    TextView textView, textView2, textView3, textView4;
    EditText editText;
    TextView resultTextView;
    ImageView imageView = null;
    String level_string, exp_string;
    Customer cus;
    GlideDrawableImageViewTarget gifImage, backgif;
    ProgressBar myProgressBar;

    private boolean isBind;
    private static Intent serviceIntent;


    //소현----------------------------------------------------------------
    WeatherEvent weatherinfo = null;
    ArrayList<String> wtstate = new ArrayList<>();
    ArrayList<String> tempor = new ArrayList<>();
    ArrayList<Integer> time = new ArrayList<>();
    ImageView imageView_1;
    TextView textview_1;
    FrameLayout framelayout;

    int indexs[];
    int index = 0;
    // items   : 1. 물뿌리개   2. 비료     3. 우산 4. 모자 5. 옷
    // wtstate : 1. manycloud 2. fewcloud 3. sun 4. rain 5. snow
    boolean[][] items =
            {{true, true, false, false, false},
                    {true, true, false, false, false},
                    {true, true, false, true, false},
                    {false, true, true, false, false},
                    {false, true, true, false, true}};

    Button[] buttons;
    ImageView[] itemImage;

    ServiceConnection sconn = new ServiceConnection() {
        @Override //서비스가 실행될 때 호출
        public void onServiceConnected(ComponentName name, IBinder service) {
            // MyService.MyBinder myBinder = (MyService.MyBinder) service;
            //  mService = myBinder.getService();

            isBind = true;
            Log.e("LOG", "onServiceConnected()");
        }

        @Override //서비스가 종료될 때 호출
        public void onServiceDisconnected(ComponentName name) {

            isBind = false;
            Log.e("LOG", "onServiceDisconnected()");
        }
    };

    public LockScreenActivity() {
    }

    //----------------------------------------------------------------

    public void LevelUp(plant_info a) {

        a.setLevel(a.getLevel() + 1);

        if (a.getLevel() == 5) {
            a.setState(a.getState() + 1);
        } else if (a.getLevel() == 10) {
            a.setState(a.getState() + 1);
        }

    }

    public void ExpUp(plant_info a) {
        if (a.getLevel()==1){
            a.setExp(a.getExp()+20);
        }

        else if(a.getLevel()==2 || a.getLevel()==3 ||a.getLevel()==4){
            a.setExp(a.getExp()+15);
        }
        else if(a.getLevel()==5 || a.getLevel()==6 || a.getLevel()==7){
            a.setExp(a.getExp()+10);
        }
        else if(a.getLevel()==8 ||a.getLevel()==9 || a.getLevel()>=10){
            a.setExp(a.getExp()+5);
        }

        if(a.getExp()>=100){
            LevelUp(a);
            a.setExp(0);

        }


    }


    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        KeyguardManager km = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock keyLock = km.newKeyguardLock(KEYGUARD_SERVICE);
        if (LockScreenActivity.serviceIntent == null) {
            serviceIntent = new Intent(this, LockScreenActivity.class);
            startService(serviceIntent);
        } else {
            serviceIntent = LockScreenActivity.serviceIntent;//getInstance().getApplication();
            Toast.makeText(getApplicationContext(), "already", Toast.LENGTH_LONG).show();
        }

        //keyLock.reenableKeyguard();
        cus = (Customer) getApplication();
        //소현------------------------------------------------------------------------------------
        // Register ourselves so that we can provide the initial value.
        BusProvider.getInstance().register(this);

        // 해당 프래그먼트가 켜졌을때 엑티비티에 날씨 정보가 저장되어있다면 가져옴
        // FinishLoad 함수의 경우 날씨 값이 바뀌면 값을 업데이트 해주는것
        // FinishLoad 함수는 Fragment가 연결되어있어야만 수행되기에
        // Fragment 시작시에 초기 날씨 정보 값 적재하는 함수를 따로 만들었음.
        if (MainActivity.getWeatherInfo() != null) {
            weatherinfo = new WeatherEvent(MainActivity.getWeatherInfo());
            wtstate.addAll(weatherinfo.getWstate());
            tempor.addAll(weatherinfo.getTempor());
            time.addAll(weatherinfo.getTime());
        }


        getWindow().addFlags(
                // 기본 잠금화면보다 우선출력

                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED

// 기본 잠금화면 해제시키기
                        | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                        | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);


        getWindow().setContentView(R.layout.fragment_menu1_frag_growth_main2);
        cus = (Customer.getInstance());
        cus.plant1.getLevel();
        Log.d("레벨", "" + cus.plant1.getLevel());



     /* findViewById(R.id.stopService).setOnClickListener(new View.OnClickListener() {


            @Override

            public void onClick(View v) {

            //    finish();

            }

        });



*/

        // ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_menu1_frag_growth_main2, container, false);

        // Nolock=(Button) findViewById(R.id.nolock);
        button = (Button) findViewById(R.id.button4);
        textView = (TextView) findViewById(R.id.LevelResult);
       // textView2 = (TextView) findViewById(R.id.ExpResult);
        imageView = (ImageView) findViewById(R.id.plant1);
        myProgressBar=(ProgressBar)findViewById(R.id.progressBar);

        textView3 = (TextView) findViewById(R.id.temperResult);
        // textView4=(TextView)findViewById(R.id.tvBclock);
        SlideView slideView = (SlideView) findViewById(R.id.slider1);



       /* Nolock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  keyLock.disableKeyguard();
                finish();
            }
        });*/
        gifImage = new GlideDrawableImageViewTarget(imageView);

//        if (cus.plant1.getState() == 1)
//            Glide.with(this).load(R.drawable.sad).into(gifImage);
//        else if (cus.plant1.getState() == 2)
//            Glide.with(this).load(R.drawable.normally).into(gifImage);
//        else if (cus.plant1.getState() == 3)
//            Glide.with(this).load(R.drawable.happy).into(gifImage);
            Glide.with(this).load(plantEmotion()).into(gifImage);

        slideView.setOnSlideCompleteListener(new SlideView.OnSlideCompleteListener() {
            @Override
            public void onSlideComplete(SlideView slideView) {
                // vibrate the device
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(100);

                finish();
            }
        });
        /*button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExpUp(cus.plant1);

                exp_string=cus.plant1.getExp()+"";
                level_string=cus.plant1.getLevel()+"";

                textView.setText(level_string);
                textView2.setText(exp_string);
                //textView.setText("1");

            }});
*/
        //소현-----------------------------------------------------------------------------
        imageView_1 = (ImageView) findViewById(R.id.imageView);
        textview_1 = (TextView) findViewById(R.id.WeatherResult);
        framelayout = (FrameLayout) findViewById(R.id.frame_layout);

        buttons = new Button[n];
        indexs = new int[indexMax];
        itemImage = new ImageView[n];


        /*
>>>>>>> 01c30a1246b6a9c7f586d32c42cd9a3cd270311e
        buttons[0]=(Button)findViewById(R.id.sprinklerButton);
        buttons[1]=(Button)findViewById(R.id.FertilizerButton);
        buttons[2]=(Button)findViewById(R.id.unbrellaButton);
        buttons[3]=(Button)findViewById(R.id.hatButton);
        buttons[4]=(Button)findViewById(R.id.coatButton);


        // item 연결 : 0. 물뿌리개   1. 비료     2. 우산 3. 모자 4. 옷
        itemImage[0]=(ImageView)findViewById(R.id.sprinkler);
        itemImage[1]=(ImageView)findViewById(R.id.Fertilizer);
        itemImage[2]=(ImageView)findViewById(R.id.umbrella);
        itemImage[3]=(ImageView)findViewById(R.id.hat);
        itemImage[4]=(ImageView)findViewById(R.id.coat);

        textView3.setText(tempor.get(0));
        if(MainActivity.getWeatherInfo() != null){
            getIndex();
        }
        */

        set1Image(0);
    }


    //소현----------------------------------------------------------------


    public void onResume() {
        super.onResume();
        Log.d("growth_Fragment", "onResume");
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        BusProvider.getInstance().unregister(this);
        if (serviceIntent != null) {
            stopService(serviceIntent);
            serviceIntent = null;
        }

        Log.d("growth_Fragment", "onDestroy");
    }

    public void onPause() {
        super.onPause();
        Log.d("growth_Fragment", "onPause");
    }

    // 날씨 정보가 업데이트 되면 자동으로 정보를 가져옴
    // using otto libary
    @Subscribe
    public void FinishLoad(WeatherEvent mWeatherEvent) {
        weatherinfo = new WeatherEvent(mWeatherEvent);
        wtstate.addAll(mWeatherEvent.getWstate());
        tempor.addAll(mWeatherEvent.getTempor());
        time.addAll(mWeatherEvent.getTime());
        getIndex();
    }

    public void onStart() {
        super.onStart();
        Log.d("growth_Fragment", "onStart");
    }


    // 버튼 눌리면 아이템 변경
    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                //물뿌리개 선택
                case R.id.sprinklerButton:
                    itemImage[0].setVisibility(View.VISIBLE);
                    buttons[0].setVisibility(View.GONE);
                    cus.plant1.setItems(0);
                    break;
                //비료 선택
                case R.id.FertilizerButton:
                    itemImage[1].setVisibility(View.VISIBLE);
                    buttons[1].setVisibility(View.GONE);
                    cus.plant1.setItems(1);
                    break;
                //우산 선택
                case R.id.unbrellaButton:
                    itemImage[2].setVisibility(View.VISIBLE);
                    buttons[2].setVisibility(View.GONE);
                    cus.plant1.setItems(2);
                    break;
                //모자 선택
                case R.id.hatButton:
                    itemImage[3].setVisibility(View.VISIBLE);
                    buttons[3].setVisibility(View.GONE);
                    cus.plant1.setItems(3);
                    break;
                //옷 선택
                case R.id.coatButton:
                    itemImage[4].setVisibility(View.VISIBLE);
                    buttons[4].setVisibility(View.GONE);
                    cus.plant1.setItems(4);
                    break;
            }

            ExpUp(cus.plant1);
            exp_string = cus.plant1.getExp() + "";
            level_string = cus.plant1.getLevel() + "";

            textView.setText(level_string);
           // textView2.setText(exp_string);

        }
    };

    //날씨정보로 다시 수정해야함
    //날씨 정보를 받아오는 함수 (1번)
    public void getIndex() {
        for (int i = 0; i < n; i++) {
            buttons[i].setVisibility(View.GONE);
            buttons[i].setOnClickListener(listener);
        }

        ImageChange();
    }

    //이미지를 변경하는 함수 (2번)
    public void ImageChange() {
        set1Image(index);
    }


    //이미지 변경 규칙 (3번)
    //날씨에 따라서 얼굴 바뀌는거 해야함 + 날씨에 따른 만족도?
    //(비료, 물뿌리개는 일정 시간이 지나야지만 다시 줄 수 있게 바꿔야 함, 이건 plant 객체에서 바꿔야함)
    public void set1Image(int _index) {
        backgif = new GlideDrawableImageViewTarget(imageView_1);

        if (wtstate.isEmpty()) {
            wtstate.add("empty");
        }
        if (wtstate.get(0) == "manycloud") {
            index = 0;
            //imageView_1.setImageResource(R.drawable.spring);
            Glide.with(this).load(R.drawable.many_cloud).into(backgif);
            textview_1.setText("manycloud");


        } else if (wtstate.get(0) == "fewcloud") {
            index = 1;
            //imageView_1.setImageResource(R.drawable.autumn);
            Glide.with(this).load(R.drawable.fewcloud).into(backgif);

            textview_1.setText("fewcloud");
        } else if (wtstate.get(0) == "sun") {
            index = 2;
            Glide.with(this).load(R.drawable.sunny_day).into(backgif);

            //imageView_1.setImageResource(R.drawable.summer);
            textview_1.setText("sun");
        } else if (wtstate.get(0) == "rain") {
            index = 3;
            //imageView_1.setImageResource(R.drawable.winter);
            Glide.with(this).load(R.drawable.rain).into(backgif);

            textview_1.setText("rain");
        } else if (wtstate.get(0) == "snow") {
            index = 4;
            //imageView_1.setImageResource(R.drawable.sunny);
            Glide.with(this).load(R.drawable.snow).into(backgif);

            textview_1.setText("snow");
        } else if (wtstate.get(0) == "empty") {
            index = 0;
            imageView_1.setImageResource(R.drawable.xkon);
            textview_1.setText("empty");
        }

        exp_string=cus.plant1.getExp()+"";
        level_string=cus.plant1.getLevel()+"";
    //    myProgressBar.setProgress(cus.plant1.getExp());

        textView.setText(level_string);
       // textView2.setText(exp_string);
        textView3.setText(tempor.get(0)+"도");
        //아이템 적용 함수
        //----------------------------------------------------------------


    }
    public int plantEmotion() {
        if (cus.plant1.getState() == 1) {
            if (cus.plant1.getLove() >= 70) {
                return R.drawable.bean1_happy;
            } else if (cus.plant1.getLove() >= 30 && cus.plant1.getLove() < 70) {
                return R.drawable.bean1_normal;
            } else if (cus.plant1.getLove() >= 0 && cus.plant1.getLove() < 30) {
                return R.drawable.bean1_sad;
            }

        } else if (cus.plant1.getState() == 2) {
            if (cus.plant1.getLove() >= 70) {
                return R.drawable.bean2_happy;
            } else if (cus.plant1.getLove() >= 30 && cus.plant1.getLove() < 70) {
                return R.drawable.bean2_happy;
            } else if (cus.plant1.getLove() >= 0 && cus.plant1.getLove() < 30) {
                return R.drawable.bean2_sad;
            }
        } else if (cus.plant1.getState() == 2) {
            if (cus.plant1.getLove() >= 70) {
                return R.drawable.bean3_happy;
            } else if (cus.plant1.getLove() >= 30 && cus.plant1.getLove() < 70) {
                return R.drawable.bean3_happy;
            } else if (cus.plant1.getLove() >= 0 && cus.plant1.getLove() < 30) {
                return R.drawable.bean3_sad;
            }
        }
        else if (cus.plant1.getState() == 3) {
            if (cus.plant1.getLove() >= 70) {
                return R.drawable.bean3_happy;
            } else if (cus.plant1.getLove() >= 30 && cus.plant1.getLove() < 70) {
                return R.drawable.bean3_happy;
            } else if (cus.plant1.getLove() >= 0 && cus.plant1.getLove() < 30) {
                return R.drawable.bean3_sad;
            }
        }
        return R.drawable.bean1_normal;
    }

}




