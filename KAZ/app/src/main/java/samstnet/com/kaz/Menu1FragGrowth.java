package samstnet.com.kaz;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import samstnet.com.kaz.eventbus.BusProvider;
import samstnet.com.kaz.eventbus.WeatherEvent;

public class Menu1FragGrowth extends Fragment {

    WeatherEvent weatherinfo = null;
    ArrayList<String> wtstate = new ArrayList<>();
    ArrayList<String> tempor = new ArrayList<>();
    ArrayList<Integer> time = new ArrayList<>();
    //소현----------------------------------------------------------------
    ImageView imageView;
    TextView textview;
    FrameLayout framelayout;

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

    int n=5; //버튼 최대 개수
    int indexMax=5; //상태 최대 개수
    //----------------------------------------------------------------

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView=(ViewGroup)inflater.inflate(R.layout.fragment_menu1_frag_growth,container,false);
        imageView=(ImageView)rootView.findViewById(R.id.imageView);
        textview=(TextView)rootView.findViewById(R.id.textView2);
        framelayout=(FrameLayout)rootView.findViewById(R.id.frame_layout);

        buttons=new Button[n];
        indexs=new int[indexMax];
        itemImage=new ImageView[n];

        for(int i=0;i<n;i++) {
            buttons[i] = (Button) rootView.findViewById(R.id.button17 + i);
            //itemImage[i]=(ImageView)rootView.findViewById(R.id.umbrella);
        }

        // item 연결 : 0. 물뿌리개   1. 비료     2. 우산 3. 모자 4. 옷
        itemImage[0]=(ImageView)rootView.findViewById(R.id.sprinkler);
        itemImage[1]=(ImageView)rootView.findViewById(R.id.Fertilizer);
        itemImage[2]=(ImageView)rootView.findViewById(R.id.umbrella);
        itemImage[3]=(ImageView)rootView.findViewById(R.id.hat);
        itemImage[4]=(ImageView)rootView.findViewById(R.id.coat);

        if(((MainActivity)getActivity()).getWeatherInfo() != null){
            getIndex();
        }


        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BusProvider.getInstance().unregister(this);
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


    //소현----------------------------------------------------------------

    // 버튼 눌리면 아이템 변경
    View.OnClickListener listener=new View.OnClickListener()
    {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                //물뿌리개 선택
                case R.id.button17:
                    itemImage[0].setVisibility(View.VISIBLE);
                    buttons[0].setVisibility(View.GONE);
                    break;
                //비료 선택
                case R.id.button18:
                    itemImage[1].setVisibility(View.VISIBLE);
                    buttons[1].setVisibility(View.GONE);
                    break;
                //우산 선택
                case R.id.button19:
                    itemImage[2].setVisibility(View.VISIBLE);
                    buttons[2].setVisibility(View.GONE);
                    break;
                //모자 선택
                case R.id.button20:
                    itemImage[3].setVisibility(View.VISIBLE);
                    buttons[3].setVisibility(View.GONE);
                    break;
                //옷 선택
                case R.id.button21:
                    itemImage[4].setVisibility(View.VISIBLE);
                    buttons[4].setVisibility(View.GONE);
                    break;
            }
        }
    };

    //이미지 변경 규칙
    public void set1Image(int _index){

        if(wtstate.isEmpty()) {
            wtstate.add("empty");
        }

       if(wtstate.get(0)=="manycloud"){
           index=0;
            imageView.setImageResource(R.drawable.spring);
            textview.setText("manycloud");
        }
        else if(wtstate.get(0)=="fewcloud"){
           index=1;
            imageView.setImageResource(R.drawable.autumn);
            textview.setText("fewcloud");
        }
        else if(wtstate.get(0)=="sun"){
           index=2;
            imageView.setImageResource(R.drawable.summer);
            textview.setText("sun");
        }
        else if(wtstate.get(0)=="rain"){
           index=3;
            imageView.setImageResource(R.drawable.winter);
            textview.setText("rain");
        }
        else if(wtstate.get(0)=="snow"){
           index=4;
            imageView.setImageResource(R.drawable.sunny);
            textview.setText("snow");
        }
        else if(wtstate.get(0)=="empty"){
           index=0;
            imageView.setImageResource(R.drawable.xkon);
            textview.setText("empty");
        }

        for(int i=0;i<n;i++){
            if(items[index][i]){
                buttons[i].setVisibility(View.VISIBLE);
            }
        }

    }

    //날씨정보로 다시 수정해야함
    //날씨 정보를 받아오는 함수
    public void getIndex(){
        for(int i=0;i<n;i++){
            buttons[i].setVisibility(View.GONE);
            buttons[i].setOnClickListener(listener);
        }

        ImageChange();
    }

    //이미지를 변경하는 함수
    public void ImageChange(){
        set1Image(index);
    }



    //아이템 적용 함수

    //----------------------------------------------------------------

}