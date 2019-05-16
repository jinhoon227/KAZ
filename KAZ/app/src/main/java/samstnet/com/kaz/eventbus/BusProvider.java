package samstnet.com.kaz.eventbus;

import com.squareup.otto.Bus;

public final class BusProvider {
    //Singleton
    private static final Bus BUS = new Bus();

    public static Bus getInstance() {
        return BUS;
    }

    private BusProvider() {
        // No instances.
    }
}
