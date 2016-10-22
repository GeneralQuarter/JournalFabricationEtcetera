package journalfabricationetcetera.controller;

import javafx.fxml.Initializable;
import journalfabricationetcetera.db.Database;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Quentin Gangler on 22/10/2016.
 *
 */
public class RecipesController implements Initializable {

    private Database db;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void afterInitialize() {

    }

    public void setDb(Database db) {
        this.db = db;
    }
}
