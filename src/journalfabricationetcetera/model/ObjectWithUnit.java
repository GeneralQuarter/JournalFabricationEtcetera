package journalfabricationetcetera.model;

/**
 * Created by Quentin Gangler on 01/11/2016.
 *
 */
public class ObjectWithUnit extends ObjectWithId{

    ObjectWithUnit(int id, Unit unit) {
        super(id);
        this.unit = unit;
    }

    protected Unit unit;

    public Unit getUnit() {
        return unit;
    }
}
