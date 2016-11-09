package journalfabricationetcetera.model;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by Quentin Gangler on 09/11/2016.
 *
 */
public class RecipeConsumption implements ObjectWithRecipe{

    private StringProperty recipeName;
    private FloatProperty consumption;
    private Recipe recipe;

    public RecipeConsumption(Recipe recipe, float consumption) {
        this.recipe = recipe;
        this.consumption = new SimpleFloatProperty(consumption);
        this.recipeName = new SimpleStringProperty(recipe.getName());
    }

    public String getRecipeName() {
        return recipeName.get();
    }

    public StringProperty recipeNameProperty() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName.set(recipeName);
    }

    public float getConsumption() {
        return consumption.get();
    }

    public FloatProperty consumptionProperty() {
        return consumption;
    }

    public void setConsumption(float consumption) {
        this.consumption.set(consumption);
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public Recipe getRecipe() {
        return recipe;
    }
}
