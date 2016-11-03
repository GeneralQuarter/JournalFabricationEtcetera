package journalfabricationetcetera.db;

import javafx.collections.ObservableList;
import journalfabricationetcetera.Utils;
import journalfabricationetcetera.model.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Quentin Gangler on 02/11/2016.
 *
 */
public class Data {
    private Database db;

    public ObservableList<Unit> units;
    public ObservableList<RawMaterial> rawMaterials;
    public ObservableList<Recipe> recipes;
    ObservableList<Stock> stocks;
    public ObservableList<StockView> stockViews;
    private Map<String, ObservableList<Journal>> journalsByDate;
    private Map<String, ObservableList<StockModification>> stockModificationsByDate;

    public Data(Database db) {
        this.db = db;
        this.db.setData(this);
        units = db.selectAllUnit();
        rawMaterials = db.selectAllRawMaterial();
        recipes = db.selectAllRecipe();
        db.fillRecipes();
        stocks = db.selectAllStock();
        stockViews = db.selectStockView();
        journalsByDate = new HashMap<>();
        String now = LocalDate.now().toString();
        journalsByDate.put(now, db.selectAllJournalByDate(now));
        stockModificationsByDate = new HashMap<>();
        stockModificationsByDate.put(now, db.selectAllStockModificationByDate(now));
    }

    public Database getDb() {
        return db;
    }

    private ObjectWithId findWithId(ObservableList<? extends ObjectWithId> data, int id) {
        for (ObjectWithId obj : data) {
            if (obj.getId() == id) {
                return obj;
            } else if (obj.getId() == 0) {
                return null;
            }
        }
        return null;
    }

    Unit findUnit(int id) {
        return (Unit) findWithId(units, id);
    }

    RawMaterial findRawMaterial(int id) {
        return (RawMaterial) findWithId(rawMaterials, id);
    }

    Stock findStock(int id) {
        return (Stock) findWithId(stocks, id);
    }

    Recipe findRecipe(int id) {
        return (Recipe) findWithId(recipes, id);
    }

    public ObservableList<Journal> getJournalByDate(LocalDate date) {
        String dateStr = date.toString();
        if (!journalsByDate.containsKey(dateStr)) {
            journalsByDate.put(dateStr, db.selectAllJournalByDate(dateStr));
        }
        return journalsByDate.get(dateStr);
    }

    public ObservableList<StockModification> getStockModificationByDate(LocalDate date) {
        String dateStr = date.toString();
        if (!stockModificationsByDate.containsKey(dateStr)) {
            stockModificationsByDate.put(dateStr, db.selectAllStockModificationByDate(dateStr));
        }
        return stockModificationsByDate.get(dateStr);
    }

    Journal findJournal(int id, LocalDate date) {
        return (Journal) findWithId(getJournalByDate(date), id);
    }

    StockModification findStockModification(int id, LocalDate date) {
        return (StockModification) findWithId(getStockModificationByDate(date), id);
    }

    public boolean journalByDateContainsRecipe(Recipe recipe, LocalDate date) {
        for (Journal journal : getJournalByDate(date)) {
            if(journal.getRecipe().getId() == recipe.getId()) {
                return true;
            }
        }
        return false;
    }

    public boolean stockModificationByDateContainsRawMaterial(RawMaterial rawMaterial, LocalDate date) {
        for (StockModification stockModification : getStockModificationByDate(date)) {
            if(stockModification.getRawMaterial().getId() == rawMaterial.getId()) {
                return true;
            }
        }
        return false;
    }
}
