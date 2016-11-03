package journalfabricationetcetera.model.tablecell;

import javafx.scene.control.Alert;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import journalfabricationetcetera.Utils;

/**
 * Created by Quentin Gangler on 03/11/2016.
 *
 */
public abstract class FloatEditableTableCell<S> extends TableCell<S, Float> {

    protected TextField textField;

    public FloatEditableTableCell() {
        super();
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();

        setText(getTextToDIsplayOnCell());
        setGraphic(null);
    }

    @Override
    public void startEdit() {
        if (!isEmpty()) {
            super.startEdit();
            getTableRow().requestFocus();
            createTextField();
            setText(null);
            setGraphic(textField);
            textField.requestFocus();
        }
    }

    @Override
    protected void updateItem(Float item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setText("");
            setStyle("");
        } else {
            if (isEditing()) {
                if (textField != null) {
                    textField.setText(getString());
                }
                setText(null);
                setGraphic(textField);
            } else {
                setText(getTextToDIsplayOnCell());
                setGraphic(null);
            }
        }
    }

    private void createTextField() {
        textField = new TextField(getString());
        textField.selectAll();
        textField.setMinWidth(this.getWidth() - this.getGraphicTextGap()* 2);
        textField.setOnKeyReleased(t -> {
            if (t.getCode() == KeyCode.ENTER) {
                commitEditWithParsing();
            } else if (t.getCode() == KeyCode.ESCAPE) {
                cancelEdit();
            }
        });
    }

    protected abstract String getTextToDIsplayOnCell();

    protected abstract void commitEditWithParsing();

    private String getString() {
        return getItem() == null ? "" : getItem().toString();
    }
}
