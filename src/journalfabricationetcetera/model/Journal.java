package journalfabricationetcetera.model;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;

import java.time.LocalDate;

/**
 * Created by Quentin Gangler on 21/10/2016.
 *
 */
public class Journal extends ObjectWithId{
    private Recipe recipe;
    private ObjectProperty<LocalDate> date;
    private FloatProperty multiplier;

    public Journal(int id, Recipe recipe, LocalDate date, float multiplier) {
        super(id);
        this.recipe = recipe;
        this.date = new SimpleObjectProperty<>(date);
        this.multiplier = new SimpleFloatProperty(multiplier);
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
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

    public float getMultiplier() {
        return multiplier.get();
    }

    public FloatProperty multiplierProperty() {
        return multiplier;
    }

    public void setMultiplier(float multiplier) {
        this.multiplier.set(multiplier);
    }
}
