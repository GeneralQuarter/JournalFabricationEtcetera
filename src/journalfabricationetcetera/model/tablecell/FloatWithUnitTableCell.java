package journalfabricationetcetera.model.tablecell;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import journalfabricationetcetera.Utils;
import journalfabricationetcetera.model.ObjectWithUnit;
import journalfabricationetcetera.model.Unit;

/**
 * Created by Quentin Gangler on 01/11/2016.
 *
 */
public class FloatWithUnitTableCell<S extends ObjectWithUnit> extends FloatEditableTableCell<S> {

    private TableColumn<S, Float> param;

    public FloatWithUnitTableCell(TableColumn<S, Float> param) {
        super();
        this.param = param;
    }

    @Override
    protected String getTextToDIsplayOnCell() {
        int currentIndex = indexProperty().getValue() < 0 ? 0 : indexProperty().getValue();
        Unit unit = param.getTableView().getItems().get(currentIndex).getUnit();
        return Utils.displayFloatWithUnit(getItem(), unit);
    }

    @Override
    protected void commitEditWithParsing(){
        getTableRow().requestFocus();
        float quantity = Utils.validateFloatNumber(textField.getText());
        if (quantity != -1) {
            commitEdit(quantity);
        }
    }
}
