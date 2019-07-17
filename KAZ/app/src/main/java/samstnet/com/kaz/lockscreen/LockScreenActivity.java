package samstnet.com.kaz.lockscreen;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import samstnet.com.kaz.MainActivity;
import samstnet.com.kaz.R;


public class LockScreenActivity extends Activity {


    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);




        getWindow().addFlags(
                // 기본 잠금화면보다 우선출력

                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED

// 기본 잠금화면 해제시키기
                        | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                        | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);



        getWindow().setContentView(R.layout.lockscreen);


     /* findViewById(R.id.stopService).setOnClickListener(new View.OnClickListener() {


            @Override

            public void onClick(View v) {

            //    finish();

            }

        });



*/

    }









}
