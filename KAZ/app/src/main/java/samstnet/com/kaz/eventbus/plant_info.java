package samstnet.com.kaz.eventbus;

public class plant_info {
    int level;//레벨
    int exp;//경험치
    String name;//이름
    int state;//1,2,3 따라 식물 진화

    //소현------------------------------------------------------------------------------

    // items   : 1. 물뿌리개   2. 비료     3. 우산 4. 모자 5. 옷
    // wtstate : 1. manycloud 2. fewcloud 3. sun 4. rain 5. snow
    static boolean[] items;
    static int itemNum;


    //----------------------------------------------------------------------------------

    public plant_info(int level, int exp, String name, int state, int itemNum){
    this.level=level;
    this.exp=exp;
    this.name=name;
    this.state=state;

    //소현--------------------------------
        this.itemNum=itemNum;
        items=new boolean[itemNum];
        //이쪽부분은 db랑 연결해서 데이터 받아올 수 있게 되면 바꿔야함
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


}