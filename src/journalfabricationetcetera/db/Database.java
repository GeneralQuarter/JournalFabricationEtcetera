package journalfabricationetcetera.db;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import journalfabricationetcetera.model.*;

import java.io.File;
import java.sql.*;
import java.time.LocalDate;
import java.util.Scanner;

/**
 * Created by Quentin Gangler on 21/10/2016.
 *
 */
public class Database {

    private Connection c;

    private Data data;

    public Database() {
        c = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:resources\\database.db");
            print("Opened successfully");
            if(isEmpty()){
                executeDDL();
            }
        } catch (Exception e) {
            System.err.println( e.getClass() + " : " + e.getMessage());
        }
    }

    void setData(Data data){
        this.data = data;
    }

    private boolean isEmpty() {
        try {
            Statement stm = c.createStatement();
            stm.execute("SELECT * FROM unit;");
            stm.close();
            print("is not empty");
            return false;
        } catch (SQLException e) {
            System.err.println(e.getClass() + " : " + e.getMessage());
            print("is empty");
            return true;
        }
    }

    private boolean executeDDL() {
        try {
            String ddl = new Scanner(new File("resources\\ddl.sql")).useDelimiter("\\Z").next();
            Statement stm = c.createStatement();
            stm.executeUpdate(ddl);
            stm.close();
            print("ddl executed");
            return true;
        } catch (Exception e) {
            System.err.println(e.getClass() + " : " + e.getMessage());
            return false;
        }
    }

    /*private boolean dropTables() {
        try {
            String drop = new Scanner(new File("resources\\drop.sql")).useDelimiter("\\Z").next();
            Statement stm = c.createStatement();
            stm.executeUpdate(drop);
            stm.close();
            print("Dropped all tables");
            return true;
        } catch (Exception e) {
            System.err.println(e.getClass() + " : " + e.getMessage());
            return false;
        }
    }*/

    public void closeConnection() {
        try {
            c.close();
            print("Closed successfully");
        } catch (SQLException e) {
            System.err.println(e.getClass() + " : " + e.getMessage());
        }
    }

    private void print(String message) {
        System.out.println("[Database] " + message);
    }

    public boolean isRawMaterialNameUsed(String name) {
        try {
            PreparedStatement stm = c.prepareStatement("SELECT * FROM raw_material WHERE name = ?;");
            stm.setString(1, name);
            ResultSet rs = stm.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            print(e.getClass() + " : " + e.getMessage());
            return true;
        }
    }

    public boolean isRecipeNameUsed(String name) {
        try {
            PreparedStatement stm = c.prepareStatement("SELECT * FROM recipe WHERE name = ?;");
            stm.setString(1, name);
            ResultSet rs = stm.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            print(e.getClass() + " : " + e.getMessage());
            return true;
        }
    }

    private int getLastInsertedId(PreparedStatement stm) throws SQLException {
        ResultSet rs = stm.getGeneratedKeys();
        if(rs.next()){
            return rs.getInt(1);
        }else {
            throw new SQLException("Impossible de récupérer l'id");
        }
    }

    ObservableList<StockView> selectStockView() {
        ObservableList<StockView> result = FXCollections.observableArrayList();
        try {
            Statement stm = c.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM stock_view;");
            while (rs.next()) {
                result.add(new StockView(
                                data.findRawMaterial(rs.getInt("rawMaterialID")),
                                data.findStock(rs.getInt("stockID")),
                                data.findUnit(rs.getInt("unitID")),
                                rs.getString("name"),
                                rs.getFloat("lastQuantityInStock"),
                                rs.getString("lastDateInStock"),
                                0)
                );
            }
            stm.close();
        } catch (SQLException e) {
            System.err.println(e.getClass() + " : " + e.getMessage());
        }

        return result;
    }

    ObservableList<Unit> selectAllUnit() {
        ObservableList<Unit> result = FXCollections.observableArrayList();
        try {
            String query = "SELECT * FROM unit;";
            Statement stm = c.createStatement();
            ResultSet rs = stm.executeQuery(query);
            while (rs.next()) {
                Unit newUnit = new Unit(rs.getInt("id"), rs.getString("name"), rs.getString("abr1000"), rs.getString("abr1"));
                result.add(newUnit);
            }
            stm.close();
        } catch (SQLException e) {
            System.err.println(e.getClass() + " : " + e.getMessage());
        }
        return result;
    }

    ObservableList<Recipe> selectAllRecipe() {
        ObservableList<Recipe> result = FXCollections.observableArrayList();
        try {
            String query = "SELECT * FROM recipe;";
            Statement stm = c.createStatement();
            ResultSet rs = stm.executeQuery(query);
            while (rs.next()) {
                Recipe newRecipe = new Recipe(rs.getInt("id"), rs.getString("name"));
                newRecipe.setLines(FXCollections.observableArrayList());
                result.add(newRecipe);
            }
            stm.close();
        } catch (SQLException e) {
            System.err.println(e.getClass() + " : " + e.getMessage());
        }
        return result;
    }

    void fillRecipes() {
        try {
            String query = "SELECT * FROM recipe_line;";
            Statement stm = c.createStatement();
            ResultSet rs = stm.executeQuery(query);
            while (rs.next()) {
                Recipe recipe = data.findRecipe(rs.getInt("recipeID"));
                RecipeLine newRecipeLine = new RecipeLine(recipe, data.findRawMaterial(rs.getInt("rawMaterialID")), rs.getFloat("quantity"));
                recipe.getLines().add(newRecipeLine);
            }
            stm.close();
        } catch (SQLException e) {
            System.err.println(e.getClass() + " : " + e.getMessage());
        }
    }

    ObservableList<Stock> selectAllStock() {
        ObservableList<Stock> result = FXCollections.observableArrayList();
        try {
            String query = "SELECT * FROM stock;";
            Statement stm = c.createStatement();
            ResultSet rs = stm.executeQuery(query);
            while (rs.next()) {
                Stock newStock = new Stock(
                        rs.getInt("id"),
                        data.findRawMaterial(rs.getInt("rawMaterialID")),
                        LocalDate.parse(rs.getString("date")),
                        rs.getFloat("quantity"));
                result.add(newStock);
            }
            stm.close();
        } catch (SQLException e) {
            System.err.println(e.getClass() + " : " + e.getMessage());
        }
        return result;
    }

    ObservableList<RawMaterial> selectAllRawMaterial() {
        ObservableList<RawMaterial> result = FXCollections.observableArrayList();
        try {
            Statement stm = c.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM raw_material;");
            while (rs.next()) {
                RawMaterial newRaw = new RawMaterial(rs.getInt("id"), rs.getString("name"), data.findUnit(rs.getInt("unitID")));
                result.add(newRaw);
            }
            stm.close();
        } catch (SQLException e) {
            System.err.println(e.getClass() + " : " + e.getMessage());
        }
        return result;
    }

    ObservableList<Journal> selectAllJournalByDate(String date) {
        ObservableList<Journal> result = FXCollections.observableArrayList();
        try {
            PreparedStatement stm = c.prepareStatement("SELECT * FROM journal WHERE date = ?;");
            stm.setString(1, date);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Journal newJournal = new Journal(
                        rs.getInt("id"),
                        data.findRecipe(rs.getInt("recipeID")),
                        LocalDate.parse(rs.getString("date")),
                        rs.getFloat("multiplier")
                );
                result.add(newJournal);
            }
            stm.close();
        } catch (SQLException e) {
            System.err.println(e.getClass() + " : " + e.getMessage());
        }
        return result;
    }

    ObservableList<StockModification> selectAllStockModificationByDate(String date) {
        ObservableList<StockModification> result = FXCollections.observableArrayList();
        try {
            PreparedStatement stm = c.prepareStatement("SELECT * FROM stock_modification WHERE date = ?;");
            stm.setString(1, date);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                StockModification newStockModification = new StockModification(
                        rs.getInt("id"),
                        data.findRawMaterial(rs.getInt("rawMaterialID")),
                        LocalDate.parse(rs.getString("date")),
                        rs.getFloat("quantity"),
                        rs.getString("description")
                );
                result.add(newStockModification);
            }
            stm.close();
        } catch (SQLException e) {
            System.err.println(e.getClass() + " : " + e.getMessage());
        }
        return result;
    }

    public Stock insertInStock(RawMaterial rm, float quantity, LocalDate date) {
        try {
            PreparedStatement stm = c.prepareStatement("INSERT INTO stock(rawMaterialID, date, quantity) VALUES (?, ?, ?)");
            stm.setInt(1, rm.getId());
            stm.setString(2, date.toString());
            stm.setFloat(3, quantity);
            stm.executeUpdate();
            int id = getLastInsertedId(stm);
            Stock stock = new Stock(id, rm, date, quantity);
            data.stocks.add(stock);
            return stock;
        } catch (SQLException e) {
            System.err.println(e.getClass() + " : " + e.getMessage());
        }
        return null;
    }

    public boolean insertRecipe(Recipe recipe) {
        try {
            PreparedStatement stm = c.prepareStatement("INSERT INTO recipe(name) VALUES (?);");
            stm.setString(1, recipe.getName());
            stm.executeUpdate();
            recipe.setId(getLastInsertedId(stm));
            recipe.setLines(insertMultipleRecipeLines(recipe));
            data.recipes.add(recipe);
            return true;
        } catch (SQLException e) {
            System.err.println(e.getClass() + " : " + e.getMessage());
        }
        return false;
    }

    private ObservableList<RecipeLine> insertMultipleRecipeLines(Recipe recipe) {
        ObservableList<RecipeLine> result = FXCollections.observableArrayList();
        try {
            for (RecipeLine line : recipe.getLines()) {
                PreparedStatement stm = c.prepareStatement("INSERT INTO recipe_line(recipeID, rawMaterialID, quantity) VALUES (?, ?, ?)");
                stm.setInt(1, recipe.getId());
                stm.setInt(2, line.getRawMaterial().getId());
                stm.setFloat(3, line.getQuantity());
                stm.executeUpdate();

                line.setRecipe(recipe);
                result.add(line);
            }
        } catch (SQLException e) {
            System.err.println(e.getClass() + " : " + e.getMessage());
        }
        return result;
    }

    public boolean insertRawMaterialAndStock(String name, Unit unit, float quantity, LocalDate date) {
        try {
            PreparedStatement stm = c.prepareStatement("INSERT INTO raw_material(name, unitID) VALUES (?, ?)");
            stm.setString(1, name);
            stm.setInt(2, unit.getId());
            stm.executeUpdate();
            int id = getLastInsertedId(stm);
            RawMaterial rm = new RawMaterial(id, name, unit);
            data.rawMaterials.add(rm);
            Stock stock = insertInStock(rm, quantity, date);
            StockView stockView = new StockView(rm, stock, unit, name, quantity, date.toString(), 0);
            data.stockViews.add(stockView);
            return true;
        } catch (SQLException e) {
            System.err.println(e.getClass() + " : " + e.getMessage());
        }
        return false;
    }

    public boolean insertJournal(Recipe recipe, LocalDate date, float multiplier) {
        try {
            PreparedStatement stm = c.prepareStatement("INSERT INTO journal(recipeID, date, multiplier) VALUES (?, ?, ?)");
            stm.setInt(1, recipe.getId());
            stm.setString(2, date.toString());
            stm.setFloat(3, multiplier);
            stm.executeUpdate();
            int id = getLastInsertedId(stm);
            Journal journal = new Journal(id, recipe, date, multiplier);
            data.getJournalByDate(date).add(journal);
            return true;
        } catch (SQLException e) {
            System.err.println(e.getClass() + " : " + e.getMessage());
        }
        return false;
    }

    public boolean deleteJournal(Journal journal) {
        try {
            PreparedStatement stm = c.prepareStatement("DELETE FROM journal WHERE id = ?;");
            stm.setInt(1, journal.getId());
            stm.executeUpdate();
            data.getJournalByDate(journal.getDate()).remove(journal);
            return true;
        } catch (SQLException e) {
            System.err.println(e.getClass() + " : " + e.getMessage());
        }
        return false;
    }

    public boolean updateMultiplierJournal(Journal journal, float multiplier) {
        try {
            PreparedStatement stm = c.prepareStatement("UPDATE journal SET multiplier = ? WHERE id = ?;");
            stm.setFloat(1, multiplier);
            stm.setInt(2, journal.getId());
            stm.executeUpdate();
            data.findJournal(journal.getId(), journal.getDate()).setMultiplier(multiplier);
            return true;
        } catch (SQLException e) {
            System.err.println(e.getClass() + " : " + e.getMessage());
        }
        return false;
    }

    public boolean insertStockModification(RawMaterial rawMaterial, LocalDate date, float quantity) {
        try {
            PreparedStatement stm = c.prepareStatement("INSERT INTO stock_modification(rawMaterialID, date, quantity) VALUES (?, ?, ?)");
            stm.setInt(1, rawMaterial.getId());
            stm.setString(2, date.toString());
            stm.setFloat(3, quantity);
            stm.executeUpdate();
            int id = getLastInsertedId(stm);
            StockModification stockModification = new StockModification(id, rawMaterial, date, quantity, "");
            data.getStockModificationByDate(date).add(stockModification);
            return true;
        } catch (SQLException e) {
            System.err.println(e.getClass() + " : " + e.getMessage());
        }
        return false;
    }

    public boolean updateQuantityStockModification(StockModification stockModification, float quantity) {
        try {
            PreparedStatement stm = c.prepareStatement("UPDATE stock_modification SET quantity = ? WHERE id = ?;");
            stm.setFloat(1, quantity);
            stm.setInt(2, stockModification.getId());
            stm.executeUpdate();
            data.findStockModification(stockModification.getId(), stockModification.getDate()).setQuantity(quantity);
            return true;
        } catch (SQLException e) {
            System.err.println(e.getClass() + " : " + e.getMessage());
        }
        return false;
    }

    public boolean updateDescriptionStockModification(StockModification stockModification, String description) {
        try {
            PreparedStatement stm = c.prepareStatement("UPDATE stock_modification SET description = ? WHERE id = ?;");
            stm.setString(1, description);
            stm.setInt(2, stockModification.getId());
            stm.executeUpdate();
            data.findStockModification(stockModification.getId(), stockModification.getDate()).setDescription(description);
            return true;
        } catch (SQLException e) {
            System.err.println(e.getClass() + " : " + e.getMessage());
        }
        return false;
    }

    public boolean deleteStockModification(StockModification stockModification) {
        try {
            PreparedStatement stm = c.prepareStatement("DELETE FROM stock_modification WHERE id = ?;");
            stm.setInt(1, stockModification.getId());
            stm.executeUpdate();
            data.getStockModificationByDate(stockModification.getDate()).remove(stockModification);
            return true;
        } catch (SQLException e) {
            System.err.println(e.getClass() + " : " + e.getMessage());
        }
        return false;
    }
}
