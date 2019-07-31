package samstnet.com.kaz.eventbus;

public class plant_info {
    int level;//레벨
    int exp;//경험치
    String name;//이름
    int state;//1,2,3 따라 식물 진화
    int love;//슬픔 평범 기쁨 이 수치의 의해 결정됨

    //소현------------------------------------------------------------------------------

    // items   : 1. 물뿌리개   2. 비료     3. 우산 4. 모자 5. 옷
    // wtstate : 1. manycloud 2. fewcloud 3. sun 4. rain 5. snow
    // 아이템을 사용 했는지 여부, 3시간마다 false로 리셋해주어야 함.
    public static boolean[] items;
    static int itemNum;


    //----------------------------------------------------------------------------------

    public plant_info(int level, int exp, String name, int state, int itemNum,boolean[] items,int love){
    this.level=level;
    this.exp=exp;
    this.name=name;
    this.state=state;
    this.items=items;
    this.love = love;

    //소현--------------------------------
        this.itemNum=itemNum;
        items=new boolean[itemNum];

        for(int i=0;i<itemNum;i++){
            items[i]=false;
        }
    //-----------------------------------------
    }

    //소현 ---------------------------------------------------------------------
    //비료 물뿌리개는 시간에 따라서 뜨고 안뜨게 변경해야함. 3시간 간격으로 아이템 리셋하게 하자
    public void setItems(int i) {
        this.items[i] = true;
    }
    public boolean getItems(int i){
        return this.items[i];
    }
    //----------------------------------------------------------------------------


    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public void StateChange(){
        state++;
    }

    public static int getItemNum() {
        return itemNum;
    }

    public static boolean[] getItems() {
        return items;
    }

    public int getLove() {
        return love;
    }

    public void setLove(int love) {
        this.love = love;
    }
}
