package journalfabricationetcetera.model;

/**
 * Created by Quentin Gangler on 03/11/2016.
 *
 */
public abstract class ObjectWithId {
    protected int id;

    ObjectWithId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
