package samstnet.com.kaz.menu1_growth_inventory;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import samstnet.com.kaz.eventbus.plant_info;
import java.util.ArrayList;

import samstnet.com.kaz.MainActivity;
import samstnet.com.kaz.R;
import samstnet.com.kaz.eventbus.Item_type;

public class growth_Fragment extends Fragment {
    MainActivity activity;
    Button button;
    TextView textView,textView2;
    EditText editText;
    TextView resultTextView;
    ImageView imageView=null;
    String level_string, exp_string;
    plant_info plant1=new plant_info(1,0,"PLANT1",1);
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
            imageView.setImageResource(R.drawable.bean2);
        }
        else if(a.getLevel()==10){
            a.setState(a.getState()+1);
            imageView.setImageResource(R.drawable.bean3);
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

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_menu1_frag_growth_main, container, false);
        //인덱스 참고 함수
        button=(Button) rootView.findViewById(R.id.button4);
        textView=(TextView) rootView.findViewById(R.id.LevelResult);
        textView2=(TextView) rootView.findViewById(R.id.ExpResult);
        imageView=(ImageView)rootView.findViewById(R.id.plant1);
        textView.setText(level_string);
        textView2.setText(exp_string);
        if(plant1.getState()==1)imageView.setImageResource(R.drawable.bean1);
        else if(plant1.getState()==2)imageView.setImageResource(R.drawable.bean2);
        else if(plant1.getState()==3)imageView.setImageResource(R.drawable.bean3);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExpUp(plant1);

                exp_string=plant1.getExp()+"";
                level_string=plant1.getLevel()+"";

                textView.setText(level_string);
                textView2.setText(exp_string);
                //textView.setText("1");

            }});






        return rootView;
    }
}
