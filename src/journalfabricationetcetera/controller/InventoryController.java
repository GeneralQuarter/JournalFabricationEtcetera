package journalfabricationetcetera.controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import journalfabricationetcetera.Utils;
import journalfabricationetcetera.model.RawMaterial;
import journalfabricationetcetera.model.Stock;
import journalfabricationetcetera.model.StockView;
import journalfabricationetcetera.model.Unit;
import journalfabricationetcetera.model.tablecell.DateTableCell;
import journalfabricationetcetera.model.tablecell.FloatWithUnitTableCell;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

/**
 * Created by Quentin Gangler on 22/10/2016.
 *
 */
public class InventoryController extends SubController implements Initializable{

    @FXML private DatePicker newDateInStockDatePicker;
    @FXML private TextField newQuantityInStockTextField;
    @FXML private TextField rawMaterialNameDisplayTextField;
    @FXML private Label stockUnitLabel;
    @FXML private Label newStockQuantityLabel;
    @FXML private TextField stockQuantitytextField;
    @FXML private ChoiceBox<Unit> rawMaterialUnitChoiceBox;
    @FXML private TextField rawMaterialNametextField;
    @FXML private DatePicker stockDatePicker;
    @FXML private TableView<StockView> stockTableView;
    @FXML private TableColumn<StockView, Float> lastQuantityInStockTableColumn;
    @FXML private TableColumn<StockView, LocalDate> lastDateInStockTableColumn;
    @FXML private TableColumn<StockView, Float> stockAvailableTableColumn;
    @FXML private TableColumn<StockView, String> nameTableColumn;

    private StockView rowSelected;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @Override
    public void afterInitialize() {
        initializeUnitChoiceBox();
        stockDatePicker.setValue(LocalDate.now());
        stockQuantitytextField.setText("0");
        newDateInStockDatePicker.setValue(LocalDate.now());
        newQuantityInStockTextField.setText("0");
        initializeStockTable();
    }

    @Override
    public void update() {
        updateTable();
    }

    private void updateTable(){
        stockTableView.refresh();
    }

    private void initializeStockTable() {
        nameTableColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        lastQuantityInStockTableColumn.setCellValueFactory(cellData -> cellData.getValue().lastQuantityInStockProperty().asObject());
        lastDateInStockTableColumn.setCellValueFactory(cellData -> cellData.getValue().lastDateInStockProperty());
        stockAvailableTableColumn.setCellValueFactory(cellData -> cellData.getValue().stockAvailableProperty().asObject());
        lastDateInStockTableColumn.setCellFactory(param -> new DateTableCell<>());
        lastQuantityInStockTableColumn.setCellFactory(FloatWithUnitTableCell::new);
        stockTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                rowSelected = newSelection;
                rawMaterialNameDisplayTextField.setText(rowSelected.getName());
                newStockQuantityLabel.setText("Nouvelle quantité (" + rowSelected.getUnit().getAbr1000() + ")");
            }
        });
        stockTableView.setItems(data.stockViews);
        updateTable();
    }

    private void initializeUnitChoiceBox() {
        rawMaterialUnitChoiceBox.setItems(data.units);
        rawMaterialUnitChoiceBox.getSelectionModel().selectedIndexProperty().addListener(
                (observable, oldValue, newValue) ->
                        stockUnitLabel.setText(
                                "Quantité en stock (" + data.units.get(newValue.intValue()).getAbr1000() + ")"
                        ));
        rawMaterialUnitChoiceBox.getSelectionModel().selectFirst();
    }

    @FXML protected void handleSubmitAddRawMaterialButtonAction() {
        String name = rawMaterialNametextField.getText();
        Unit unit = rawMaterialUnitChoiceBox.getValue();
        float quantity;
        LocalDate date = stockDatePicker.getValue();

        if (name.isEmpty()) {
            Utils.showAlert(Alert.AlertType.WARNING, "Le nom indiqué est vide", "Le nom de la matière est vide");
        } else if (data.getDb().isRawMaterialNameUsed(name)) {
            Utils.showAlert(Alert.AlertType.WARNING, "Le nom indiqué est déjà pris", "La matière est déjà dans la base de données");
        } else {
            quantity = Utils.validateFloatNumber(stockQuantitytextField.getText());
            if(quantity != -1) {
                if(data.getDb().insertRawMaterialAndStock(name, unit, quantity, date)) {
                    updateTable();
                }
            }
        }
    }

    @FXML protected void handleSubmitModifiyStockButtonAction() {
        float quantity;

        if (rowSelected == null) {
            Utils.showAlert(Alert.AlertType.WARNING, "Aucune matière sélectionnée", "Veuillez sélectionner une matière dans la table");
        } else {
            quantity = Utils.validateFloatNumber(newQuantityInStockTextField.getText());
            if(quantity != -1) {
                Stock stock = data.getDb().insertInStock(rowSelected.getRawMaterial(), quantity, newDateInStockDatePicker.getValue());
                rowSelected.setLastDateInStock(stock.getDate());
                rowSelected.setLastQuantityInStock(stock.getQuantity());
                updateTable();
            }
        }
    }


}
