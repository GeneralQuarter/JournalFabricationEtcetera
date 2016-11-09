package journalfabricationetcetera.model.tablecell;

import javafx.scene.control.Alert;
import journalfabricationetcetera.Utils;

/**
 * Created by Quentin Gangler on 03/11/2016.
 *
 */
public class FloatWithMultiplierTableCell<S> extends FloatEditableTableCell<S> {

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
