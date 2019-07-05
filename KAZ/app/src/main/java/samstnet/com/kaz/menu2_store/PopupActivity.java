package samstnet.com.kaz.menu2_store;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import samstnet.com.kaz.R;

        public class PopupActivity extends Activity {

            TextView txtText;
            //Button event
            Button button_cancel;
            Button button_confirm;
            String bus_data;
            //데이터 전달하기
            Intent intent = new Intent();


            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.fragment_menu2_popuplayout);

        //UI 객체생성
        txtText = (TextView)findViewById(R.id.txtText);
        //데이터 가져오기
        intent = getIntent();
        String data = intent.getStringExtra("data");
        txtText.setText(data);
                button_cancel=(Button)findViewById(R.id.button_cancel);
                button_confirm=(Button)findViewById(R.id.button_confirm);
                View.OnClickListener listener = new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        switch (v.getId())
                        {
                            case R.id.button_cancel:
                                bus_data="cancel";
                                intent.putExtra("result", bus_data);
                                setResult(RESULT_OK, intent);
                                Log.d("취소","취소");
                                finish();
                                break;

                            case R.id.button_confirm:
                                bus_data="confirm";
                                intent.putExtra("result", bus_data);
                                setResult(RESULT_OK, intent);
                                Log.d("확인","확인");
                                finish();
                                break;

                        }
                    }
                };
                button_confirm.setOnClickListener(listener);
                button_cancel.setOnClickListener(listener);
                //액티비티(팝업) 닫기
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
                return false;
            }
        return true;
        }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }
}