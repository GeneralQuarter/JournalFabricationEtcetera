package journalfabricationetcetera.model.tablecell;

import javafx.scene.control.TableColumn;
import journalfabricationetcetera.Utils;
import journalfabricationetcetera.model.ObjectWithUnit;

/**
 * Created by Quentin Gangler on 03/11/2016.
 *
 */
public class FloatWithUnitNegativeTableCell<S extends ObjectWithUnit> extends FloatWithUnitTableCell<S> {

    public FloatWithUnitNegativeTableCell(TableColumn<S, Float> param) {
        super(param);
    }

    @Override
    protected String getTextToDIsplayOnCell() {
        float quantity = getItem();
        if(quantity > 0) {
            return "+" + super.getTextToDIsplayOnCell();
        } else {
            return super.getTextToDIsplayOnCell();
        }
    }

    @Override
    protected void commitEditWithParsing() {
        getTableRow().requestFocus();
        Float quantityFloat = Utils.validateAndParseFloat(textField.getText());
        float quantity = 0;
        if(quantityFloat != null) {
            quantity = quantityFloat;
        }
        commitEdit(quantity);
    }
}
