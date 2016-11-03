package journalfabricationetcetera.model;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * Created by Quentin Gangler on 21/10/2016.
 *
 */
public class RecipeLine extends ObjectWithUnit{
    private ObjectProperty<Recipe> recipe;
    private RawMaterial rawMaterial;
    private FloatProperty quantity;

    public RecipeLine(Recipe recipe, RawMaterial rawMaterial, float quantity) {
        super(0, rawMaterial.getUnit());
        this.recipe = new SimpleObjectProperty<>(recipe);
        this.rawMaterial = rawMaterial;
        this.quantity = new SimpleFloatProperty(quantity);
    }

    public Recipe getRecipe() {
        return recipe.get();
    }

    public ObjectProperty<Recipe> recipeProperty() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe.set(recipe);
    }

    public RawMaterial getRawMaterial() {
        return rawMaterial;
    }

    public void setRawMaterial(RawMaterial rawMaterial) {
        this.rawMaterial = rawMaterial;
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
