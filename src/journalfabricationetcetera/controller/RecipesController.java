package journalfabricationetcetera.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import journalfabricationetcetera.Utils;
import journalfabricationetcetera.model.RawMaterial;
import journalfabricationetcetera.model.Recipe;
import journalfabricationetcetera.model.RecipeLine;
import journalfabricationetcetera.model.tablecell.FloatWithUnitTableCell;
import journalfabricationetcetera.model.tablecell.RecipeTableCell;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Quentin Gangler on 22/10/2016.
 *
 */
public class RecipesController extends SubController implements Initializable {

    @FXML private TableView<Recipe> recipeTableView;
    @FXML private TableColumn<Recipe, String> recipeNameTableColumn;
    @FXML private TextField recipeNameTextField;
    @FXML private TableView<RecipeLine> recipeLinesTableView;
    @FXML private TableColumn<RecipeLine, String> recipeLineNameTableColumn;
    @FXML private TableColumn<RecipeLine, Float> recipeLineQuantityTableColumn;
    @FXML private TableView<RawMaterial> rawMaterialTableView;
    @FXML private TableColumn<RawMaterial, String> rawMaterialTableColumn;

    private ObservableList<RecipeLine> recipeLines;
    private Recipe currentRecipe;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @Override
    public void afterInitialize() {
        initializeRawMaterialTableView();
        initializeRecipeLinesTableView();
        initializeRecipeTableView();
    }

    @Override
    public void update() {
        updateRawMaterialTableView();
        updateRecipeTableView();
    }

    private void initializeRecipeLinesTableView() {
        recipeLinesTableView.setEditable(true);
        recipeLinesTableView.setOnKeyReleased(event -> {
            RecipeLine selected = recipeLinesTableView.getSelectionModel().getSelectedItem();
            if (event.getCode() == KeyCode.DELETE && selected != null) {
                recipeLines.removeAll(selected);
                updateRecipeLinesTableView();
            }
        });
        recipeLineNameTableColumn.setCellValueFactory(cellData -> cellData.getValue().getRawMaterial().nameProperty());
        recipeLineQuantityTableColumn.setCellValueFactory(cellData -> cellData.getValue().quantityProperty().asObject());
        recipeLineQuantityTableColumn.setCellFactory(FloatWithUnitTableCell::new);
        recipeLineQuantityTableColumn.setOnEditCommit(t -> t.getTableView().getItems().get(t.getTablePosition().getRow()).setQuantity(t.getNewValue()));
        recipeLines = FXCollections.observableArrayList();
        recipeLinesTableView.setItems(recipeLines);
        updateRecipeLinesTableView();
    }

    private void initializeRawMaterialTableView() {
        rawMaterialTableColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        rawMaterialTableView.setRowFactory(tv -> {
            TableRow<RawMaterial> tableRow = new TableRow<>();
            tableRow.setOnMouseClicked(event -> {
                if(event.getClickCount() == 2 && (! tableRow.isEmpty())) {
                    RawMaterial data = tableRow.getItem();
                    addRawMaterialToCurrentRecipe(data);
                }
            });
            return tableRow;
        });
        rawMaterialTableView.setItems(data.rawMaterials);
        updateRawMaterialTableView();
    }

    private void initializeRecipeTableView() {
        recipeNameTableColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        recipeNameTableColumn.setCellFactory(RecipeTableCell::new);
        recipeTableView.setItems(data.recipes);
        updateRecipeTableView();
    }

    private void updateRecipeLinesTableView() {
        recipeLinesTableView.refresh();
    }

    private void updateRawMaterialTableView() { rawMaterialTableView.refresh(); }

    private void updateRecipeTableView() {
        recipeTableView.refresh();
    }

    private void addRawMaterialToCurrentRecipe(RawMaterial rm) {
        if(currentRecipe == null) {
            currentRecipe = new Recipe(0, "");
        }
        if(!Utils.containsRawMaterial(recipeLines, rm)) {
            recipeLines.add(new RecipeLine(currentRecipe, rm, 0));
        }
        updateRecipeLinesTableView();
    }

    private void clearCurrentRecipe() {
        currentRecipe = null;
        recipeLines.clear();
        recipeNameTextField.setText(null);
        updateRecipeLinesTableView();
        updateRecipeTableView();
    }

    @FXML protected void handleSaveRecipe() {
        String recipeName = recipeNameTextField.getText();
        if (recipeName != null && !recipeName.isEmpty()) {
            if (!recipeLines.isEmpty()) {
                if (!data.getDb().isRecipeNameUsed(recipeName)) {
                    currentRecipe.setName(recipeName);
                    currentRecipe.setLines(recipeLines);
                    if (data.getDb().insertRecipe(currentRecipe)) {
                        clearCurrentRecipe();
                    }
                } else {
                    Utils.showAlert(Alert.AlertType.WARNING, "Nom de recette déjà utilisé", "La recette \"" + recipeName + "\" est déjà en base de donnée");
                }
            } else {
                Utils.showAlert(Alert.AlertType.WARNING, "Aucun ingrédients", "Veuillez ajouter des ingrédients à la recette");
            }
        } else {
            Utils.showAlert(Alert.AlertType.WARNING, "Nom de recette vide", "Le nom de la recette est vide");
        }
    }
}
