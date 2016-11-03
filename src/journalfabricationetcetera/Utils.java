package journalfabricationetcetera;

import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import journalfabricationetcetera.model.RawMaterial;
import journalfabricationetcetera.model.RecipeLine;
import journalfabricationetcetera.model.Unit;

import javax.swing.text.DateFormatter;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;

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

    public static String displayMultiplier(float number) {
        DecimalFormat format = new DecimalFormat("0.#");
        return format.format(number);
    }

    public static float validateFloatNumber(String floatStr) {
        Float nombreFloat = validateAndParseFloat(floatStr);
        float nombre = -1;
        if(nombreFloat != null) {
            nombre = nombreFloat;
        }
        if (nombre < 0) {
            showAlert(Alert.AlertType.WARNING, "Le nombre entré est négatif", "Le nombre \"" + floatStr + "\" est négatif");
        } else {
            return nombre;
        }
        return -1;
    }

    public static Float validateAndParseFloat(String floatStr) {
        try {
            return Float.parseFloat(floatStr);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Le texte entré n'est pas un nombre", "Le texte \"" + floatStr + "\" est invalide (Le point est utilisé pour les virgules)");
        }
        return null;
    }

    public static void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.setHeaderText(null);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("file:resources/icone.png"));
        alert.show();
    }

    public static boolean containsRawMaterial(ObservableList<RecipeLine> recipeLines, RawMaterial rm) {
        for (RecipeLine rl : recipeLines) {
            if (rl.getRawMaterial() == rm) {
                return true;
            }
        }
        return false;
    }

    public static DateTimeFormatter getDateFormatter() {
        return DateTimeFormatter.ofPattern("dd/MM/yyyy");
    }
}
