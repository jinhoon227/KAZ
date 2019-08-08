
        package samstnet.com.kaz;

        import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.icu.text.SimpleDateFormat;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.w3c.dom.Document;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

import samstnet.com.kaz.Service.ExampleService;
import samstnet.com.kaz.alarm.mAlarm;
import samstnet.com.kaz.eventbus.BusProvider;
import samstnet.com.kaz.eventbus.Customer;
import samstnet.com.kaz.eventbus.Item_type;
import samstnet.com.kaz.eventbus.WeatherEvent;
import samstnet.com.kaz.eventbus.plant_info;
import samstnet.com.kaz.gps.ConverterGridGps;
import samstnet.com.kaz.gps.GpsInfo;
import samstnet.com.kaz.gps.LatXLngY;
import samstnet.com.kaz.lockscreen.Menu4FragConfig;
import samstnet.com.kaz.menu2_store.Menu2FragStore;
import samstnet.com.kaz.weekweather.WeekWeatherInfo;
import samstnet.com.kaz.weekweather.WeekWeatherParser;

import static samstnet.com.kaz.eventbus.Customer.ALARMEVENT_NUM;
import static samstnet.com.kaz.eventbus.Customer.ITEM_NUM;


@TargetApi(Build.VERSION_CODES.N)
public class MainActivity extends AppCompatActivity {

    // FrameLayout에 각 메뉴의 Fragment를 바꿔 줌
    private FragmentManager fragmentManager = getSupportFragmentManager();
    // 4개의 메뉴에 들어갈 Fragment들
    private Menu1FragGrowth menu1FragGrowth = new Menu1FragGrowth();
    private Menu2FragStore menu2FragStore = new Menu2FragStore();
    private Menu3FragWeather menu3FragWeather = new Menu3FragWeather();
    private Menu4FragConfig menu4FragConfig = new Menu4FragConfig();

    //for Gps
    private final int PERMISSIONS_ACCESS_FINE_LOCATION = 1000;
    private final int PERMISSIONS_ACCESS_COARSE_LOCATION = 1001;
    private boolean isAccessFineLocation = false;
    private boolean isAccessCoarseLocation = false;
    private boolean isPermission = false;
    public static boolean locklock=true;
    public static int TO_GRID = 0;
    private GpsInfo gps;
    private LatXLngY grid;
    private ConverterGridGps converterGridGps;
    static public int nowtime=0;
    Document doc = null;
    String data_info = null;
    Intent intent3;
    static WeatherEvent wev = null;

    static public ArrayList<String> wtstate = new ArrayList<>();
    static public ArrayList<String> tempor = new ArrayList<>();
    ArrayList<Integer> time = new ArrayList<>();

    //주간 날씨 저장
    ArrayList<WeekWeatherInfo> arr_wwif = null;
    WeekWeatherParser wwp;

    static public String cityInfo;

   //store 프래그먼트
    //store part-1
   // Shop_fragment fragmentshop = new Shop_fragment();
    FragmentTransaction transaction = fragmentManager.beginTransaction();


    //네트워크 연결 상태 확인
    ConnectivityManager connectivityManager;

    //알람 서비스
    static public Intent intent;
    Customer cus;
    //public Intent intent1;
    public int count=0;
    Intent intent2;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String itemtmp[] = new String [50];
        String tmparr[];
        String tmparr2;

        if(savedInstanceState!=null)
        {
            if(savedInstanceState!=null)
            {
                Log.d("강제 종료시 복원 데이터","saveinstance1");
                Bundle bundle = savedInstanceState.getParcelable("saveBundle");
                if(bundle!=null)
                {
                    Log.d("강제 종료시 복원 데이터","bundle");
                    for(int i=0;i<ITEM_NUM;i++) {
                        itemtmp[i]=bundle.getString("item"+i,null);
                        Log.d("강제 종료시 복원 데이터"+i,itemtmp[i]);
                        tmparr = itemtmp[i].split("&");
                        Customer.item[i] = new Item_type(tmparr[0], tmparr[1], Integer.parseInt(tmparr[2]), Integer.parseInt(tmparr[3]), Boolean.valueOf(tmparr[4]), Boolean.valueOf(tmparr[5]));
                        bundle.putInt("money",cus.getMoney());
                    }
                    tmparr2 =  cus.setting1.isCreateevent()+"&"+cus.setting1.isSwitch1event()+"&"+cus.setting1.isSoundevent()+"&"+cus.setting1.isScreen();
                    bundle.putString("setting",tmparr2);
                    tmparr2 = cus.plant1.getLevel()+"&"+cus.plant1.getExp()+"&"+cus.plant1.getName()+"&"+cus.plant1.getState()+"&"+ plant_info.getItemNum()+"&"+cus.plant1.getLove();
                    bundle.putString("plant",tmparr2);
                    for(int i=0;i<ALARMEVENT_NUM-1;i++)
                    {
                        tmparr2= tmparr2+String.valueOf(cus.alarmevent[i])+"&";
                    }
                    tmparr2=tmparr2+String.valueOf(cus.alarmevent[ALARMEVENT_NUM-1]);
                    bundle.putString("alarmevent",tmparr2);

                }

            }
        }

        //투명한 상태바
        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        //    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);

        NetworkInfo mNetworkState=getNetworkInfo();

        if(mNetworkState!=null&&mNetworkState.isConnected()){
            if(mNetworkState.getType()==ConnectivityManager.TYPE_WIFI){
                Log.d("Network","WIFI");
            }else if(mNetworkState.getType()==ConnectivityManager.TYPE_MOBILE){
                Log.d("Network","3G/LTE");
            }
            /*if(LockScreenActivity.islock==false){
            Intent intent = new Intent(getApplication(), ScreenService.class);
            startService(intent);}*/
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);


        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // 프래그먼트 초기 추가
        // 하지만 해당 프래그먼트 이동시 초기 데이터를 가져오기로 해놨기 때문에 필요없어보임
        // 프래그먼트 초기 추가
        //transaction.attach(menu1FragGrowth);
        //transaction.attach(menu2FragStore);
        //transaction.attach(menu3FragWeather);
        //transaction.attach(menu4FragConfig);
        // 첫 화면 추가
        transaction.replace(R.id.frame_layout, menu1FragGrowth).commitAllowingStateLoss();

        // bottomNavigationView의 아이템이 선택될 때 호출될 리스너 등록
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                switch (item.getItemId()) {
                    case R.id.navigation_menu1: {
                        transaction.replace(R.id.frame_layout, menu1FragGrowth).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.navigation_menu2: {
                        transaction.replace(R.id.frame_layout, menu2FragStore).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.navigation_menu3: {
                        transaction.replace(R.id.frame_layout, menu3FragWeather).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.navigation_menu4: {
                        transaction.replace(R.id.frame_layout, menu4FragConfig).commitAllowingStateLoss();
                        break;
                    }
                }

                return true;
            }
        });
        //08.04 하단 이동탭버튼을 함수를 이용하여 고정
        //이전에는 desing libary 가 28버전 이상이여야지
        //app:labelVisibilityMode="selected" 를 지원해주었으나
        //28버전에서는 NoClassDefFoundError 가 발생함
        //해당 오류가 떠도 앱실행에는 아무문제없으나 그냥 꺼림칙해서 바꿈
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

        // 권한 요청을 해야 함
        if (!isPermission) {
            callPermission();
            if(isPermission) {
                UsingGps();
            }
        }
        else {
            UsingGps();
        }
        }else{
            Log.d("Network","Not connected");
            new AlertDialog.Builder(this).setMessage("인터넷과 연결되어 있지 않습니다.")
                    .setCancelable(false).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finishAffinity();
                }
            }).show();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            lovestatetime();
            Log.e("api ","build");
        }
        intent = new Intent(getApplicationContext(), mAlarm.class); // 이동할 컴포넌트
        intent2=new Intent(getApplicationContext(), ExampleService.class);
        sendBroadcast(intent2);
        if(cus.setting1.isCreateevent()){
            intent3=new Intent(getApplicationContext(), Lovestatetime.class);
            Log.d( "스크린 꺼졌을때 애정도 받아오는거", String.valueOf(cus.plant1.getLove()));
            startService(intent3);
            Log.d("이거 실행","한다");
        }
    }
    //애정도 나타내는 함수(시간에 따라)
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void lovestatetime(){
        //현재시간 변수들
        long now1 = System.currentTimeMillis();
        Date date = new Date(now1);
        SimpleDateFormat sdf1 = new SimpleDateFormat("HH");//1시간단위
        String getTime = sdf1.format(date);
        int timing1=cus.getStateTime();//로컬시간 가져오기
        nowtime=Integer.valueOf(getTime);//현재시간 가져오기
        int timing=timing1-nowtime;//로컬시간-현재시간
        count=10;
        Log.e(String.valueOf(cus.getStateTime()),"저장된시간");
        if(Math.abs(timing)<1||cus.getStateTime()==0){
            count=0;
        }
        if(Math.abs(timing)>=1&&Math.abs(timing)<3&&cus.getStateTime()!=0){
            count=10;
        }
        else if(Math.abs(timing)>=3&&Math.abs(timing)<5&&cus.getStateTime()!=0){
            count=20;
        }
        else if(Math.abs(timing)>=5&&Math.abs(timing)<7&&cus.getStateTime()!=0){
            count=30;
        }
        else if(Math.abs(timing)>=7&&Math.abs(timing)<9&&cus.getStateTime()!=0){
            count=50;
        }
        else if(Math.abs(timing)>=9&&cus.getStateTime()!=0){
            count=70;
        }
        else if(cus.getStateTime()==0)
        {
            count=0;
        }
        Log.e("time1",String.valueOf(timing));
        Log.e("count",String.valueOf(count));
        //애정도가 마이너스로 변하면 최소값0으로 모두 통일
        if(cus.plant1.getLove()-count<0)
        {
            cus.plant1.setLove(0);
            Log.e("애정도",String.valueOf(cus.plant1.getLove()));
            Log.e("count",String.valueOf(count));
        }
        //애정도가 마이너스가 아니면 그값 저장
        else{
            cus.plant1.setLove(cus.plant1.getLove() - count);
            Log.e("애정도",String.valueOf(cus.plant1.getLove()));
            Log.e("count",String.valueOf(count));
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_ACCESS_FINE_LOCATION
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            isAccessFineLocation = true;

        } else if (requestCode == PERMISSIONS_ACCESS_COARSE_LOCATION
                && grantResults[0] == PackageManager.PERMISSION_GRANTED){

            isAccessCoarseLocation = true;
        }

        if (isAccessFineLocation && isAccessCoarseLocation) {
            isPermission = true;
        }

        //권한을 수락했다면 위치정보 가져오기
        if(isAccessFineLocation){
            UsingGps();
        }
    }

    // 위치 권한 요청
    private void callPermission() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_ACCESS_FINE_LOCATION);

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){

            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSIONS_ACCESS_COARSE_LOCATION);
        } else {
            isPermission = true;
        }
    }

    double latitude;
    double longitude;
    public void UsingGps(){
        //GetXMLTask task = new GetXMLTask();
        PassingWeather passingWeather = new PassingWeather();
        gps = new GpsInfo(MainActivity.this);
        // GPS 사용유무 가져오기
        if (gps.isGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            Log.d("latitude",Double.toString(latitude));
            Log.d("latitudeddd",Double.toString(longitude));
            converterGridGps = new ConverterGridGps();
            grid = converterGridGps.getGridValue(TO_GRID, latitude, longitude);
            Log.d("ddd","당신의 위치 - " + grid.x + "    " + grid.y);

            //task.execute("http://www.kma.go.kr/wid/queryDFS.jsp?gridx=" + grid.x + "&gridy=" + grid.y);

            //자신이 조회를 원하는 지역의 경도와 위도를 입력해주세요
            String nx = grid.x+""; //경도
            String ny = grid.y+""; //위도

            DayTimeFormatter daytimeformatter=new DayTimeFormatter();

            String baseDate = daytimeformatter.getBaseTime(); // 자신이 조회하고싶은 날짜를 입력해주세요
            String baseTime = daytimeformatter.getNowTime(); //자신이 조회하고싶은 시간대를 입력해주세요

            Log.d("dsfa",baseTime);
            // 서비스 인증키입니다. 공공데이터포털에서 제공해준 인증키를 넣어주시면 됩니다.
            String serviceKey = "eT6LqyOyPldZj9CPLxXJVJFB75l9YoEkT%2FM5ujddCbGvXr2Ehb%2BiVHZfoLg4TPEF%2BGdjNGfjoN%2B4ax26AW0xwQ%3D%3D";
            // 정보를 모아서 URL정보를 만들면됩니다.
            // 맨 마지막 "&_type=json"에 따라 반환 데이터의 형태가 정해집니다.
            String urlStr = "http://newsky2.kma.go.kr/service/SecndSrtpdFrcstInfoService2/ForecastSpaceData?" + "serviceKey=" + serviceKey + "&base_date=" + baseDate + "&base_time=" + baseTime + "&nx="+ nx + "&ny=" + ny + "&numOfRows=100"+"&_type=json";
            passingWeather.execute(urlStr);
        } else {
            // GPS 를 사용할수 없으므로
            gps.showSettingsAlert();
        }


        
    }

    private class PassingWeather extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            //주간날씨
            wwp = new WeekWeatherParser(latitude, longitude);
            cityInfo= wwp.getCity();
            wwp.StartParsing();

            //일간날시
            URL url;
            try {
                url = new URL(urls[0]);
                BufferedReader bf;
                String line = "";
                String result=""; //날씨 정보를 받아옵니다.
                bf = new BufferedReader(new InputStreamReader(url.openStream()));

                //버퍼에 있는 정보를 하나의 문자열로 변환.
                while((line=bf.readLine())!=null){
                    result=result.concat(line);
                    data_info=result;
                }


            } catch(Exception e){
                System.out.println(e.getMessage());
            }
            return data_info;
        }

        protected void onPostExecute(String doc) {
            super.onPostExecute(doc);
            //이 부분에서 날씨 이미지를 출력해줌
            //주간
            arr_wwif = wwp.GetArr_wwif();
            //일간
            tempor.clear();
            wtstate.clear();
            time.clear();

            try {
                //날씨 데이터를 추출
                // Json parser를 만들어 만들어진 문자열 데이터를 객체화 합니다.
                JSONParser parser = new JSONParser();
                JSONObject obj = (JSONObject) parser.parse(doc); // Top레벨 단계인 response 키를 가지고 데이터를 파싱합니다.
                JSONObject parse_response = (JSONObject) obj.get("response"); // response 로 부터 body 찾아옵니다.
                JSONObject parse_body = (JSONObject) parse_response.get("body"); // body 로 부터 items 받아옵니다.
                JSONObject parse_items = (JSONObject) parse_body.get("items"); // items로 부터 itemlist 를 받아오기 itemlist : 뒤에 [ 로 시작하므로 jsonarray이다
                JSONArray parse_item = (JSONArray) parse_items.get("item");
                String category="";
                String fcstTime="";
                String tmpfcst="";
                int weatherValue;
                boolean nonRain=true;
                JSONObject weather; // parse_item은 배열형태이기 때문에 하나씩 데이터를 하나씩 가져올때 사용합니다.

                // 필요한 데이터만 가져오려고합니다.
                for(int i = 0 ; i < parse_item.size(); i++)
                {
                    weather = (JSONObject) parse_item.get(i);
                    fcstTime = weather.get("fcstTime").toString();

                    if(tmpfcst.equals(fcstTime)) {
                        category = (String) weather.get("category");
                        if(category.equals("PTY")){
                            weatherValue=Integer.parseInt(weather.get("fcstValue").toString());
                            //없음(0), 비(1), 비/눈(2), 눈(3), 소나기(4)
                            if(weatherValue==1 || weatherValue==2 || weatherValue==4) {
                                wtstate.add("rain");
                                nonRain=false;
                            }
                            else if(weatherValue==3) {
                                wtstate.add("snow");
                                nonRain=false;
                            }
                        }else if(category.equals("SKY")&&nonRain){
                            weatherValue=Integer.parseInt(weather.get("fcstValue").toString());
                            //맑음(1), 구름많음(3), 흐림(4)
                            if(weatherValue==1)
                                wtstate.add("sun"); //맑음
                            else if(weatherValue==3)
                                wtstate.add("fewcloud"); //구름적음 , 기존버전 호환을위해 구름많음이 구름적음, 흐림이 구름많음으로
                            else if(weatherValue==4)
                                wtstate.add("manycloud");  //구름많음
                        }else if(category.equals("T3H")){
                            tempor.add(weather.get("fcstValue").toString());
                        }
                        // 출력합니다.
                        //Log.d("f","배열의 "+i+"번째 요소");
                        //Log.d("f"," category : "+ category);

                    }else{
                        tmpfcst=fcstTime;
                        String tmpTime = fcstTime.substring(0,2);
                        time.add(Integer.parseInt(tmpTime));
                        nonRain=true;
                        category = (String) weather.get("category");
                        if(category.equals("PTY")){
                            weatherValue=Integer.parseInt(weather.get("fcstValue").toString());
                            //없음(0), 비(1), 비/눈(2), 눈(3), 소나기(4)
                            if(weatherValue==1 || weatherValue==2 || weatherValue==4) {
                                wtstate.add("비");
                                nonRain=false;
                            }
                            else if(weatherValue==3) {
                                wtstate.add("눈");
                                nonRain=false;
                            }
                        }else if(category.equals("SKY")&&nonRain){
                            weatherValue=Integer.parseInt(weather.get("fcstValue").toString());
                            //맑음(1), 구름많음(3), 흐림(4)
                            if(weatherValue==1)
                                wtstate.add("맑음");
                            else if(weatherValue==3)
                                wtstate.add("구름많음");
                            else if(weatherValue==4)
                                wtstate.add("흐림");
                        }else if(category.equals("T3H")){
                            tempor.add(weather.get("fcstValue").toString());
                        }
                    }

                }
            }catch(Exception e){
                e.printStackTrace();
            }
            Log.d("End","eee");
            for(String nm : wtstate)
                Log.d("wtste: |",nm);
            for(String nm : tempor)
                Log.d("tempor: |",nm);
            for(int nm : time)
                Log.d("time: |",Integer.toString(nm));
            wev = new WeatherEvent(time,wtstate,tempor);
            BusProvider.getInstance().post(wev);
            BusProvider.getInstance().post(arr_wwif);
        }
    }



    public static WeatherEvent getWeatherInfo(){
        return wev;
    }
    public ArrayList<WeekWeatherInfo> getWeekWeatherInfo(){
        return arr_wwif;
    }

    @Override
    protected void onStop() {
        super.onStop();
        String items[];
        Customer cus = (Customer)getApplication();
        SharedPreferences pref= getSharedPreferences("pref", MODE_PRIVATE); // 선언
        SharedPreferences.Editor editor = pref.edit();// editor에 put 하기
        String tmparr2="";
        String tmparr3="";
        for(int i=0;i<ITEM_NUM;i++) {
            tmparr2= Customer.item[i].getName()+ "&" + Customer.item[i].getMobile() + "&" +Customer.item[i].getResId() + "&" +Customer.item[i].getPrice()+ "&" + Customer.item[i].isWear() + "&" + Customer.item[i].isBuy();

            Log.d("끝날때 여기 저장됨",tmparr2);
            editor.putString("item"+i , tmparr2); //item1라는 key값으로 item 데이터를 저장한다.
            editor.putInt("money",cus.getMoney());//money 라는 key값으로 고객 돈 데이터 저장
            tmparr3 = tmparr3+Boolean.valueOf(cus.plant1.items[i])+"&";
        }
        editor.putString("itemwear",tmparr3);
        tmparr2 = cus.plant1.getLevel()+"&"+cus.plant1.getExp()+"&"+cus.plant1.getName()+"&"+cus.plant1.getState()+"&"+ plant_info.getItemNum()+"&"+cus.plant1.getLove();
        editor.putString("plant",tmparr2);
        tmparr2 =  cus.setting1.isCreateevent()+"&"+cus.setting1.isSwitch1event()+"&"+cus.setting1.isSoundevent()+"&"+cus.setting1.isScreen();
        editor.putString("setting",tmparr2);
        tmparr2="";
        for(int i=0;i<ALARMEVENT_NUM-1;i++)
        {
            tmparr2= tmparr2+String.valueOf(cus.alarmevent[i])+"&";
        }
        tmparr2=tmparr2+String.valueOf(cus.alarmevent[ALARMEVENT_NUM-1]);
        Log.d("알람 이벤트", ""+tmparr2);
        editor.putString("alarmevent",tmparr2);
        editor.putString("stateTime",String.valueOf(cus.getStateTime()));
        editor.commit(); //완료한다.
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Customer cus = (Customer)getApplication();
        //2019-07-17 강제 종료시 저장한 데이터 가져오기
        String itemtmp[] = new String [50];
        String tmparr[];
        String tmparr2;

        Log.d("강제 종료시 복원 데이터","복원데이터");
        if(savedInstanceState!=null)
        {
            Log.d("강제 종료시 복원 데이터","saveinstance1");
            Bundle bundle = savedInstanceState.getParcelable("saveBundle");
            if(bundle!=null)
            {
                Log.d("강제 종료시 복원 데이터","bundle");
                for(int i=0;i<ITEM_NUM;i++) {
                    itemtmp[i]=bundle.getString("item"+i,null);
                    Log.d("강제 종료시 복원 데이터"+i,itemtmp[i]);
                    tmparr = itemtmp[i].split("&");
                    Customer.item[i] = new Item_type(tmparr[0], tmparr[1], Integer.parseInt(tmparr[2]), Integer.parseInt(tmparr[3]), Boolean.valueOf(tmparr[4]), Boolean.valueOf(tmparr[5]));
                    bundle.putInt("money",cus.getMoney());
                }
                tmparr2 =  cus.setting1.isCreateevent()+"&"+cus.setting1.isSwitch1event()+"&"+cus.setting1.isSoundevent()+"&"+cus.setting1.isScreen();
                bundle.putString("setting",tmparr2);
                tmparr2 = cus.plant1.getLevel()+"&"+cus.plant1.getExp()+"&"+cus.plant1.getName()+"&"+cus.plant1.getState()+"&"+ plant_info.getItemNum()+"&"+cus.plant1.getLove();
                bundle.putString("plant",tmparr2);
                for(int i=0;i<ALARMEVENT_NUM-1;i++)
                {
                    tmparr2= tmparr2+String.valueOf(cus.alarmevent[i])+"&";
                }
                tmparr2=tmparr2+String.valueOf(cus.alarmevent[ALARMEVENT_NUM-1]);
                bundle.putString("alarmevent",tmparr2);
                bundle.putString("stateTime",String.valueOf(cus.getStateTime()));

            }

        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Bundle bundle = new Bundle();
        String tmparr2;

        for (int i = 0; i < 4; i++) {
            tmparr2 = Customer.item[i].getName() + "&" + Customer.item[i].getMobile() + "&" + Customer.item[i].getResId() + "&" + Customer.item[i].getPrice() + "&" + Customer.item[i].isWear() + "&" + Customer.item[i].isBuy();
            Log.d("강제로 끝날때 여기 저장됨", tmparr2);
            bundle.putString("item" + i, tmparr2); //item1라는 key값으로 id 데이터를 저장한다.
        }
        outState.putParcelable("saveBundle", bundle);
    }


    //접속시 네트워크 상태 확인
    private NetworkInfo getNetworkInfo(){
        ConnectivityManager connectivityManager=(ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        return networkInfo;
    }


}
