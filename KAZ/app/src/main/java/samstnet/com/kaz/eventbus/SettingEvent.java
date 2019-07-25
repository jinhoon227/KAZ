package samstnet.com.kaz.eventbus;

public class SettingEvent {
    boolean createevent;
    boolean switch1event;
    boolean soundevent;
    boolean screen;

    public SettingEvent(boolean createevent, boolean switch1event, boolean soundevent, boolean screen) {
        this.createevent = createevent;
        this.switch1event = switch1event;
        this.soundevent = soundevent;
        this.screen = screen;
    }

    public boolean isCreateevent() {
        return createevent;
    }

    public void setCreateevent(boolean createevent) {
        this.createevent = createevent;
    }

    public boolean isSwitch1event() {
        return switch1event;
    }

    public void setSwitch1event(boolean switch1event) {
        this.switch1event = switch1event;
    }

    public boolean isSoundevent() {
        return soundevent;
    }

    public void setSoundevent(boolean soundevent) {
        this.soundevent = soundevent;
    }

    public boolean isScreen() {
        return screen;
    }

    public void setScreen(boolean screen) {
        this.screen = screen;
    }

}
