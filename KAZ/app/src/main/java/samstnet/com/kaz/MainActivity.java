
        package samstnet.com.kaz;

        import android.Manifest;
        import android.annotation.TargetApi;
        import android.content.DialogInterface;
        import android.content.SharedPreferences;
        import android.content.pm.PackageManager;
        import android.net.ConnectivityManager;
        import android.net.NetworkInfo;
        import android.os.AsyncTask;
        import android.os.Build;
        import android.support.annotation.NonNull;
        import android.support.design.widget.BottomNavigationView;
        import android.support.v4.app.FragmentManager;
        import android.support.v4.app.FragmentTransaction;
        import android.support.v7.app.AlertDialog;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.MenuItem;

        import org.w3c.dom.Document;
        import org.w3c.dom.Element;
        import org.w3c.dom.Node;
        import org.w3c.dom.NodeList;
        import org.xml.sax.InputSource;
        import org.xml.sax.SAXException;

        import java.io.IOException;
        import java.net.MalformedURLException;
        import java.net.URL;
        import java.util.ArrayList;

        import javax.xml.parsers.DocumentBuilder;
        import javax.xml.parsers.DocumentBuilderFactory;
        import javax.xml.parsers.ParserConfigurationException;

        import samstnet.com.kaz.eventbus.BusProvider;
        import samstnet.com.kaz.eventbus.Customer;
        import samstnet.com.kaz.eventbus.Item_type;
        import samstnet.com.kaz.eventbus.WeatherEvent;
        import samstnet.com.kaz.gps.ConverterGridGps;
        import samstnet.com.kaz.gps.GpsInfo;
        import samstnet.com.kaz.gps.LatXLngY;

        import samstnet.com.kaz.lockscreen.Menu4FragConfig;
        import samstnet.com.kaz.weekweather.WeekWeatherInfo;
        import samstnet.com.kaz.weekweather.WeekWeatherParser;

        import samstnet.com.kaz.menu1_growth_inventory.growth_Fragment;
        import samstnet.com.kaz.menu2_store.Menu2FragStore;
        import samstnet.com.kaz.menu2_store.Shop_fragment;


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

    public static int TO_GRID = 0;
    private GpsInfo gps;
    private LatXLngY grid;
    private ConverterGridGps converterGridGps;

    Document doc = null;

    WeatherEvent wev = null;
    ArrayList<String> wtstate = new ArrayList<>();
    ArrayList<String> tempor = new ArrayList<>();
    ArrayList<Integer> time = new ArrayList<>();

    //주간 날씨 저장
    ArrayList<WeekWeatherInfo> arr_wwif = null;
    WeekWeatherParser wwp;


    //growth 프래그먼트 인벤토리 , 메인 화면
    growth_Fragment fragmentgrowth = new growth_Fragment();
    //store 프래그먼트
    //store part-1
    Shop_fragment fragmentshop = new Shop_fragment();
    FragmentTransaction transaction = fragmentManager.beginTransaction();


    //네트워크 연결 상태 확인
    ConnectivityManager connectivityManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        NetworkInfo mNetworkState=getNetworkInfo();

        if(mNetworkState!=null&&mNetworkState.isConnected()){
            if(mNetworkState.getType()==ConnectivityManager.TYPE_WIFI){
                Log.d("Network","WIFI");
            }else if(mNetworkState.getType()==ConnectivityManager.TYPE_MOBILE){
                Log.d("Network","3G/LTE");
            }

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
    private void UsingGps(){
        GetXMLTask task = new GetXMLTask();
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

            task.execute("http://www.kma.go.kr/wid/queryDFS.jsp?gridx=" + grid.x + "&gridy=" + grid.y);

        } else {
            // GPS 를 사용할수 없으므로
            gps.showSettingsAlert();
        }
    }


    private class GetXMLTask extends AsyncTask<String, Void, Document> {
        @Override
        protected Document doInBackground(String... urls) {
            //주간날씨

            wwp = new WeekWeatherParser(latitude, longitude);
            wwp.StartParsing();

            //일간날시
            URL url;
            try {
                url = new URL(urls[0]);
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder(); //XML문서 빌더 객체를 생성
                doc = db.parse(new InputSource(url.openStream())); //XML문서를 파싱한다.
                doc.getDocumentElement().normalize();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            }
            return doc;
        }

        protected void onPostExecute(Document doc) {
            super.onPostExecute(doc);//이 부분에서 날씨 이미지를 출력해줌
            //주간
            arr_wwif = wwp.GetArr_wwif();
            //일간
            String s = "";

            int nowTime = -100;

            //data 태그가 있는 노드를 찾아서 리스트 형태로 만들어서 반환
            NodeList nodeList = doc.getElementsByTagName("data");
            //data 태그를 가지는 노드를 찾음, 계층적인 노드 구조를 반환


            for (int i = 0; i < 15; i++) {////////////////////////////////for문 시작
                //날씨 데이터를 추출
                s = "";
                Node node = nodeList.item(i); //data엘리먼트 노드
                Element fstElmnt = (Element) node;

                NodeList timeList = fstElmnt.getElementsByTagName("hour");          //시간 timeList
                s += timeList.item(0).getChildNodes().item(0).getNodeValue() + "시 ";
                nowTime = Integer.parseInt(timeList.item(0).getChildNodes().item(0).getNodeValue());
                NodeList nameList = fstElmnt.getElementsByTagName("temp");          //이름
                Element nameElement = (Element) nameList.item(0);
                nameList = nameElement.getChildNodes();

                s += ((Node) nameList.item(0)).getNodeValue() + "°C\n\n";

                NodeList websiteList = fstElmnt.getElementsByTagName("wfKor");
                // s += websiteList.item(0).getChildNodes().item(0).getNodeValue() + "\n";
                time.add(nowTime);
                tempor.add(((Node) nameList.item(0)).getNodeValue() + "°C");
                if(websiteList.item(0).getChildNodes().item(0).getNodeValue().equals("구름 많음") || websiteList.item(0).getChildNodes().item(0).getNodeValue().equals("흐림"))
                {//구름 많을때
                    wtstate.add("manycloud");
                }
                else if(websiteList.item(0).getChildNodes().item(0).getNodeValue().equals("구름 조금"))
                {
                    wtstate.add("fewcloud");
                }
                else if(websiteList.item(0).getChildNodes().item(0).getNodeValue().equals("맑음"))
                {
                    wtstate.add("sun");
                }
                else if(websiteList.item(0).getChildNodes().item(0).getNodeValue().equals("비"))
                { //비
                    wtstate.add("rain");
                }
                else if(websiteList.item(0).getChildNodes().item(0).getNodeValue().equals("눈") || websiteList.item(0).getChildNodes().item(0).getNodeValue().equals("눈/비"))
                {//눈 올 때
                    wtstate.add("snow");
                }

                //Log.d("weather : ",wtstate.get(i));
                //Log.d("tempor : ",tempor.get(i));
                //Log.d("time : ",Integer.toString(time.get(i)));
            }
            wev = new WeatherEvent(time,wtstate,tempor);
            BusProvider.getInstance().post(wev);
        }
    }



    public WeatherEvent getWeatherInfo(){
        return wev;
    }
    public ArrayList<WeekWeatherInfo> getWeekWeatherInfo(){
        return arr_wwif;
    }

    @Override
    protected void onStop() {
        super.onStop();
        Customer cus = (Customer)getApplication();
        SharedPreferences pref= getSharedPreferences("pref", MODE_PRIVATE); // 선언
        SharedPreferences.Editor editor = pref.edit();// editor에 put 하기
        String tmparr2;
        for(int i=0;i<4;i++) {
            tmparr2= Customer.item[i].getName()+ "&" + Customer.item[i].getMobile() + "&" +Customer.item[i].getResId() + "&" +Customer.item[i].getPrice()+ "&" + Customer.item[i].isWear() + "&" + Customer.item[i].isBuy();

            Log.d("끝날때 여기 저장됨",tmparr2);
            editor.putString("item"+i , tmparr2); //item1라는 key값으로 item 데이터를 저장한다.
            editor.putInt("money",cus.getMoney());//money 라는 key값으로 고객 돈 데이터 저장

        }
        tmparr2 =  cus.setting1.isCreateevent()+"&"+cus.setting1.isSwitch1event()+"&"+cus.setting1.isSoundevent()+"&"+cus.setting1.isScreen();

        editor.putString("setting",tmparr2);
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
                for(int i=0;i<4;i++) {
                    itemtmp[i]=bundle.getString("item"+i,null);
                    Log.d("강제 종료시 복원 데이터"+i,itemtmp[i]);
                    tmparr = itemtmp[i].split("&");
                    Customer.item[i] = new Item_type(tmparr[0], tmparr[1], Integer.parseInt(tmparr[2]), Integer.parseInt(tmparr[3]), Boolean.valueOf(tmparr[4]), Boolean.valueOf(tmparr[5]));
                    bundle.putInt("money",cus.getMoney());
                }
                tmparr2 =  cus.setting1.isCreateevent()+"&"+cus.setting1.isSwitch1event()+"&"+cus.setting1.isSoundevent()+"&"+cus.setting1.isScreen();
                bundle.putString("setting",tmparr2);


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
