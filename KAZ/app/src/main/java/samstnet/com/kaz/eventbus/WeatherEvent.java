package samstnet.com.kaz.eventbus;

import android.util.Log;

import java.util.ArrayList;

//버스로 전달될 객체
//이거슨 날씨정보
        public class WeatherEvent {
        private ArrayList<Integer> time = new ArrayList<>(); //해당 시각
        private ArrayList<String> wstate = new ArrayList<>(); //3시간 단위 일일 날씨 정보 저장
        private ArrayList<String> tempor = new ArrayList<>(); //온도 저장

            public WeatherEvent(WeatherEvent wev){
            this.time.addAll((wev.getTime()));
            this.wstate.addAll((wev.getWstate()));
            this.tempor.addAll(wev.getTempor());
        }

    public WeatherEvent(ArrayList<Integer> time, ArrayList<String> wstate, ArrayList<String> tempor) {
            this.time.addAll(time);
            this.wstate.addAll(wstate);
            this.tempor.addAll(tempor);
    }

    public ArrayList<Integer> getTime() {
        return time;
    }

    public ArrayList<String> getWstate() {
        return wstate;
    }

    public ArrayList<String> getTempor() {
        return tempor;
    }

}
