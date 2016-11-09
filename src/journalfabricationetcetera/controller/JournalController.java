package journalfabricationetcetera.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.util.converter.DefaultStringConverter;
import journalfabricationetcetera.model.Journal;
import journalfabricationetcetera.model.RawMaterial;
import journalfabricationetcetera.model.Recipe;
import journalfabricationetcetera.model.StockModification;
import journalfabricationetcetera.model.tablecell.FloatWithMultiplierTableCell;
import journalfabricationetcetera.model.tablecell.FloatWithUnitNegativeTableCell;
import journalfabricationetcetera.model.tablecell.RecipeTableCell;
import org.apache.commons.lang3.text.WordUtils;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

/**
 * Created by Quentin Gangler on 03/11/2016.
 *
 */
public class JournalController extends SubController implements Initializable {

    @FXML private DatePicker datePicker;
    @FXML private Label dateLabel;

    @FXML private TableView<Recipe> recipesTableView;
    @FXML private TableColumn<Recipe, String> recipeNameTableColumn;

    @FXML private TableView<Journal> recipesProducedTableView;
    @FXML private TableColumn<Journal, String> recipeProducedNameTableColumn;
    @FXML private TableColumn<Journal, Float> recipeProducedMultiplierTableColumn;

    @FXML private TableView<RawMaterial> rawMaterialsTableView;
    @FXML private TableColumn<RawMaterial, String> rawMaterialNameTableColumn;

    @FXML private TableView<StockModification> rawMaterialsChangedTableView;
    @FXML private TableColumn<StockModification, String> rawMaterialChangedNameTableColumn;
    @FXML private TableColumn<StockModification, Float> rawMaterialChangedChangeTableColumn;
    @FXML private TableColumn<StockModification, String> rawMaterialChangedDescriptionTableColumn;

    private LocalDate currentDate;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @Override
    public void afterInitialize() {
        currentDate = LocalDate.now();
        dateLabel.setText("Aujourd'hui");
        datePicker.setValue(currentDate);
        datePicker.setOnAction(event -> {
            currentDate = datePicker.getValue();
            updateDate();
        });
        initializeRecipesTable();
        initializeRecipesProducedTable();
        initializeRawMaterialsTable();
        initializeRawMaterialsChangedTable();
    }

    @Override
    public void update() {
        updateRecipesTable();
        updateRecipesProducedTable();
        updateRawMaterialsTable();
        updateRawMaterialsChangedTable();
    }

    private void updateDate() {
        LocalDate now = LocalDate.now();
        if (currentDate.isEqual(now)) {
            dateLabel.setText("Aujourd'hui");
        } else if (now.minusDays(1).isEqual(currentDate)) {
            dateLabel.setText("Hier");
        } else if (now.plusDays(1).isEqual(currentDate)) {
            dateLabel.setText("Demain");
        } else {
            dateLabel.setText(WordUtils.capitalize(DateTimeFormatter.ofPattern("d MMMM yyyy").format(currentDate)));
        }
        updateRecipesProducedTable();
        updateRawMaterialsChangedTable();
    }

    private void updateRecipesTable() {
        recipesTableView.refresh();
    }

    private void updateRecipesProducedTable() {
        recipesProducedTableView.setItems(data.getJournalByDate(currentDate));
        recipesProducedTableView.refresh();
    }

    private void updateRawMaterialsTable() {
        rawMaterialsTableView.refresh();
    }

    private void updateRawMaterialsChangedTable() {
        rawMaterialsChangedTableView.setItems(data.getStockModificationByDate(currentDate));
        rawMaterialsChangedTableView.refresh();
    }

    private void initializeRecipesTable() {
        recipeNameTableColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        recipeNameTableColumn.setCellFactory(RecipeTableCell::new);
        recipesTableView.setRowFactory(tv -> {
            TableRow<Recipe> tableRow = new TableRow<>();
            tableRow.setOnMouseClicked(event -> {
                if(event.getClickCount() == 2 && (! tableRow.isEmpty())) {
                    Recipe data = tableRow.getItem();
                    addEntryToJournal(data);
                }
            });
            return tableRow;
        });
        recipesTableView.setItems(data.recipes);
        updateRecipesTable();
    }

    private void initializeRecipesProducedTable() {
        recipesProducedTableView.setEditable(true);
        recipeProducedNameTableColumn.setCellValueFactory(cellData -> cellData.getValue().getRecipe().nameProperty());
        recipeProducedNameTableColumn.setCellFactory(RecipeTableCell::new);
        recipeProducedMultiplierTableColumn.setCellValueFactory(cellData -> cellData.getValue().multiplierProperty().asObject());
        recipeProducedMultiplierTableColumn.setCellFactory(param -> new FloatWithMultiplierTableCell());
        recipeProducedMultiplierTableColumn.setOnEditCommit(t ->
                updateMultiplierJournal(t.getTableView().getItems().get(t.getTablePosition().getRow()),
                        t.getNewValue()));
        recipesProducedTableView.setOnKeyReleased(event -> {
            Journal selected = recipesProducedTableView.getSelectionModel().getSelectedItem();
            if (event.getCode() == KeyCode.DELETE && selected != null) {
                deleteEntryToJournal(selected);
            }
        });
        updateRecipesProducedTable();
    }

    private void initializeRawMaterialsTable() {
        rawMaterialNameTableColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        rawMaterialsTableView.setRowFactory(tv -> {
            TableRow<RawMaterial> tableRow = new TableRow<>();
            tableRow.setOnMouseClicked(event -> {
                if(event.getClickCount() == 2 && (! tableRow.isEmpty())) {
                    RawMaterial data = tableRow.getItem();
                    addEntryToStockModification(data);
                }
            });
            return tableRow;
        });
        rawMaterialsTableView.setItems(data.rawMaterials);
        updateRawMaterialsTable();
    }

    private void initializeRawMaterialsChangedTable() {
        rawMaterialsChangedTableView.setEditable(true);
        rawMaterialChangedNameTableColumn.setCellValueFactory(cellData -> cellData.getValue().getRawMaterial().nameProperty());
        rawMaterialChangedChangeTableColumn.setCellValueFactory(cellData -> cellData.getValue().quantityProperty().asObject());
        rawMaterialChangedChangeTableColumn.setCellFactory(FloatWithUnitNegativeTableCell::new);
        rawMaterialChangedChangeTableColumn.setOnEditCommit(t ->
            updateQuantityStockModification(t.getTableView().getItems().get(t.getTablePosition().getRow()),
                    t.getNewValue()));
        rawMaterialChangedDescriptionTableColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
        rawMaterialChangedDescriptionTableColumn.setCellFactory(t -> new TextFieldTableCell<>(new DefaultStringConverter()));
        rawMaterialChangedDescriptionTableColumn.setOnEditCommit(t ->
            updateDescriptionStockModification(t.getTableView().getItems().get(t.getTablePosition().getRow()),
                    t.getNewValue()));
        rawMaterialsChangedTableView.setOnKeyReleased(event -> {
            StockModification selected = rawMaterialsChangedTableView.getSelectionModel().getSelectedItem();
            if (event.getCode() == KeyCode.DELETE && selected != null) {
                deleteEntryToStockModification(selected);
            }
        });
        updateRawMaterialsChangedTable();
    }

    private void addEntryToJournal(Recipe recipe) {
        if (!data.journalByDateContainsRecipe(recipe, currentDate)) {
            if (data.getDb().insertJournal(recipe, currentDate, 1)) {
                updateRecipesProducedTable();
            }
        }
    }

    private void deleteEntryToJournal(Journal journal) {
        if (data.getDb().deleteJournal(journal)) {
            updateRecipesProducedTable();
        }
    }

    private void updateMultiplierJournal(Journal journal, float multiplier) {
        if (data.getDb().updateMultiplierJournal(journal, multiplier)) {
            updateRecipesProducedTable();
        }
    }

    private void addEntryToStockModification(RawMaterial rawMaterial) {
        if (!data.stockModificationByDateContainsRawMaterial(rawMaterial, currentDate)) {
            if (data.getDb().insertStockModification(rawMaterial, currentDate, 1)) {
                updateRawMaterialsChangedTable();
            }
        }
    }

    private void deleteEntryToStockModification(StockModification stockModification) {
        if(data.getDb().deleteStockModification(stockModification)) {
            updateRawMaterialsChangedTable();
        }
    }

    private void updateQuantityStockModification(StockModification stockModification, float quantity) {
        if (data.getDb().updateQuantityStockModification(stockModification, quantity)) {
            updateRawMaterialsChangedTable();
        }
    }

    private void updateDescriptionStockModification(StockModification stockModification, String description) {
        if (data.getDb().updateDescriptionStockModification(stockModification, description)) {
            updateRawMaterialsChangedTable();
        }
    }
}
