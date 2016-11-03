package journalfabricationetcetera.controller;

import journalfabricationetcetera.db.Data;

/**
 * Created by Quentin Gangler on 01/11/2016.
 *
 */
public abstract class SubController {
    Data data;

    public void setData(Data data) {
        this.data = data;
    }

    public abstract void afterInitialize();

    public abstract void update();
}
