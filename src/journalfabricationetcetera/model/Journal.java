package journalfabricationetcetera.model;

/**
 * Created by Quentin Gangler on 21/10/2016.
 *
 */
public class Journal {
    private int id;
    private Recipe recipe;
    private String date;
    private float multiplier;

    public Journal(int id, Recipe recipe, String date, float multiplier) {
        this.id = id;
        this.recipe = recipe;
        this.date = date;
        this.multiplier = multiplier;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(float multiplier) {
        this.multiplier = multiplier;
    }
}
