package journalfabricationetcetera.model;

import javafx.collections.ObservableList;

/**
 * Created by Quentin Gangler on 03/11/2016.
 *
 */
public abstract class ObjectWithId {
    protected int id;

    public ObjectWithId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
