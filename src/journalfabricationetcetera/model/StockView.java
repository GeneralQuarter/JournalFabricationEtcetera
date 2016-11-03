package journalfabricationetcetera.model;

import javafx.beans.property.*;

import java.time.LocalDate;

/**
 * Created by Quentin Gangler on 21/10/2016.
 *
 */
public class StockView extends ObjectWithUnit {
    private RawMaterial rawMaterial;
    private Stock stock;
    private StringProperty name;
    private FloatProperty lastQuantityInStock;
    private ObjectProperty<LocalDate> lastDateInStock;
    private FloatProperty stockAvailable;

    public StockView(RawMaterial rawMaterial, Stock stock, Unit unit, String name, float lastQuantityInStock, String lastDateInStock, float stockAvailable) {
        super(0, unit);
        this.rawMaterial = rawMaterial;
        this.stock = stock;
        this.name = new SimpleStringProperty(name);
        this.lastQuantityInStock = new SimpleFloatProperty(lastQuantityInStock);
        this.lastDateInStock = new SimpleObjectProperty<>(LocalDate.parse(lastDateInStock));
        this.stockAvailable = new SimpleFloatProperty(stockAvailable);
    }

    public RawMaterial getRawMaterial() {
        return rawMaterial;
    }

    public void setRawMaterial(RawMaterial rawMaterial) {
        this.rawMaterial = rawMaterial;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
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
}
