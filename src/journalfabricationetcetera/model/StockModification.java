package journalfabricationetcetera.model;

import javafx.beans.property.*;

import java.time.LocalDate;

/**
 * Created by Quentin Gangler on 03/11/2016.
 *
 */
public class StockModification extends ObjectWithUnit{

    private RawMaterial rawMaterial;
    private ObjectProperty<LocalDate> date;
    private FloatProperty quantity;
    private StringProperty description;

    public StockModification(int id, RawMaterial rawMaterial, LocalDate date, float quantity, String description) {
        super(id, rawMaterial.getUnit());
        this.rawMaterial = rawMaterial;
        this.date = new SimpleObjectProperty<>(date);
        this.quantity = new SimpleFloatProperty(quantity);
        this.description = new SimpleStringProperty(description);
    }

    public String getDescription() {
        return description.get();
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public RawMaterial getRawMaterial() {
        return rawMaterial;
    }

    public void setRawMaterial(RawMaterial rawMaterial) {
        this.rawMaterial = rawMaterial;
    }

    public LocalDate getDate() {
        return date.get();
    }

    public ObjectProperty<LocalDate> dateProperty() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date.set(date);
    }

    public float getQuantity() {
        return quantity.get();
    }

    public FloatProperty quantityProperty() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity.set(quantity);
    }
}
