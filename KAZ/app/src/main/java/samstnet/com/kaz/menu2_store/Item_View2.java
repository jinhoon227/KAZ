package samstnet.com.kaz.menu2_store;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import samstnet.com.kaz.R;

public class Item_View2 extends LinearLayout {
    TextView textView2,textView,textView3;
    ImageView imageView;
    LinearLayout LL;
    public Item_View2(Context context) {
        super(context);
        init(context);
    }

    public Item_View2(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    public void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.list_item,this ,true);
        LL = (LinearLayout)findViewById(R.id.layout_listitem);
        textView = (TextView) findViewById(R.id.textView);
        textView2 = (TextView) findViewById(R.id.textView2);
        textView3 = (TextView) findViewById(R.id.textView_buy);
        imageView= (ImageView) findViewById(R.id.imageView);

    }
    @SuppressLint("ResourceAsColor")
    public void background_change(){
        LL.setBackgroundResource(R.drawable.shop_item_ui2);
    }
    public void setName(String name)
    {
        textView.setText(name);
    }
    public void setMobile(String mobile)
    {
        textView2.setText(mobile);
    }
    public void setwear(String wear){textView3.setText(wear);}
    public void setImage(int resId)
    {
        imageView.setImageResource(resId);
    }


}
