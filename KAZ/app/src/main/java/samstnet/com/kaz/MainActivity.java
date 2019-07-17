
        package samstnet.com.kaz;

        import android.Manifest;
        import android.content.pm.PackageManager;
        import android.os.AsyncTask;
        import android.os.Build;
        import android.support.annotation.NonNull;
        import android.support.design.widget.BottomNavigationView;
        import android.support.v4.app.FragmentManager;
        import android.support.v4.app.FragmentTransaction;
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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    //Grwoth-inventory눌럿을떄 fragment 를 전환 해주는 함수 index : 0 growth || index 1 : inventory
  /*  public void onFragmentChange(int index) {
        if (index == 0) {
            Log.d("MainActivity","fragmentgrowth 들어감");
            getSupportFragmentManager().beginTransaction().replace(R.id.change, fragmentgrowth).commit();
        } else if (index == 1) {
            Log.d("MainActivity","fragmentivent 들어감");
            getSupportFragmentManager().beginTransaction().replace(R.id.change, fragmentivent).commit();
        }
        Log.d("MainActivity","들어감");
    }
    */
}
