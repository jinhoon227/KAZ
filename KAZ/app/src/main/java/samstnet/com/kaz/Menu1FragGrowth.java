package samstnet.com.kaz;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.Random;

import samstnet.com.kaz.eventbus.BusProvider;
import samstnet.com.kaz.eventbus.Customer;
import samstnet.com.kaz.eventbus.WeatherEvent;
import samstnet.com.kaz.eventbus.plant_info;

import static samstnet.com.kaz.DayTimeFormatter.night;
import static samstnet.com.kaz.DayTimeFormatter.nowTime_str;


public class Menu1FragGrowth extends Fragment {


    int n=5; //아이템 개수    ( 우산, 비료 등등)
    int indexMax=5; //상태 개수 (추움, 목마름 뭐 이런거)

    MainActivity activity;
    Button button;
    TextView textView,textView2;
    TextView temperResult;
    EditText editText;
    TextView resultTextView;
    ImageView imageView=null;
    String level_string, exp_string;
    Customer cus ;


    //소현----------------------------------------------------------------
    WeatherEvent weatherinfo = null;
    ArrayList<String> wtstate = new ArrayList<>();
    ArrayList<String> tempor = new ArrayList<>();
    ArrayList<Integer> time = new ArrayList<>();
    ImageView imageView_1;
    TextView textview_1;
    FrameLayout framelayout;
    FrameLayout frame1;

    int indexs[];
    int index=0;
    // items   : 1. 물뿌리개   2. 비료     3. 우산 4. 모자 5. 옷
    // wtstate : 1. manycloud 2. fewcloud 3. sun 4. rain 5. snow
    boolean[][] items=
            {{true,true,false,false,false},
                    {true,true,false,false,false},
                    {true,true,false,true,false},
                    {false,true,true,false,false},
                    {false,true,true,false,true}};

    Button[] buttons;
    ImageView[] itemImage;
    GlideDrawableImageViewTarget gifImage,backgif;

    //----------------------------------------------------------------


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) getActivity();

    }
    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;

    }

    public void LevelUp(plant_info a){

        a.setLevel(a.getLevel()+1);

        if(a.getLevel()==5 ){
            a.setState(a.getState()+1);
            Glide.with(this).load(R.drawable.normally).into(gifImage);
        }
        else if(a.getLevel()==10){
            a.setState(a.getState()+1);
            Glide.with(this).load(R.drawable.happy).into(gifImage);
        }

    }
    public void ExpUp(plant_info a){
        a.setExp(a.getExp()+20);

        if(a.getExp()==100){
            LevelUp(a);
            a.setExp(0);

        }


    }
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cus = (Customer)getActivity().getApplication();
        //소현------------------------------------------------------------------------------------
        // Register ourselves so that we can provide the initial value.
        BusProvider.getInstance().register(this);

        // 해당 프래그먼트가 켜졌을때 엑티비티에 날씨 정보가 저장되어있다면 가져옴
        // FinishLoad 함수의 경우 날씨 값이 바뀌면 값을 업데이트 해주는것
        // FinishLoad 함수는 Fragment가 연결되어있어야만 수행되기에
        // Fragment 시작시에 초기 날씨 정보 값 적재하는 함수를 따로 만들었음.
        if( ((MainActivity)getActivity()).getWeatherInfo() != null){
            weatherinfo = new WeatherEvent(((MainActivity)getActivity()).getWeatherInfo());
            wtstate.addAll(weatherinfo.getWstate());
            tempor.addAll(weatherinfo.getTempor());
            time.addAll(weatherinfo.getTime());
        }
        //--------------------------------------------------------------------------------------

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_menu1_frag_growth_main, container, false);
        //인덱스 참고 함수
        button=(Button) rootView.findViewById(R.id.button4);
        textView=(TextView) rootView.findViewById(R.id.LevelResult);
        textView2=(TextView) rootView.findViewById(R.id.ExpResult);
        temperResult  =(TextView)rootView.findViewById(R.id.temperResult);
        imageView=(ImageView)rootView.findViewById(R.id.plant1);
        frame1=(FrameLayout)rootView.findViewById(R.id.frame1);


        textView.setText(level_string);
        textView2.setText(exp_string);
        gifImage=new GlideDrawableImageViewTarget(imageView);

        if(cus.plant1.getState()==1)
            Glide.with(this).load(R.drawable.sad).into(gifImage);
        else if(cus.plant1.getState()==2)
            Glide.with(this).load(R.drawable.normally).into(gifImage);
        else if(cus.plant1.getState()==3)
            Glide.with(this).load(R.drawable.happy).into(gifImage);



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExpUp(cus.plant1);

                exp_string=cus.plant1.getExp()+"";
                level_string=cus.plant1.getLevel()+"";

                textView.setText(level_string);
                textView2.setText(exp_string);

            }});

        //소현-----------------------------------------------------------------------------
        imageView_1=(ImageView)rootView.findViewById(R.id.imageView);
        textview_1=(TextView)rootView.findViewById(R.id.WeatherResult);
        framelayout=(FrameLayout)rootView.findViewById(R.id.frame_layout);

        buttons=new Button[n];
        indexs=new int[indexMax];
        itemImage=new ImageView[n];

        buttons[0]=(Button)rootView.findViewById(R.id.sprinklerButton);
        buttons[1]=(Button)rootView.findViewById(R.id.FertilizerButton);
        buttons[2]=(Button)rootView.findViewById(R.id.unbrellaButton);
        buttons[3]=(Button)rootView.findViewById(R.id.hatButton);
        buttons[4]=(Button)rootView.findViewById(R.id.coatButton);

        if(((MainActivity)getActivity()).getWeatherInfo() != null){
            getIndex();
        }

        //---------------------------------------------------------------------------------




        return rootView;
    }
    //소현----------------------------------------------------------------


    public  void onResume(){
        super.onResume();
        Log.d("growth_Fragment","onResume");
    }

    public  void onStart(){
        super.onStart();
        Log.d("growth_Fragment","onStart");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BusProvider.getInstance().unregister(this);
        Log.d("growth_Fragment","onDestroy");
    }

    public void onPause(){
        super.onPause();
        Log.d("growth_Fragment","onPause");
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

    public void setCharacterImage(int url){
        Glide.with(this).load(url).into(gifImage);
    }


    // 버튼 눌리면 아이템 변경
    View.OnClickListener listener=new View.OnClickListener()
    {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                //물뿌리개 선택
                case R.id.sprinklerButton:
                    setCharacterImage(R.drawable.sprinkler);
                    buttons[0].setVisibility(View.GONE);
                    cus.plant1.setItems(0);
                    break;
                //비료 선택
                case R.id.FertilizerButton:
                    setCharacterImage(R.drawable.scarf);
                    buttons[1].setVisibility(View.GONE);
                    cus.plant1.setItems(1);
                    break;
                //우산 선택
                case R.id.unbrellaButton:
                    setCharacterImage(R.drawable.sunglasses);
                    buttons[2].setVisibility(View.GONE);
                    cus.plant1.setItems(2);
                    break;
                //썬글라스 선택
                case R.id.hatButton:
                    setCharacterImage(R.drawable.sunglasses);
                    buttons[3].setVisibility(View.GONE);
                    cus.plant1.setItems(3);
                    break;
                //목도리 선택
                case R.id.coatButton:
                    setCharacterImage(R.drawable.scarf);
                    buttons[4].setVisibility(View.GONE);
                    cus.plant1.setItems(4);
                    break;
            }

            ExpUp(cus.plant1);
            exp_string=cus.plant1.getExp()+"";
            level_string=cus.plant1.getLevel()+"";

            textView.setText(level_string);
            textView2.setText(exp_string);
            Random rnd = new Random();
            cus.setMoney(cus.getMoney()+rnd.nextInt(10));
        }
    };

    //날씨정보로 다시 수정해야함
    //날씨 정보를 받아오는 함수 (1번)
    public void getIndex(){
        for(int i=0;i<n;i++){
            buttons[i].setVisibility(View.GONE);
            buttons[i].setOnClickListener(listener);
        }

        ImageChange();
    }

    //이미지를 변경하는 함수 (2번)
    public void ImageChange(){
        set1Image(index);
    }


    //이미지 변경 규칙 (3번)
    //날씨에 따라서 얼굴 바뀌는거 해야함 + 날씨에 따른 만족도?
    //(비료, 물뿌리개는 일정 시간이 지나야지만 다시 줄 수 있게 바꿔야 함, 이건 plant 객체에서 바꿔야함)
    public void set1Image(int _index){
        backgif=new GlideDrawableImageViewTarget(imageView_1);

        if(wtstate.isEmpty()) {
            wtstate.add("empty");
        }
        if(wtstate.get(0)=="manycloud"){
            index=0;
            //imageView_1.setImageResource(R.drawable.spring);
            Glide.with(this).load(R.drawable.many_cloud).into(backgif);
            textview_1.setText("manycloud");

        }
        else if(wtstate.get(0)=="fewcloud"){
            index=1;
            //imageView_1.setImageResource(R.drawable.autumn);

            if(DayTimeFormatter.night=="밤"){
                Glide.with(this).load(R.drawable.fewcloud_night).into(backgif);
                Log.d("밤","밤");


            }else {
                Glide.with(this).load(R.drawable.fewcloud).into(backgif);
                Log.d("낮","낮");

            }
            textview_1.setText("fewcloud");




        }
        else if(wtstate.get(0)=="sun"){
            index=2;
            if(DayTimeFormatter.night=="밤"){
                Glide.with(this).load(R.drawable.sunny_night).into(backgif);

            }else {
                Glide.with(this).load(R.drawable.sunny_day).into(backgif);
            }
            //imageView_1.setImageResource(R.drawable.summer);
            textview_1.setText("sun");

        }
        else if(wtstate.get(0)=="rain"){
            index=3;
            //imageView_1.setImageResource(R.drawable.winter);
            Glide.with(this).load(R.drawable.rain).into(backgif);

            textview_1.setText("rain");
        }
        else if(wtstate.get(0)=="snow"){
            index=4;
            //imageView_1.setImageResource(R.drawable.sunny);
            Glide.with(this).load(R.drawable.snow).into(backgif);

            textview_1.setText("snow");
        }
        else if(wtstate.get(0)=="empty"){
            index=0;
            imageView_1.setImageResource(R.drawable.xkon);
            textview_1.setText("empty");
        }

        for(int i=0;i<n;i++){
            if(items[index][i]&&!cus.getPlant().getItems(i)){
                if(cus._getItem(i).isWear()&&cus._getItem(i).isBuy())
                    buttons[i].setVisibility(View.VISIBLE);
            }
        }

        //변경을 다 했다면 FragGrowth보여주기
        frame1.setVisibility(View.VISIBLE);

        exp_string=cus.plant1.getExp()+"";
        level_string=cus.plant1.getLevel()+"";

        textView.setText(level_string);
        textView2.setText(exp_string);
    }


    //아이템 적용 함수

    //----------------------------------------------------------------


}