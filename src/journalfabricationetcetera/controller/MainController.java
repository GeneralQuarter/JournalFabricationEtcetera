package journalfabricationetcetera.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;
import journalfabricationetcetera.Utils;
import journalfabricationetcetera.db.Database;
import journalfabricationetcetera.model.RawMaterial;
import journalfabricationetcetera.model.Stock;
import journalfabricationetcetera.model.StockView;
import journalfabricationetcetera.model.Unit;

import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by Quentin Gangler on 21/10/2016.
 *
 */
public class MainController implements Initializable {

    @FXML private TabPane mainTabPane;
    @FXML private InventoryController inventoryTabController;
    @FXML private RecipesController recipesTabController;

    private Database db;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void afterInitialize() {
        SingleSelectionModel<Tab> selectionModel = mainTabPane.getSelectionModel();
        selectionModel.select(2);
        if (inventoryTabController != null) {
            inventoryTabController.setDb(db);
            inventoryTabController.afterInitialize();
        } else {
            print("InventoryController null");
        }
        if (recipesTabController != null) {
            recipesTabController.setDb(db);
            recipesTabController.afterInitialize();
        }
    }

    public void setDB(Database db) {
        this.db = db;
    }

    private void print(String message) {
        System.out.println("[MainController] " + message);
    }
}
