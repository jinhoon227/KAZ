package samstnet.com.kaz;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import samstnet.com.kaz.eventbus.BusProvider;
import samstnet.com.kaz.eventbus.WeatherEvent;

public class DailyWeather extends Fragment {
    WeatherEvent weatherinfo = null;
    ArrayList<String> wtstate = new ArrayList<>();
    ArrayList<String> tempor = new ArrayList<>();
    ArrayList<Integer> time = new ArrayList<>();
    int img[]=new int[15];
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BusProvider.getInstance().register(this);
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
        ViewGroup rootView=(ViewGroup)inflater.inflate(R.layout.dailyweather,container,false);
        ListView listView=rootView.findViewById(R.id.list);
        SingerAdapter adapter=new SingerAdapter();
        for(int i=0;i<14;i++){
            if(wtstate.get(i)=="sun"){
                img[i]=R.drawable.sunny;
            }
            else if(wtstate.get(i)=="fewcloud"){
                img[i]=R.drawable.cloudy;
            }
            else if(wtstate.get(i)=="manycloud"){
                img[i]=R.drawable.cloudy2;
            }
            else if(wtstate.get(i)=="rain"){
                img[i]=R.drawable.rainy;
            }
            else if(wtstate.get(i)=="snow"){
                img[i]=R.drawable.snowy;
            }
        }
        adapter.addItem(new WeatherItem(String.valueOf(time.get(0))+"시",tempor.get(0),wtstate.get(0),img[0]));
        adapter.addItem(new WeatherItem(String.valueOf(time.get(1))+"시",tempor.get(1),wtstate.get(1),img[1]));
        adapter.addItem(new WeatherItem(String.valueOf(time.get(2))+"시",tempor.get(2),wtstate.get(2),img[2]));
        adapter.addItem(new WeatherItem(String.valueOf(time.get(3))+"시",tempor.get(3),wtstate.get(3),img[3]));
        adapter.addItem(new WeatherItem(String.valueOf(time.get(4))+"시",tempor.get(4),wtstate.get(4),img[4]));
        listView.setAdapter(adapter);
        return rootView;
    }
    class SingerAdapter extends BaseAdapter {
        ArrayList<WeatherItem> items=new ArrayList<WeatherItem>();
        @Override
        public int getCount() {
            return items.size();
        }
        public void addItem(WeatherItem item){
            items.add(item);
        }
        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            WeatherItemView view=new WeatherItemView(getActivity().getApplicationContext());

            WeatherItem item=items.get(position);
            view.setTime(item.getTime());
            view.setTempor(item.getTempor());
            view.setWtstate(item.getWtstate());
            view.setImage(item.getResId());
            return view;
        }
    }
    @Subscribe
    public void FinishLoad(WeatherEvent mWeatherEvent) {
        weatherinfo = new WeatherEvent(mWeatherEvent);
        wtstate.addAll(mWeatherEvent.getWstate());
        tempor.addAll(mWeatherEvent.getTempor());
        time.addAll(mWeatherEvent.getTime());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BusProvider.getInstance().unregister(this);
    }


}
