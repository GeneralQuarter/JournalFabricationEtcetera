package journalfabricationetcetera.model;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by Quentin Gangler on 09/11/2016.
 *
 */
public class RawMaterialConsumption extends ObjectWithUnit{
    private StringProperty rawMaterialName;
    private FloatProperty stockAtDayX;
    private FloatProperty stockAtDayY;

    public RawMaterialConsumption(String rawMaterialname, Unit unit, float stockAtDayX, float stockAtDayY) {
        super(0, unit);
        this.rawMaterialName = new SimpleStringProperty(rawMaterialname);
        this.stockAtDayX = new SimpleFloatProperty(stockAtDayX);
        this.stockAtDayY = new SimpleFloatProperty(stockAtDayY);
    }

    public String getRawMaterialName() {
        return rawMaterialName.get();
    }

    public StringProperty rawMaterialNameProperty() {
        return rawMaterialName;
    }

    public void setRawMaterialName(String rawMaterialName) {
        this.rawMaterialName.set(rawMaterialName);
    }

    public float getStockAtDayX() {
        return stockAtDayX.get();
    }

    public FloatProperty stockAtDayXProperty() {
        return stockAtDayX;
    }

    public void setStockAtDayX(float stockAtDayX) {
        this.stockAtDayX.set(stockAtDayX);
    }

    public float getStockAtDayY() {
        return stockAtDayY.get();
    }

    public FloatProperty stockAtDayYProperty() {
        return stockAtDayY;
    }

    public void setStockAtDayY(float stockAtDayY) {
        this.stockAtDayY.set(stockAtDayY);
    }
}
