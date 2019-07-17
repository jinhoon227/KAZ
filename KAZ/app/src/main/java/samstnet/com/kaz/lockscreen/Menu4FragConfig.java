package samstnet.com.kaz.lockscreen;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import samstnet.com.kaz.MainActivity;
import samstnet.com.kaz.R;
import samstnet.com.kaz.eventbus.BusProvider;

public class Menu4FragConfig extends Fragment {

    private Button onBtn, offBtn;
    TextView tx;
    MainActivity activity;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) getActivity();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BusProvider.getInstance().unregister(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;

    }
    @Override

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Log.d("yeaahna","yeah");
        //getActivity().setContentView(R.layout.fragment_menu4_frag_config);




    }
    @Nullable

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_menu4_frag_config, container, false);

        onBtn = (Button) rootView.findViewById(R.id.startService);

        offBtn = (Button) rootView.findViewById(R.id.stopService);


        onBtn.setOnClickListener(new OnClickListener() {

            @Override

            public void onClick(View v) {

                Intent intent = new Intent(getActivity().getApplication(), ScreenService.class);

                getActivity().startService(intent);
                tx=(TextView) getActivity().findViewById(R.id.textView4);
                tx.setText("0");

            }
        });
        offBtn.setOnClickListener(new OnClickListener() {

            @Override

            public void onClick(View v) {

                Intent intent = new Intent(getActivity().getApplication(),ScreenService.class);

                getActivity().stopService(intent);

            }

        });


        return rootView;
    }
}