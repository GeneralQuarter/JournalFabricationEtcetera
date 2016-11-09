package journalfabricationetcetera.model.tablecell;

import javafx.scene.control.TableCell;
import journalfabricationetcetera.Utils;

import java.time.LocalDate;

/**
 * Created by Quentin Gangler on 01/11/2016.
 *
 */
public class DateTableCell<S> extends TableCell<S, LocalDate> {

    @Override
    protected void updateItem(LocalDate item, boolean empty) {
        super.updateItem(item, empty);

        if (item == null || empty) {
            setText(null);
            setStyle("");
        } else {
            setText(Utils.getDateFormatter().format(item));
        }
    }
}
