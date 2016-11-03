package journalfabricationetcetera.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import journalfabricationetcetera.Utils;

/**
 * Created by Quentin Gangler on 21/10/2016.
 *
 */
public class Recipe extends ObjectWithId{
    private StringProperty name;
    private ObservableList<RecipeLine> lines;

    public Recipe(int id, String name) {
        super(id);
        this.name = new SimpleStringProperty(name);
        this.lines = FXCollections.observableArrayList();
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

    public ObservableList<RecipeLine> getLines() {
        return lines;
    }

    public void setLines(ObservableList<RecipeLine> lines) {
        this.lines = lines;
    }

    public String getTooltipMessage() {
        String output = "";
        for (RecipeLine line : getLines()) {
            output += line.getRawMaterial().getName() + "  :  " +  Utils.displayFloatWithUnit(line.getQuantity(), line.getRawMaterial().getUnit());
            output += "\n";
        }
        return output;
    }
}
