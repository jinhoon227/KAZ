package samstnet.com.kaz;

import android.graphics.Color;
import android.icu.text.AlphabeticIndex;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import samstnet.com.kaz.weekweather.WeekWeatherInfo;

public class TheDaytimeWeather extends Fragment {
    ArrayList<WeekWeatherInfo> arr_wwif = new ArrayList<>();
    LineChart lineChart;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView=(ViewGroup)inflater.inflate(R.layout.thedaytimeweather,container,false);
        lineChart = (LineChart) rootView.findViewById(R.id.chart);

        if( ((MainActivity)getActivity()).getWeekWeatherInfo() != null){
            arr_wwif = (((MainActivity)getActivity()).getWeekWeatherInfo());
            makeChart();
        }

        return rootView;
    }

    private void makeChart(){

        //차트 설정
        lineChart.setTouchEnabled(false);
        lineChart.setBackgroundColor((Color.rgb(000,191,255)));
        lineChart.setDrawGridBackground(false);
        lineChart.setDescription("기상청");
        lineChart.setDescriptionTextSize(1f);


        ArrayList<Entry> entries = new ArrayList<>();
        for(int i=0;i<arr_wwif.size();i++){
            entries.add(new Entry(Float.parseFloat(arr_wwif.get(i).getmTmx()),i));
        }
        LineDataSet dataset = new LineDataSet(entries, "최고기온");

        ArrayList<Entry> entries2 = new ArrayList<>();
        for(int i=0;i<arr_wwif.size();i++){
            entries2.add(new Entry(Float.parseFloat(arr_wwif.get(i).getmTmn()),i));
        }
        LineDataSet dataset2 = new LineDataSet(entries, "최저기온");


        //줄 색깔 및 동그라미 색깔
        //dataset.setLineWidth(1.75f);
        dataset.setCircleRadius(5f);
        dataset.setColor(Color.WHITE);
        dataset.setCircleColor(Color.WHITE);
        dataset.setHighLightColor(Color.WHITE);

        //점 위에 값 크기 및 색깔
        dataset.setValueTextSize(12f);
        dataset.setValueTextColor(Color.BLUE);

        //선 크기
        dataset2.setLineWidth(3f);

        //줄 색깔 및 동그라미 색깔
        //dataset.setLineWidth(1.75f);
        dataset2.setCircleRadius(5f);
        dataset2.setColor(Color.WHITE);
        dataset2.setCircleColor(Color.WHITE);
        dataset2.setHighLightColor(Color.WHITE);

        //점 위에 값 크기 및 색깔
        dataset2.setValueTextSize(12f);
        dataset2.setValueTextColor(Color.BLUE);

        //선 크기
        dataset2.setLineWidth(3f);


        ArrayList<String> labels = new ArrayList<String>();
        for(int i=0;i<arr_wwif.size();i++){
            labels.add(arr_wwif.get(i).getDay());
        }

        LineData data = new LineData(labels, dataset, dataset2);
        //dataset.setColors(ColorTemplate.COLORFUL_COLORS); //
           /*dataset.setDrawCubic(true); //선 둥글게 만들기
            dataset.setDrawFilled(true); //그래프 밑부분 색칠*/

        lineChart.setData(data);

        // get the legend (only possible after setting data)
        Legend l = lineChart.getLegend();
        l.setEnabled(false);

        //아마 선없애기? 또는 마진추가
        lineChart.getAxisLeft().setEnabled(false);
        lineChart.getAxisLeft().setSpaceTop(40);
        lineChart.getAxisLeft().setSpaceBottom(40);
        lineChart.getAxisRight().setEnabled(false);

        //격자줄 없애기
        lineChart.getAxisRight().setDrawGridLines(false);
        // lineChart.getAxisRight().setDrawAxisLine(false);
        lineChart.getXAxis().setDrawAxisLine(false);
        lineChart.getXAxis().setDrawGridLines(false);

        //x축 줄 폰트 크기 및 색깔
        lineChart.getXAxis().setTextSize(15f);
        lineChart.getXAxis().setSpaceBetweenLabels(2);
        lineChart.getXAxis().setTextColor(Color.WHITE);


        lineChart.animateX(2500);
    }


    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d("what","hell");
    }
}
