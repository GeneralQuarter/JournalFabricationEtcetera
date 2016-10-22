package journalfabricationetcetera.model;

import javafx.beans.property.*;

import java.time.LocalDate;

/**
 * Created by Quentin Gangler on 21/10/2016.
 *
 */
public class StockView {
    private int rawMaterialID;
    private int stockID;
    private int unitID;
    private StringProperty name;
    private FloatProperty lastQuantityInStock;
    private ObjectProperty<LocalDate> lastDateInStock;
    private FloatProperty stockAvailable;

    public StockView(int rawMaterialID, int stockID, int unitID, String name, float lastQuantityInStock, String lastDateInStock, float stockAvailable) {
        this.rawMaterialID = rawMaterialID;
        this.stockID = stockID;
        this.unitID = unitID;
        this.name = new SimpleStringProperty(name);
        this.lastQuantityInStock = new SimpleFloatProperty(lastQuantityInStock);
        this.lastDateInStock = new SimpleObjectProperty<>(LocalDate.parse(lastDateInStock));
        this.stockAvailable = new SimpleFloatProperty(stockAvailable);
    }

    public int getRawMaterialID() {
        return rawMaterialID;
    }

    public void setRawMaterialID(int rawMaterialID) {
        this.rawMaterialID = rawMaterialID;
    }

    public int getStockID() {
        return stockID;
    }

    public void setStockID(int stockID) {
        this.stockID = stockID;
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

    public float getLastQuantityInStock() {
        return lastQuantityInStock.get();
    }

    public FloatProperty lastQuantityInStockProperty() {
        return lastQuantityInStock;
    }

    public void setLastQuantityInStock(float lastQuantityInStock) {
        this.lastQuantityInStock.set(lastQuantityInStock);
    }

    public LocalDate getLastDateInStock() {
        return lastDateInStock.get();
    }

    public ObjectProperty<LocalDate> lastDateInStockProperty() {
        return lastDateInStock;
    }

    public void setLastDateInStock(LocalDate lastDateInStock) {
        this.lastDateInStock.set(lastDateInStock);
    }

    public float getStockAvailable() {
        return stockAvailable.get();
    }

    public FloatProperty stockAvailableProperty() {
        return stockAvailable;
    }

    public void setStockAvailable(float stockAvailable) {
        this.stockAvailable.set(stockAvailable);
    }

    public int getUnitID() {
        return unitID;
    }
}
