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

public class Menu1FragGrowth extends Fragment {




    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_menu1_frag_growth, container, false);
        Button button1 = (Button) rootView.findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("메인","화면");
                ((MainActivity)getActivity()).onFragmentChange(0);
            }
        });
        Button button2 = (Button) rootView.findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("인벤","토리");
                ((MainActivity)getActivity()).onFragmentChange(1);

            }
        });

        return rootView;

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        ((MainActivity)getActivity()).onFragmentChange(0);
        super.onCreate(savedInstanceState);
    }
}