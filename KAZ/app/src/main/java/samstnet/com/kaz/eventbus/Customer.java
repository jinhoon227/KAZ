package samstnet.com.kaz.eventbus;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.util.Log;

import samstnet.com.kaz.R;

public class Customer extends Application {

    public static Item_type item[] = new Item_type[50];

    public static plant_info plant1;

    public static SettingEvent setting1;

    public static final String CHANNEL_ID = "exampleServiceChannel";


    private int money;

    private int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public static plant_info getPlant() {
        return plant1;
    }

    public static void setPlant(plant_info plant1) {
        Customer.plant1 = plant1;
    }

    public Item_type[] getItem() {
        return item;
    }

    public void setItem(Item_type[] item) {
        this.item = item;
    }

    public static SettingEvent getSetting1() {
        return setting1;
    }

    public static void setSetting1(SettingEvent setting1) {
        Customer.setting1 = setting1;
    }

    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        plant1 = new plant_info(1,0,"PLANT1",1,5);
        String []tmparr;
        position=0;
        String itemtmp[] = new String [100];
        String planttmp;
        String settmp;

        SharedPreferences pref= getSharedPreferences("pref", MODE_PRIVATE); // 선언
        SharedPreferences prefs= getSharedPreferences("pref", MODE_PRIVATE); // 선언
        SharedPreferences.Editor editor = pref.edit();// editor에 put 하기

        boolean isExist = pref.contains("item");
        if(!isExist) {
            Log.d("초기 데이터 초기화 시작" ,"");
            editor.putBoolean("item", true);
            editor.putInt("money",200);
            editor.putString("plant","1&0&PLANT1&1&5");
            editor.putString("item0", "우산&비를 피할 수 있는 우산을 씌워 줍니다&" + R.drawable.img1 + "&10&false&false"); //item1라는 key값으로 id 데이터를 저장한다.
            editor.putString("item1", "우비&비를 피할 수 있는 우비를 씌워 줍니다&" + R.drawable.img2 + "&20&false&false"); //item1라는 key값으로 id 데이터를 저장한다.
            editor.putString("item2", "선풍기&더위를 피할 수 있게 선풍기를 틉니다.&" + R.drawable.img3 + "&50&false&false"); //item1라는 key값으로 id 데이터를 저장한다.
            editor.putString("item3", "난로&추위를 피할 수 있게 난로를 틉니다&" + R.drawable.img4 + "&60&false&false"); //item1라는 key값으로 id 데이터를 저장한다.
            editor.putString("setting","false&false&false&false&false");
            editor.commit(); //완료한다.
        }
        for(int i=0;i<4;i++) {
            itemtmp[i]=prefs.getString("item"+i,null);
            Log.d("아이탬 문자열 배열"+i,itemtmp[i]);
            tmparr = itemtmp[i].split("&");
            item[i] = new Item_type(tmparr[0], tmparr[1], Integer.parseInt(tmparr[2]), Integer.parseInt(tmparr[3]), Boolean.valueOf(tmparr[4]), Boolean.valueOf(tmparr[5]));
        }
        planttmp=prefs.getString("plant",null);
        tmparr=planttmp.split("&");
        plant1 = new plant_info(Integer.parseInt(tmparr[0]),Integer.parseInt(tmparr[1]),tmparr[2],Integer.parseInt(tmparr[3]),Integer.parseInt(tmparr[4]));
        money=prefs.getInt("money",0);
        settmp = prefs.getString("setting",null);
        tmparr = settmp.split("&");
        setting1 =  new SettingEvent(Boolean.valueOf(tmparr[0]),Boolean.valueOf(tmparr[1]),Boolean.valueOf(tmparr[2]),Boolean.valueOf(tmparr[3]));
        Log.d("고객 돈 :", ""+money);

        /*
            item[0]= new Item_type("우산","비를 피할 수 있는 우산을 씌워 줍니다",R.drawable.img1,10,false,false);
            item[1]= new Item_type("우비","비를 피할 수 있는 우비를 씌워 줍니다",R.drawable.img2,20,false,false);
            item[2]= new Item_type("선풍기","더위를 피할 수 있게 선풍기를 틉니다",R.drawable.img3,50,false,false);

            item[3]= new Item_type("난로","추위를 피할 수 있게 난로를 틉니다",R.drawable.img4,60,false,false);
            item[4]= new Item_type("마스크","미세먼지를 막을 수 있게 마스크를 씌워줍니다",R.drawable.img5,5,false,false);

            item[5]= new Item_type("공기청정기","미세먼지를 막을 수 있게 공기 청정기를 틀어줍니다",R.drawable.img6,90,false,false);
            item[6]= new Item_type("에어컨","더위를 피할 수 있게 에어컨을 틉니다",R.drawable.img6,150,false,false);
            */



    }
    private static Customer instance = null;


    public static synchronized Customer getInstance(){

        if(null == instance){

            instance = new Customer();

        }

        return instance;

    }

    @Override
    public String toString() {
        return "Customer{" +
                "item=" + item +
                '}';
    }


    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Example Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

}