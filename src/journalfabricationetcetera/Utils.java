package journalfabricationetcetera;

import javafx.scene.control.Alert;
import journalfabricationetcetera.model.Unit;

import java.text.DecimalFormat;

/**
 * Created by Quentin Gangler on 22/10/2016.
 *
 */
public class Utils {

    public static String displayFloatWithUnit(float number, Unit unit) {
        DecimalFormat format = new DecimalFormat("0.#");
        if(number < 1 && number > -1) {
            number *= 1000;
            return format.format(number) + " " + unit.getAbr1();
        } else {
            return format.format(number) + " " + unit.getAbr1000();
        }
    }

    public static float validateQuantity(String quantityStr) {
        float quantity = 0.0f;
        try {
            quantity = Float.parseFloat(quantityStr);
            if (quantity < 0) {
                showAlert(Alert.AlertType.WARNING, "La quantité indiqué est négative", "La quantité \"" + quantityStr + "\" est négative");
            } else {
                return quantity;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "La quantité indiqué n'est pas un nombre", "La quantité \"" + quantityStr + "\" est invalide (Le point est utilisé pour les virgules)");
        }
        return -1;
    }

    public static void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.setHeaderText(null);
        alert.show();
    }
}
