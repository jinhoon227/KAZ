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
import android.widget.ImageView;
import android.widget.ListView;

import com.squareup.otto.Subscribe;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import samstnet.com.kaz.eventbus.BusProvider;
import samstnet.com.kaz.eventbus.Customer;
import samstnet.com.kaz.eventbus.WeatherEvent;

public class DailyWeather extends Fragment {
    WeatherEvent weatherinfo = null;
    ArrayList<String> wtstate = new ArrayList<>();
    ArrayList<String> wtstatePresent = new ArrayList<>();
    ArrayList<String> tempor = new ArrayList<>();
    ArrayList<Integer> time = new ArrayList<>();
    ArrayList<String> timeStr = new ArrayList<>();
    //int img[]=new int[15];
    ArrayList<Integer> img=new ArrayList<>();
    ImageView tutorial;

    @Override
    public void onResume(){
        super.onResume();
        //장시간 백그라운드에 있다가 다시 돌아왔을때 날씨가 변경되었으면 해당부분실행
        //날씨 다시가져와서 리스트 업데이트(리스트는 업데이트는 finshload 호출을통해서)
        //finshload 로 뿌려주기에 주간도 업데이트됨
        if(time.size()!=0){
            long now = System.currentTimeMillis();
            Date date = new Date(now);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
            String getTime = sdf.format(date);
            String today[] = getTime.split("-");
            if(time.get(0)==21){
                //오후9시일경우
                if(Integer.parseInt(today[3])<21) {
                    Log.d("resume", "yesNine");
                    ((MainActivity) getActivity()).UsingGps();
                }
            }
            else if(Integer.parseInt(today[3])>=time.get(0)+3) {
                Log.d("resume", "yes");
                ((MainActivity) getActivity()).UsingGps();
            }
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BusProvider.getInstance().register(this);
    }

    SingerAdapter adapter;
    ListView listView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView=(ViewGroup)inflater.inflate(R.layout.dailyweather,container,false);
        listView=rootView.findViewById(R.id.list);
        tutorial = rootView.findViewById(R.id.tutorial3);
        adapter=new SingerAdapter();

        if( ((MainActivity)getActivity()).getWeatherInfo() != null){
            weatherinfo = new WeatherEvent(((MainActivity)getActivity()).getWeatherInfo());
            wtstate.addAll(weatherinfo.getWstate());
            tempor.addAll(weatherinfo.getTempor());
            time.addAll(weatherinfo.getTime());
            changeStringFormatter();

            adapter.addItem(new WeatherItem(timeStr.get(0),tempor.get(0),wtstatePresent.get(0),img.get(0)));
            adapter.addItem(new WeatherItem(timeStr.get(1),tempor.get(1),wtstatePresent.get(1),img.get(1)));
            adapter.addItem(new WeatherItem(timeStr.get(2),tempor.get(2),wtstatePresent.get(2),img.get(2)));
            adapter.addItem(new WeatherItem(timeStr.get(3),tempor.get(3),wtstatePresent.get(3),img.get(3)));
            adapter.addItem(new WeatherItem(timeStr.get(4),tempor.get(4),wtstatePresent.get(4),img.get(4)));
            adapter.addItem(new WeatherItem(timeStr.get(5),tempor.get(5),wtstatePresent.get(5),img.get(5)));
            adapter.addItem(new WeatherItem(timeStr.get(6),tempor.get(6),wtstatePresent.get(6),img.get(6)));
            adapter.addItem(new WeatherItem(timeStr.get(7),tempor.get(7),wtstatePresent.get(7),img.get(7)));
            adapter.addItem(new WeatherItem(timeStr.get(8),tempor.get(8),wtstatePresent.get(8),img.get(8)));
            adapter.addItem(new WeatherItem(timeStr.get(9),tempor.get(9),wtstatePresent.get(9),img.get(9)));
        }

        listView.setAdapter(adapter);

        //만약 처음 들어오는거면 튜토리얼 시청
        if(Customer.alarmevent[3]==false){
            tutorial.setImageResource(R.drawable.menu3_tut);
            tutorial.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tutorial.setImageBitmap(null);
                    Customer.alarmevent[3]=true;
                }
            });
        }

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
        if(wtstate==null||wtstate.size()==0) {
            //너무빨리들어가서 데이터로딩이 덜되서 온경우
            wtstate.addAll(mWeatherEvent.getWstate());
            tempor.addAll(mWeatherEvent.getTempor());
            time.addAll(mWeatherEvent.getTime());
            changeStringFormatter();

            adapter.addItem(new WeatherItem(timeStr.get(0),tempor.get(0),wtstatePresent.get(0),img.get(0)));
            adapter.addItem(new WeatherItem(timeStr.get(1),tempor.get(1),wtstatePresent.get(1),img.get(1)));
            adapter.addItem(new WeatherItem(timeStr.get(2),tempor.get(2),wtstatePresent.get(2),img.get(2)));
            adapter.addItem(new WeatherItem(timeStr.get(3),tempor.get(3),wtstatePresent.get(3),img.get(3)));
            adapter.addItem(new WeatherItem(timeStr.get(4),tempor.get(4),wtstatePresent.get(4),img.get(4)));
            adapter.addItem(new WeatherItem(timeStr.get(5),tempor.get(5),wtstatePresent.get(5),img.get(5)));
            adapter.addItem(new WeatherItem(timeStr.get(6),tempor.get(6),wtstatePresent.get(6),img.get(6)));
            adapter.addItem(new WeatherItem(timeStr.get(7),tempor.get(7),wtstatePresent.get(7),img.get(7)));
            adapter.addItem(new WeatherItem(timeStr.get(8),tempor.get(8),wtstatePresent.get(8),img.get(8)));
            adapter.addItem(new WeatherItem(timeStr.get(9),tempor.get(9),wtstatePresent.get(9),img.get(9)));

            Log.d("fewg","ㅈㄷㅈdilyfinsh");
            listView.setAdapter(adapter);
        }else{
            //기존의 데이터가 바뀐경우
            wtstate.addAll(mWeatherEvent.getWstate());
            tempor.addAll(mWeatherEvent.getTempor());
            time.addAll(mWeatherEvent.getTime());
            changeStringFormatter();
            Log.d("fewg","dilyfinsh");
            adapter.notifyDataSetChanged();
            listView.setAdapter(adapter);
        }
    }

    private void changeStringFormatter(){
        for(int i=0;i<wtstate.size();i++){
            Log.d("i size",String.valueOf(i));
            if(wtstate.get(i)=="sun"){
                //img[i]=R.drawable.sunny;

                img.add(R.drawable.sunnyday3);
            }
            else if(wtstate.get(i)=="fewcloud"){
                //img[i]=R.drawable.cloudy;
                img.add(R.drawable.fewcloud3);
            }
            else if(wtstate.get(i)=="manycloud"){
                //img[i]=R.drawable.cloudy2;
                img.add(R.drawable.manycloud3);
            }
            else if(wtstate.get(i)=="rain"){
                //img[i]=R.drawable.rainy;
                img.add(R.drawable.rainny3);
            }
            else if(wtstate.get(i)=="snow"){
                // img[i]=R.drawable.snowy;
                img.add(R.drawable.snowy2);
            }
            else{
                // img[i]=R.drawable.xkon;
            }
        }
        //시간 스트링으로변경
        int cnt=0;
        timeStr.add("현재 날씨");
        for(int i=1;i<time.size();i++){
            if(time.get(i)<10){
                if(cnt==0) {
                    timeStr.add("오늘 "+"0" + time.get(i)+"시");
                }else{
                    timeStr.add("내일 "+"0" + time.get(i)+"시");
                }
            }else{
                if(cnt==0) {
                    timeStr.add("오늘 "+ time.get(i)+"시");
                }else{
                    timeStr.add("내일 "+ time.get(i)+"시");
                }
            }
            if(time.get(i)==21)
                cnt++;
        }
        //날씨구문변경
        for(int i=0;i<wtstate.size();i++){
            if(MainActivity.wtstate.size()==0){

            }
            else if(wtstate.get(i).equals("manycloud")){
                //AlarmTitle="흐림";
                wtstatePresent.add("~구름이~\n뭉게뭉게");
            }
            else if(wtstate.get(i).equals("fewcloud")){
                //AlarmTitle="구름";
                wtstatePresent.add("~구름이~\n하늘하늘");
            }
            else if(wtstate.get(i).equals("sun")){
                //AlarmTitle="태양";
                wtstatePresent.add("날씨가\n화창해요!");
            }
            else if(wtstate.get(i).equals("rain")){
                //AlarmTitle="비";
                wtstatePresent.add("ㅠ비가ㅠ\n주륵주륵");
            }
            else if(wtstate.get(i).equals("snow")){
                //AlarmTitle="눈";
                wtstatePresent.add("눈이.와!");
            }
            else {
                wtstatePresent.add("저는 집에 갈 수 없어요ㅜㅜ");
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BusProvider.getInstance().unregister(this);
    }


}
