package journalfabricationetcetera.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import journalfabricationetcetera.Utils;
import journalfabricationetcetera.db.Database;
import journalfabricationetcetera.model.RawMaterial;
import journalfabricationetcetera.model.Stock;
import journalfabricationetcetera.model.StockView;
import journalfabricationetcetera.model.Unit;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

/**
 * Created by Quentin Gangler on 22/10/2016.
 *
 */
public class InventoryController implements Initializable {

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

    private Database db;

    private ObservableList<Unit> units;
    private ObservableList<StockView> stockView;
    private StockView rowSelected;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setDb(Database db) {
        this.db = db;
    }

    public void afterInitialize() {
        initializeUnitChoiceBox();
        stockDatePicker.setValue(LocalDate.now());
        stockQuantitytextField.setText("0");
        newDateInStockDatePicker.setValue(LocalDate.now());
        newQuantityInStockTextField.setText("0");
        initializeStockTable();
    }

    private void updateTable(){
        stockTableView.refresh();
    }

    private void initializeStockTable() {
        nameTableColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        lastQuantityInStockTableColumn.setCellValueFactory(cellData -> cellData.getValue().lastQuantityInStockProperty().asObject());
        lastDateInStockTableColumn.setCellValueFactory(cellData -> cellData.getValue().lastDateInStockProperty());
        stockAvailableTableColumn.setCellValueFactory(cellData -> cellData.getValue().stockAvailableProperty().asObject());

        DateTimeFormatter myDateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        lastDateInStockTableColumn.setCellFactory(param -> {
            return new TableCell<StockView, LocalDate>() {
                @Override
                protected void updateItem(LocalDate item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(myDateFormatter.format(item));
                    }
                }
            };
        });
        lastQuantityInStockTableColumn.setCellFactory(param -> {
            return new TableCell<StockView, Float>() {
                @Override
                protected void updateItem(Float item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty) {
                        setText("");
                        setStyle("");
                    } else {
                        int currentIndex = indexProperty().getValue() < 0 ? 0 : indexProperty().getValue();
                        //TODO Get unit and display 10 kg instead of 10.0
                        int unitID = param.getTableView().getItems().get(currentIndex).getUnitID();
                        setText(Utils.displayFloatWithUnit(item, units.get(unitID-1)));
                    }
                }
            };
        });
        stockTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                rowSelected = newSelection;
                rawMaterialNameDisplayTextField.setText(rowSelected.getName());
                newStockQuantityLabel.setText("Nouvelle quantité (" + units.get(rowSelected.getUnitID()-1).getAbr1000() + ")");
            }
        });
        stockView = db.selectStockView();
        stockTableView.setItems(stockView);
        updateTable();
    }

    private void initializeUnitChoiceBox() {
        units = db.selectAllUnit();
        rawMaterialUnitChoiceBox.setItems(units);
        rawMaterialUnitChoiceBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                stockUnitLabel.setText("Quantité en stock (" + units.get(newValue.intValue()).getAbr1000() + ")");
            }
        });
        rawMaterialUnitChoiceBox.getSelectionModel().selectFirst();
    }

    @FXML protected void handleSubmitAddRawMaterialButtonAction(ActionEvent event) {
        String name = rawMaterialNametextField.getText();
        Unit unit = (Unit) rawMaterialUnitChoiceBox.getValue();
        float quantity = 0.0f;
        LocalDate date = stockDatePicker.getValue();

        if (name.isEmpty()) {
            Utils.showAlert(Alert.AlertType.WARNING, "Le nom indiqué est vide", "Le nom de la matière est vide");
        } else if (db.isRawMaterialNameUsed(name)) {
            Utils.showAlert(Alert.AlertType.WARNING, "Le nom indiqué est déjà pris", "La matière est déjà dans la base de données");
        } else {
            quantity = Utils.validateQuantity(stockQuantitytextField.getText());
            if(quantity != -1) {
                StockView stockViewRow = db.insertRawMaterialAndStock(name, unit, quantity, date);
                stockView.add(stockViewRow);
                updateTable();
            }
        }
    }

    @FXML protected void handleSubmitModifiyStockButtonAction(ActionEvent actionEvent) {
        float quantity = 0.0f;

        if (rowSelected == null) {
            Utils.showAlert(Alert.AlertType.WARNING, "Aucune matière sélectionnée", "Veuillez sélectionner une matière dans la table");
        } else {
            quantity = Utils.validateQuantity(newQuantityInStockTextField.getText());
            if(quantity != -1) {
                RawMaterial rm = new RawMaterial(rowSelected.getRawMaterialID(), rowSelected.getName(), units.get(rowSelected.getUnitID()-1));
                Stock stock = db.insertInStock(rm, quantity, newDateInStockDatePicker.getValue());
                rowSelected.setLastDateInStock(stock.getDate());
                rowSelected.setLastQuantityInStock(stock.getQuantity());
                updateTable();
            }
        }
    }


}
