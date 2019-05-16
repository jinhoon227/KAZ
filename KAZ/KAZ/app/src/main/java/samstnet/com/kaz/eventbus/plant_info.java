package samstnet.com.kaz.eventbus;

public class plant_info {
    int level;//레벨
    int exp;//경험치
    String name;//이름
    int state;//1,2,3 따라 식물 진화

    public plant_info(int level, int exp, String name, int state){
    this.level=level;
    this.exp=exp;
    this.name=name;
    this.state=state;
    }
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
