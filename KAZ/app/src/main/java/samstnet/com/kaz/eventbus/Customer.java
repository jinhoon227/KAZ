package samstnet.com.kaz.eventbus;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import samstnet.com.kaz.R;

public class Customer extends Application {

    public static Item_type item[] = new Item_type[50];

    public static plant_info plant1;

    public static SettingEvent setting1;

    public static int stateTime;

    public static final String CHANNEL_ID = "exampleServiceChannel";

    //아이탬 갯수 수정시에 추가
    public static final int ITEM_NUM=5;
    //PLANT_INFO 갯수
    public static final int PLANT_INFO_NUM=5;
    //SETTING EVENT 갯수
    public static final int SETTING_EVENT_NUM=5;
    //ALARMEVENT 갯수
    public static final int ALARMEVENT_NUM=24;

    //0 : seogi 1-4 : jinhoon-tutorial others : empty
    public static boolean alarmevent[] = new boolean[25];

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

    public Item_type _getItem(int i) {
        return item[i];
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

    public static boolean[] getAlarmevent() {
        return alarmevent;
    }

    public static void setAlarmevent(boolean[] alarmevent) {
        Customer.alarmevent = alarmevent;
    }

    public static int getStateTime() {
        return stateTime;
    }

    public static void setStateTime(int stateTime) {
        Customer.stateTime = stateTime;
    }

    public void onCreate() {
        super.onCreate();
        boolean[] items = {false, false, false, false, false};
        plant1 = new plant_info(1, 0, "PLANT1", 1, 5, items, 50);
        String[] tmparr;
        position = 0;
        String itemtmp[] = new String[100];
        String planttmp;
        String settmp;
        String itemWearTmp;
        String alarmeventtmp;
        String timetmp;

        //처음 기본 데이터 저장하는 Customer
        String Initalize_plant;
        String Intialize_item[] = new String[50];
        String Intialize_setting;
        String Intialize_itemwear;
        String Intialize_alarmevent;
        String Intialize_timestate;


        Initalize_plant = "1&0&PLANT1&1&5&50";

        Intialize_item[0]= "물뿌리개&비를 피할 수 있는 우산을 씌워 줍니다&" + R.drawable.vec_sprinkler+ "&10&false&false";
        Intialize_item[1]="비료&식물이 더 잘 자랄 수 있는 영양분을 제공합니다&" + R.drawable.vec_beryo+ "&20&false&false";
        Intialize_item[2]="우산&비를 피할 수 있는 우산을 씌워 줍니다&" + R.drawable.vec_umbrella + "&50&false&false";
        Intialize_item[3]="썬글라스&강한 햇빛을 피할 수 있는 썬글라쓰를 씌워 줍니다&" + R.drawable.vec_sunglasses + "&60&false&false";
        Intialize_item[4]="목도리&추위를 피할 수 있게 목도리를 두릅니다.&" + R.drawable.vec_scarf+ "&100&false&false";
        Intialize_setting= "true&true&true&true&true";
        //Intialize_setting="false&false&false&false&false";
        Intialize_itemwear="false&false&false&false&false";
        Intialize_alarmevent="false&false&false&false&false&false&false&false&false&false&false&false&false&false&false&false&false&false&false&false&false&false&false&false";
        Intialize_timestate="0";

        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE); // 선언
        SharedPreferences prefs = getSharedPreferences("pref", MODE_PRIVATE); // 선언
        SharedPreferences.Editor editor = pref.edit();// editor에 put 하기

        boolean isExist = pref.contains("item");
        if (!isExist) {
            Log.d("초기 데이터 초기화 시작", "");
            editor.putBoolean("item", true);
            editor.putInt("money", 200);
            // item 연결 : 0. 물뿌리개   1. 비료     2. 우산 3. 썬글라스 4. 목도리
            editor.putString("plant", Initalize_plant);
            for(int i=0;i<PLANT_INFO_NUM;i++) {
                editor.putString("item"+i, Intialize_item[i]); //item1라는 key값으로 id 데이터를 저장한다.
            }

            editor.putString("setting", Intialize_setting);
            editor.putString("itemwear", Intialize_itemwear);
            editor.putString("alarmevent",Intialize_alarmevent);
            editor.putString("stateTime",Intialize_timestate);
            editor.commit(); //완료한다.
        }
        for (int i = 0; i < ITEM_NUM; i++) {
            itemtmp[i] = prefs.getString("item" + i, Intialize_item[i]);
            Log.d("아이탬 문자열 배열" + i, itemtmp[i]);
            tmparr = itemtmp[i].split("&");
            item[i] = new Item_type(tmparr[0], tmparr[1], Integer.parseInt(tmparr[2]), Integer.parseInt(tmparr[3]), Boolean.valueOf(tmparr[4]), Boolean.valueOf(tmparr[5]));
        }
        itemWearTmp = prefs.getString("itemwear", Intialize_itemwear);
        Log.d("장착해서 버튼 누름", itemWearTmp);
        tmparr = itemWearTmp.split("&");
        for (int i = 0; i < SETTING_EVENT_NUM; i++) {
            items[i] = Boolean.valueOf(tmparr[i]);
        }

        alarmeventtmp = prefs.getString("alarmevent",Intialize_alarmevent);
        Log.d("Alarmevent데이터", alarmeventtmp);
        tmparr=alarmeventtmp.split("&");
        for(int i=0;i<ALARMEVENT_NUM;i++)
        {
            alarmevent[i]=Boolean.valueOf(tmparr[i]);
        }
        planttmp = prefs.getString("plant", Initalize_plant);
        tmparr = planttmp.split("&");
        plant1 = new plant_info(Integer.parseInt(tmparr[0]), Integer.parseInt(tmparr[1]), tmparr[2], Integer.parseInt(tmparr[3]), Integer.parseInt(tmparr[4]), items, Integer.parseInt(tmparr[5]));
        Log.d("firstlove",String.valueOf(plant1.getLove()));
        money = prefs.getInt("money", 0);
        settmp = prefs.getString("setting", Intialize_setting);
        tmparr = settmp.split("&");
        setting1 = new SettingEvent(Boolean.valueOf(tmparr[0]), Boolean.valueOf(tmparr[1]), Boolean.valueOf(tmparr[2]), Boolean.valueOf(tmparr[3]));
        Log.d("고객 돈 :", "" + money);
        timetmp = prefs.getString("stateTime",Intialize_timestate);
        stateTime = Integer.parseInt(timetmp);
        createNotificationChannel();
    }

    private static Customer instance = null;


    public static synchronized Customer getInstance() {

        if (null == instance) {

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