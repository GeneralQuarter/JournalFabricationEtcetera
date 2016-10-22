package journalfabricationetcetera.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by Quentin Gangler on 21/10/2016.
 *
 */
public class RawMaterial {
    private int id;
    private StringProperty name;
    private ObjectProperty<Unit> unit;

    public RawMaterial(int id, String name, Unit unit) {
        this.id = id;
        this.name = new SimpleStringProperty(name);
        this.unit = new SimpleObjectProperty<>(unit);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Unit getUnit() {
        return unit.get();
    }

    public ObjectProperty<Unit> unitProperty() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit.set(unit);
    }
}
