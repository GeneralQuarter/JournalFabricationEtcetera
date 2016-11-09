package journalfabricationetcetera.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import journalfabricationetcetera.model.RawMaterialConsumption;
import journalfabricationetcetera.model.RecipeConsumption;
import journalfabricationetcetera.model.StockModification;
import journalfabricationetcetera.model.tablecell.FloatWithMultiplierTableCell;
import journalfabricationetcetera.model.tablecell.FloatWithUnitNegativeTableCell;
import journalfabricationetcetera.model.tablecell.FloatWithUnitTableCell;
import journalfabricationetcetera.model.tablecell.RecipeTableCell;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

/**
 * Created by Quentin Gangler on 09/11/2016.
 *
 */
public class ConsumptionController extends SubController implements Initializable{

    @FXML private Label titleLabel;
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private TableView<RecipeConsumption> recipesProducedTableView;
    @FXML private TableColumn<RecipeConsumption, String> recipeProduceNameTableColumn;
    @FXML private TableColumn<RecipeConsumption, Float> recipeProducedQuantityTableColumn;
    @FXML private TableView<RawMaterialConsumption> rawMaterialConsumptionTableView;
    @FXML private TableColumn<RawMaterialConsumption, String> rawMaterialNameTableColumn;
    @FXML private TableColumn<RawMaterialConsumption, Float> rawMaterialStockStartTableColumn;
    @FXML private TableColumn<RawMaterialConsumption, Float> rawMaterialEndStockTableColumn;
    @FXML private TableView<StockModification> stockModificationTableView;
    @FXML private TableColumn<StockModification, String> stockMaterialNameTableColumn;
    @FXML private TableColumn<StockModification, Float> stockChangeValueTableColumn;
    @FXML private TableColumn<StockModification, String> stockChangeDescriptionTableColumn;

    private LocalDate startDate;
    private LocalDate endDate;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @Override
    public void afterInitialize() {
        initializeDatePickers();
        initializeRecipesProducedTableView();
        initializeRawMaterialConsumptionTableView();
        initializeStockModificationTableView();
        update();
    }

    @Override
    public void update() {
        updateTitle();
        updateTableColumnTitle();
        updateRecipesProducedTable();
        updateRawMaterialConsumptionTable();
        updateStockModificationTable();
    }

    private void initializeDatePickers() {
        startDate = LocalDate.now().minusDays(1);
        endDate = LocalDate.now();
        startDatePicker.setValue(startDate);
        startDatePicker.setOnAction(event -> {
            startDate = startDatePicker.getValue();
            if(startDate.isAfter(endDate)) {
                endDate = startDate;
                endDatePicker.setValue(endDate);
            }
            update();
        });
        endDatePicker.setValue(endDate);
        endDatePicker.setDayCellFactory(value -> new DateCell(){
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);

                if (item.isBefore(startDatePicker.getValue())) {
                    setDisable(true);
                    setStyle("-fx-background-color: #ffc0cb;");
                }
            }
        });
        endDatePicker.setOnAction(event -> {
            endDate = endDatePicker.getValue();
            update();
        });
    }

    private String displayDate(LocalDate date) {
        LocalDate now = LocalDate.now();
        if (date.isEqual(now)) {
            return "aujourd'hui";
        } else if (now.minusDays(1).isEqual(date)) {
            return "hier";
        } else if (now.plusDays(1).isEqual(date)) {
            return "demain";
        } else {
            return "le " + DateTimeFormatter.ofPattern("d MMMM yyyy").format(date);
        }
    }

    private void updateTitle() {
        String text = "Rapport de consommation ";
        if (startDate.isEqual(endDate)) {
            text += "pour " + displayDate(startDate);
        } else {
            text += "entre " + displayDate(startDate) + " et " + displayDate(endDate);
        }
        titleLabel.setText(text);
    }

    private void updateTableColumnTitle() {
        String startText = "Stock " + displayDate(startDate);
        String endText = "Stock " + displayDate(endDate);
        rawMaterialStockStartTableColumn.setText(startText);
        rawMaterialEndStockTableColumn.setText(endText);

    }

    private void updateRecipesProducedTable() {
        recipesProducedTableView.setItems(data.getRecipeConsumptionByDates(startDate, endDate));
        recipesProducedTableView.refresh();
    }

    private void updateRawMaterialConsumptionTable() {
        rawMaterialConsumptionTableView.setItems(data.getRawMaterialConsumptionByDates(startDate, endDate));
        rawMaterialConsumptionTableView.refresh();
    }

    private void updateStockModificationTable() {
        stockModificationTableView.setItems(data.getStockModificationsByDates(startDate, endDate));
        stockModificationTableView.refresh();
    }

    private void initializeRecipesProducedTableView() {
        recipeProduceNameTableColumn.setCellValueFactory(cellData -> cellData.getValue().recipeNameProperty());
        recipeProduceNameTableColumn.setCellFactory(RecipeTableCell::new);
        recipeProducedQuantityTableColumn.setCellValueFactory(cellData -> cellData.getValue().consumptionProperty().asObject());
        recipeProducedQuantityTableColumn.setCellFactory(param -> new FloatWithMultiplierTableCell());
    }

    private void initializeRawMaterialConsumptionTableView() {
        rawMaterialNameTableColumn.setCellValueFactory(cellData -> cellData.getValue().rawMaterialNameProperty());
        rawMaterialStockStartTableColumn.setCellValueFactory(cellData -> cellData.getValue().stockAtDayXProperty().asObject());
        rawMaterialStockStartTableColumn.setCellFactory(FloatWithUnitTableCell::new);
        rawMaterialEndStockTableColumn.setCellValueFactory(cellData -> cellData.getValue().stockAtDayYProperty().asObject());
        rawMaterialEndStockTableColumn.setCellFactory(FloatWithUnitTableCell::new);
    }

    private void initializeStockModificationTableView() {
        stockMaterialNameTableColumn.setCellValueFactory(cellData -> cellData.getValue().getRawMaterial().nameProperty());
        stockChangeValueTableColumn.setCellValueFactory(cellData -> cellData.getValue().quantityProperty().asObject());
        stockChangeValueTableColumn.setCellFactory(FloatWithUnitNegativeTableCell::new);
        stockChangeDescriptionTableColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
    }
}
