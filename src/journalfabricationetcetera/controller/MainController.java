package journalfabricationetcetera.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Quentin Gangler on 21/10/2016.
 *
 */
public class MainController extends SubController implements Initializable {

    @FXML private TabPane mainTabPane;
    @FXML private InventoryController inventoryTabController;
    @FXML private RecipesController recipesTabController;
    @FXML private JournalController journalTabController;
    @FXML private ConsumptionController consumptionTabController;

    private List<SubController> controllers;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        controllers = new ArrayList<>();
    }

    @Override
    public void afterInitialize() {
        SingleSelectionModel<Tab> selectionModel = mainTabPane.getSelectionModel();
        selectionModel.select(0);

        selectionModel.selectedItemProperty().addListener(
                (ov, t, t1) -> updateForTab(selectionModel.getSelectedIndex())
        );

        controllers.add(journalTabController);
        controllers.add(inventoryTabController);
        controllers.add(recipesTabController);
        controllers.add(consumptionTabController);

        for (SubController sub : controllers) {
            sub.setData(data);
            sub.afterInitialize();
        }
    }

    @Override
    public void update() {

    }

    private void updateForTab(int index) {
        SubController controller = controllers.get(index);
        if(controller != null) {
            controller.update();
        }
    }

    private void print(String message) {
        System.out.println("[MainController] " + message);
    }
}
