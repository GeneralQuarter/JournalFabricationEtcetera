package journalfabricationetcetera.model.tablecell;

import javafx.scene.control.Alert;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import journalfabricationetcetera.Utils;
import journalfabricationetcetera.model.Journal;
import journalfabricationetcetera.model.Unit;

/**
 * Created by Quentin Gangler on 03/11/2016.
 *
 */
public class FloatWithMultiplierTableCell extends FloatEditableTableCell<Journal> {

    public FloatWithMultiplierTableCell() {
        super();
    }

    @Override
    protected String getTextToDIsplayOnCell() {
        return Utils.displayMultiplier(getItem());
    }

    @Override
    protected void commitEditWithParsing(){
        getTableRow().requestFocus();
        float quantity = Utils.validateFloatNumber(textField.getText());
        if (quantity != -1 && quantity != 0) {
            commitEdit(quantity);
        } else if (quantity == 0){
            Utils.showAlert(Alert.AlertType.WARNING, "Le multiplicateur ne peux pas être 0", "Le multiplicateur ne peux pas être 0");
        }
    }
}
