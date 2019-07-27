package samstnet.com.kaz.eventbus;

import android.app.Application;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.util.Log;

import samstnet.com.kaz.R;

public class Customer extends Application {

    public static Item_type item[] = new Item_type[50];

    public static plant_info plant1;

    public static SettingEvent setting1;


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

    public Item_type _getItem(int i){ return item[i];}

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
        boolean[] items = {false,false,false,false,false};
        plant1 = new plant_info(1,0,"PLANT1",1,5,items);
        String []tmparr;
        position=0;
        String itemtmp[] = new String [100];
        String planttmp;
        String settmp;
        String itemWearTmp;

        SharedPreferences pref= getSharedPreferences("pref", MODE_PRIVATE); // 선언
        SharedPreferences prefs= getSharedPreferences("pref", MODE_PRIVATE); // 선언
        SharedPreferences.Editor editor = pref.edit();// editor에 put 하기

        boolean isExist = pref.contains("item");
        if(!isExist) {
            Log.d("초기 데이터 초기화 시작" ,"");
            editor.putBoolean("item", true);
            editor.putInt("money",200);
            // item 연결 : 0. 물뿌리개   1. 비료     2. 우산 3. 썬글라스 4. 목도리
            editor.putString("plant","1&0&PLANT1&1&5");
            editor.putString("item0", "물뿌리개&비를 피할 수 있는 우산을 씌워 줍니다&" + R.drawable.img1 + "&10&false&false"); //item1라는 key값으로 id 데이터를 저장한다.
            editor.putString("item1", "비료&식물이 더 잘 자랄 수 있는 영양분을 제공합니다&" + R.drawable.img2 + "&20&false&false"); //item1라는 key값으로 id 데이터를 저장한다.
            editor.putString("item2", "우산&비를 피할 수 있는 우산을 씌워 줍니다&" + R.drawable.img3 + "&50&false&false"); //item1라는 key값으로 id 데이터를 저장한다.
            editor.putString("item3", "썬글라스&강한 햇빛을 피할 수 있는 썬글라쓰를 씌워 줍니다&" + R.drawable.img4 + "&60&false&false"); //item1라는 key값으로 id 데이터를 저장한다.
            editor.putString("item4", "목도리&추위를 피할 수 있게 목도리를 두릅니다.&" + R.drawable.img4 + "&60&false&false"); //item1라는 key값으로 id 데이터를 저장한다.
            editor.putString("setting","false&false&false&false&false");
            editor.putString("itemwear","false&false&false&false&false");
            editor.commit(); //완료한다.
        }
        for(int i=0;i<5;i++) {
            itemtmp[i]=prefs.getString("item"+i,null);
            Log.d("아이탬 문자열 배열"+i,itemtmp[i]);
            tmparr = itemtmp[i].split("&");
            item[i] = new Item_type(tmparr[0], tmparr[1], Integer.parseInt(tmparr[2]), Integer.parseInt(tmparr[3]), Boolean.valueOf(tmparr[4]), Boolean.valueOf(tmparr[5]));
        }
        itemWearTmp = prefs.getString("itemwear",null);
        Log.d("장착해서 버튼 누름", itemWearTmp);
        tmparr = itemWearTmp.split("&");
        for(int i=0;i<5;i++)
        {
            items[i] = Boolean.valueOf(tmparr[i]);
        }
        planttmp=prefs.getString("plant",null);
        tmparr=planttmp.split("&");
        plant1 = new plant_info(Integer.parseInt(tmparr[0]),Integer.parseInt(tmparr[1]),tmparr[2],Integer.parseInt(tmparr[3]),Integer.parseInt(tmparr[4]),items);
        money=prefs.getInt("money",0);
        settmp = prefs.getString("setting",null);
        tmparr = settmp.split("&");
        setting1 =  new SettingEvent(Boolean.valueOf(tmparr[0]),Boolean.valueOf(tmparr[1]),Boolean.valueOf(tmparr[2]),Boolean.valueOf(tmparr[3]));
        Log.d("고객 돈 :", ""+money);


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

}