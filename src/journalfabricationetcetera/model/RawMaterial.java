package journalfabricationetcetera.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by Quentin Gangler on 21/10/2016.
 *
 */
public class RawMaterial extends ObjectWithUnit{
    private StringProperty name;

    public RawMaterial(int id, String name, Unit unit) {
        super(id, unit);
        this.name = new SimpleStringProperty(name);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }
}
