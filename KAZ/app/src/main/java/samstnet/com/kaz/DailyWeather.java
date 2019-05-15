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

import java.util.ArrayList;

public class DailyWeather extends Fragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView=(ViewGroup)inflater.inflate(R.layout.dailyweather,container,false);
        ListView listView=rootView.findViewById(R.id.list);
        SingerAdapter adapter=new SingerAdapter();
        adapter.addItem(new WeatherItem("소녀시대","010-1000-1000",R.drawable.bts2));
        adapter.addItem(new WeatherItem("걸스데이","010-2000-1000",R.drawable.bts3));
        adapter.addItem(new WeatherItem("에이핑크","010-3000-1000",R.drawable.drawable1));
        adapter.addItem(new WeatherItem("블랙핑크","010-4000-1000",R.drawable.drawable2));
        adapter.addItem(new WeatherItem("아이즈원","010-5000-1000",R.drawable.bts2));
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
            view.setName(item.getName());
            view.setMobile(item.getMobile());
            view.setImage(item.getResId());
            return view;
        }
    }


    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d("what","the");
    }


}
