package journalfabricationetcetera.model;

import java.time.LocalDate;

/**
 * Created by Quentin Gangler on 21/10/2016.
 *
 */
public class Stock extends ObjectWithId{
    private RawMaterial rawMaterial;
    private LocalDate date;
    private float quantity;

    public Stock(int id, RawMaterial rawMaterial, LocalDate date, float quantity) {
        super(id);
        this.rawMaterial = rawMaterial;
        this.date = date;
        this.quantity = quantity;
    }

    public RawMaterial getRawMaterial() {
        return rawMaterial;
    }

    public void setRawMaterial(RawMaterial rawMaterial) {
        this.rawMaterial = rawMaterial;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }
}
