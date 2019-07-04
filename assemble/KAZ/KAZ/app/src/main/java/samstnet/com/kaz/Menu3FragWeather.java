package samstnet.com.kaz;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import samstnet.com.kaz.eventbus.BusProvider;
import samstnet.com.kaz.eventbus.WeatherEvent;


/**
 * A simple {@link Fragment} subclass.
 */
public class Menu3FragWeather extends Fragment {
    WeatherEvent weatherinfo = null;
    ArrayList<String> wtstate = new ArrayList<>();
    ArrayList<String> tempor = new ArrayList<>();
    ArrayList<Integer> time = new ArrayList<>();

    public Menu3FragWeather() {
        // Required empty public constructor
    }

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu3_frag_weather, container, false);
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
    }

}
