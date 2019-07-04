package samstnet.com.kaz;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WeatherItemView extends LinearLayout {
    TextView textView2;
    TextView textView;
    TextView textView3;
    ImageView imageView;
    public WeatherItemView(Context context) {
        super(context);

        init(context);
    }

    public WeatherItemView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }
    private void init(Context context){
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;
        inflater.inflate(R.layout.weather_item,this,true);
        textView=(TextView)findViewById(R.id.textView);
        textView2=(TextView)findViewById(R.id.textView2);
        textView3=(TextView)findViewById(R.id.textView3);
        imageView=(ImageView)findViewById(R.id.imageView);
    }
    public void setTime(String time){
        textView3.setText(time);
    }
    public void setTempor(String tempor){textView.setText(tempor);}
    public void setWtstate(String wtstate){
        textView2.setText(wtstate);
    }
    public void setImage(int resId){
        imageView.setImageResource(resId);
    }
}
