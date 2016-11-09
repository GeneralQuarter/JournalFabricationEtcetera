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
        Statement stm = null;
        try {
            String ddl = new Scanner(new File("resources\\ddl.sql")).useDelimiter("\\Z").next();
            stm = c.createStatement();
            stm.executeUpdate(ddl);
            print("ddl executed");
            return true;
        } catch (Exception e) {
            System.err.println(e.getClass() + " : " + e.getMessage());
            return false;
        } finally {
            try {
                if (stm != null) {
                    stm.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
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

    private void closeStatement(Statement stm) {
        try {
            if(stm != null) {
                stm.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void closeResultSet(ResultSet rs) {
        try {
            if(rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void print(String message) {
        System.out.println("[Database] " + message);
    }

    public boolean isRawMaterialNameUsed(String name) {
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            stm = c.prepareStatement("SELECT * FROM raw_material WHERE name = ?;");
            stm.setString(1, name);
            rs = stm.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            print(e.getClass() + " : " + e.getMessage());
            return true;
        } finally {
            closeResultSet(rs);
            closeStatement(stm);
        }
    }

    public boolean isRecipeNameUsed(String name) {
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            stm = c.prepareStatement("SELECT * FROM recipe WHERE name = ?;");
            stm.setString(1, name);
            rs = stm.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            print(e.getClass() + " : " + e.getMessage());
            return true;
        } finally {
            closeResultSet(rs);
            closeStatement(stm);
        }
    }

    private int getLastInsertedId(PreparedStatement stm) throws SQLException{
        ResultSet rs = null;
        try {
            rs = stm.getGeneratedKeys();
            if(rs.next()){
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResultSet(rs);
        }
        throw new SQLException("Impossible de récupérer l'id");
    }

    ObservableList<StockView> selectStockView() {
        ObservableList<StockView> result = FXCollections.observableArrayList();
        Statement stm = null;
        ResultSet rs = null;
        try {
            stm = c.createStatement();
            rs = stm.executeQuery("SELECT * FROM stock_view;");
            while (rs.next()) {
                result.add(new StockView(
                                data.findRawMaterial(rs.getInt("rawMaterialID")),
                                data.findStock(rs.getInt("stockID")),
                                data.findUnit(rs.getInt("unitID")),
                                rs.getString("name"),
                                rs.getFloat("lastQuantityInStock"),
                                rs.getString("lastDateInStock"))
                );
            }
        } catch (SQLException e) {
            System.err.println(e.getClass() + " : " + e.getMessage());
        } finally {
            closeResultSet(rs);
            closeStatement(stm);
        }

        return result;
    }

    ObservableList<Unit> selectAllUnit() {
        ObservableList<Unit> result = FXCollections.observableArrayList();
        Statement stm = null;
        ResultSet rs = null;
        try {
            String query = "SELECT * FROM unit;";
            stm = c.createStatement();
            rs = stm.executeQuery(query);
            while (rs.next()) {
                Unit newUnit = new Unit(rs.getInt("id"), rs.getString("name"), rs.getString("abr1000"), rs.getString("abr1"));
                result.add(newUnit);
            }
        } catch (SQLException e) {
            System.err.println(e.getClass() + " : " + e.getMessage());
        } finally {
            closeResultSet(rs);
            closeStatement(stm);
        }
        return result;
    }

    ObservableList<Recipe> selectAllRecipe() {
        ObservableList<Recipe> result = FXCollections.observableArrayList();
        Statement stm = null;
        ResultSet rs = null;
        try {
            String query = "SELECT * FROM recipe;";
            stm = c.createStatement();
            rs = stm.executeQuery(query);
            while (rs.next()) {
                Recipe newRecipe = new Recipe(rs.getInt("id"), rs.getString("name"));
                newRecipe.setLines(FXCollections.observableArrayList());
                result.add(newRecipe);
            }
        } catch (SQLException e) {
            System.err.println(e.getClass() + " : " + e.getMessage());
        } finally {
            closeResultSet(rs);
            closeStatement(stm);
        }
        return result;
    }

    void fillRecipes() {
        Statement stm = null;
        ResultSet rs = null;
        try {
            String query = "SELECT * FROM recipe_line;";
            stm = c.createStatement();
            rs = stm.executeQuery(query);
            while (rs.next()) {
                Recipe recipe = data.findRecipe(rs.getInt("recipeID"));
                RecipeLine newRecipeLine = new RecipeLine(recipe, data.findRawMaterial(rs.getInt("rawMaterialID")), rs.getFloat("quantity"));
                recipe.getLines().add(newRecipeLine);
            }
        } catch (SQLException e) {
            System.err.println(e.getClass() + " : " + e.getMessage());
        } finally {
            closeResultSet(rs);
            closeStatement(stm);
        }
    }

    ObservableList<Stock> selectAllStock() {
        ObservableList<Stock> result = FXCollections.observableArrayList();
        Statement stm = null;
        ResultSet rs = null;
        try {
            String query = "SELECT * FROM stock;";
            stm = c.createStatement();
            rs = stm.executeQuery(query);
            while (rs.next()) {
                Stock newStock = new Stock(
                        rs.getInt("id"),
                        data.findRawMaterial(rs.getInt("rawMaterialID")),
                        LocalDate.parse(rs.getString("date")),
                        rs.getFloat("quantity"));
                result.add(newStock);
            }
        } catch (SQLException e) {
            System.err.println(e.getClass() + " : " + e.getMessage());
        } finally {
            closeResultSet(rs);
            closeStatement(stm);
        }
        return result;
    }

    ObservableList<RawMaterial> selectAllRawMaterial() {
        ObservableList<RawMaterial> result = FXCollections.observableArrayList();
        Statement stm = null;
        ResultSet rs = null;
        try {
            stm = c.createStatement();
            rs = stm.executeQuery("SELECT * FROM raw_material;");
            while (rs.next()) {
                RawMaterial newRaw = new RawMaterial(rs.getInt("id"), rs.getString("name"), data.findUnit(rs.getInt("unitID")));
                result.add(newRaw);
            }
        } catch (SQLException e) {
            System.err.println(e.getClass() + " : " + e.getMessage());
        } finally {
            closeResultSet(rs);
            closeStatement(stm);
        }
        return result;
    }

    ObservableList<Journal> selectAllJournalByDate(String date) {
        ObservableList<Journal> result = FXCollections.observableArrayList();
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            stm = c.prepareStatement("SELECT * FROM journal WHERE date = ?;");
            stm.setString(1, date);
            rs = stm.executeQuery();
            while (rs.next()) {
                Journal newJournal = new Journal(
                        rs.getInt("id"),
                        data.findRecipe(rs.getInt("recipeID")),
                        LocalDate.parse(rs.getString("date")),
                        rs.getFloat("multiplier")
                );
                result.add(newJournal);
            }
        } catch (SQLException e) {
            System.err.println(e.getClass() + " : " + e.getMessage());
        } finally {
            closeResultSet(rs);
            closeStatement(stm);
        }
        return result;
    }

    ObservableList<StockModification> selectAllStockModificationByDate(String date) {
        ObservableList<StockModification> result = FXCollections.observableArrayList();
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            stm = c.prepareStatement("SELECT * FROM stock_modification WHERE date = ?;");
            stm.setString(1, date);
            rs = stm.executeQuery();
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
        } catch (SQLException e) {
            System.err.println(e.getClass() + " : " + e.getMessage());
        } finally {
            closeResultSet(rs);
            closeStatement(stm);
        }
        return result;
    }

    ObservableList<RawMaterialConsumption> selectAllRawMaterialConsumptionBetweenDates(String dateStart, String dateEnd) {
        ObservableList<RawMaterialConsumption> result = FXCollections.observableArrayList();
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            stm = c.prepareStatement("SELECT rm.name AS name, rm.unitID AS unit, " +
                    "((SELECT lastQuantityInStock FROM stock_view WHERE rawMaterialID = rm.id)" +
                    " - " +
                    "(SELECT IFNULL(SUM(rl.quantity * j.multiplier), 0) FROM journal j, recipe r, recipe_line rl" +
                    " WHERE j.recipeID = r.id AND r.id = rl.recipeID AND rl.rawMaterialID = rm.id AND DATE(j.date)" +
                    " BETWEEN DATE((SELECT lastDateInStock FROM stock_view WHERE rawMaterialID = rm.id)) AND DATE(?, '-1 day'))" +
                    " + " +
                    "(SELECT IFNULL(SUM(sm.quantity), 0) FROM stock_modification sm" +
                    " WHERE sm.rawMaterialID = rm.id AND DATE(sm.date)" +
                    " BETWEEN DATE((SELECT lastDateInStock FROM stock_view WHERE rawMaterialID = rm.id)) AND DATE(?, '-1 day'))" +
                    ") AS stockAtX, " +
                    "((SELECT -IFNULL(SUM(rl.quantity * j.multiplier), 0) FROM journal j, recipe r, recipe_line rl" +
                    " WHERE j.recipeID = r.id AND r.id = rl.recipeID AND rl.rawMaterialID = rm.id AND DATE(j.date)" +
                    " BETWEEN DATE(?) AND DATE(?))" +
                    " + " +
                    "(SELECT IFNULL(SUM(sm.quantity), 0) FROM stock_modification sm WHERE sm.rawMaterialID = rm.id AND DATE(sm.date)" +
                    " BETWEEN DATE(?) AND DATE(?))" +
                    ") AS diffToY " +
                    "FROM raw_material rm;");
            stm.setString(1, dateStart);
            stm.setString(2, dateStart);
            stm.setString(3, dateStart);
            stm.setString(4, dateEnd);
            stm.setString(5, dateStart);
            stm.setString(6, dateEnd);

            rs = stm.executeQuery();
            while (rs.next()) {
                RawMaterialConsumption rawMaterialConsumption = new RawMaterialConsumption(
                        rs.getString("name"),
                        data.findUnit(rs.getInt("unit")),
                        rs.getFloat("stockAtX"),
                        rs.getFloat("stockAtX") + rs.getFloat("diffToY")
                );
                result.add(rawMaterialConsumption);
            }
        } catch (SQLException e) {
            System.err.println(e.getClass() + " : " + e.getMessage());
        } finally {
            closeResultSet(rs);
            closeStatement(stm);
        }
        return result;
    }

    ObservableList<RecipeConsumption> selectAllRecipeConsumptionBetweenDates(String dateStart, String dateEnd) {
        ObservableList<RecipeConsumption> result = FXCollections.observableArrayList();
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            stm = c.prepareStatement("SELECT recipeID, SUM(multiplier) as quantity FROM journal WHERE DATE(date) BETWEEN DATE(?) AND DATE(?) GROUP BY recipeID;");
            stm.setString(1, dateStart);
            stm.setString(2, dateEnd);
            rs = stm.executeQuery();
            while (rs.next()) {
                RecipeConsumption newRecipeConsumption = new RecipeConsumption(
                        data.findRecipe(rs.getInt("recipeID")),
                        rs.getFloat("quantity")
                );
                result.add(newRecipeConsumption);
            }
        } catch (SQLException e) {
            System.err.println(e.getClass() + " : " + e.getMessage());
        } finally {
            closeResultSet(rs);
            closeStatement(stm);
        }
        return result;
    }

    ObservableList<StockModification> selectAllStockModificationBetweenDates(String dateStart, String dateEnd) {
        ObservableList<StockModification> result = FXCollections.observableArrayList();
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            stm = c.prepareStatement("SELECT * FROM stock_modification WHERE DATE(date) BETWEEN DATE(?) AND DATE(?);");
            stm.setString(1, dateStart);
            stm.setString(2, dateEnd);
            rs = stm.executeQuery();
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
        } catch (SQLException e) {
            System.err.println(e.getClass() + " : " + e.getMessage());
        } finally {
            closeResultSet(rs);
            closeStatement(stm);
        }
        return result;
    }

    public Stock insertInStock(RawMaterial rm, float quantity, LocalDate date) {
        PreparedStatement stm = null;
        try {
            stm = c.prepareStatement("INSERT INTO stock(rawMaterialID, date, quantity) VALUES (?, ?, ?)");
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
        } finally {
            try {
                if (stm != null) { stm.close(); }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public boolean insertRecipe(Recipe recipe) {
        PreparedStatement stm = null;
        try {
            stm = c.prepareStatement("INSERT INTO recipe(name) VALUES (?);");
            stm.setString(1, recipe.getName());
            stm.executeUpdate();
            recipe.setId(getLastInsertedId(stm));
            recipe.setLines(insertMultipleRecipeLines(recipe));
            data.recipes.add(recipe);
            return true;
        } catch (SQLException e) {
            System.err.println(e.getClass() + " : " + e.getMessage());
        } finally {
            try {
                if (stm != null) { stm.close(); }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private ObservableList<RecipeLine> insertMultipleRecipeLines(Recipe recipe) {
        ObservableList<RecipeLine> result = FXCollections.observableArrayList();
        PreparedStatement stm = null;
        try {
            for (RecipeLine line : recipe.getLines()) {
                stm = c.prepareStatement("INSERT INTO recipe_line(recipeID, rawMaterialID, quantity) VALUES (?, ?, ?)");
                stm.setInt(1, recipe.getId());
                stm.setInt(2, line.getRawMaterial().getId());
                stm.setFloat(3, line.getQuantity());
                stm.executeUpdate();

                line.setRecipe(recipe);
                result.add(line);
            }
        } catch (SQLException e) {
            System.err.println(e.getClass() + " : " + e.getMessage());
        } finally {
            try {
                if (stm != null) { stm.close(); }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public boolean insertRawMaterialAndStock(String name, Unit unit, float quantity, LocalDate date) {
        PreparedStatement stm = null;
        try {
            stm = c.prepareStatement("INSERT INTO raw_material(name, unitID) VALUES (?, ?)");
            stm.setString(1, name);
            stm.setInt(2, unit.getId());
            stm.executeUpdate();
            int id = getLastInsertedId(stm);
            RawMaterial rm = new RawMaterial(id, name, unit);
            data.rawMaterials.add(rm);
            Stock stock = insertInStock(rm, quantity, date);
            StockView stockView = new StockView(rm, stock, unit, name, quantity, date.toString());
            data.stockViews.add(stockView);
            return true;
        } catch (SQLException e) {
            System.err.println(e.getClass() + " : " + e.getMessage());
        } finally {
            try {
                if (stm != null) { stm.close(); }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean insertJournal(Recipe recipe, LocalDate date, float multiplier) {
        PreparedStatement stm = null;
        try {
            stm = c.prepareStatement("INSERT INTO journal(recipeID, date, multiplier) VALUES (?, ?, ?)");
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
        } finally {
            try {
                if (stm != null) { stm.close(); }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean deleteJournal(Journal journal) {
        PreparedStatement stm = null;
        try {
            stm = c.prepareStatement("DELETE FROM journal WHERE id = ?;");
            stm.setInt(1, journal.getId());
            stm.executeUpdate();
            data.getJournalByDate(journal.getDate()).remove(journal);
            return true;
        } catch (SQLException e) {
            System.err.println(e.getClass() + " : " + e.getMessage());
        } finally {
            try {
                if (stm != null) {
                    stm.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean updateMultiplierJournal(Journal journal, float multiplier) {
        PreparedStatement stm = null;
        try {
            stm = c.prepareStatement("UPDATE journal SET multiplier = ? WHERE id = ?;");
            stm.setFloat(1, multiplier);
            stm.setInt(2, journal.getId());
            stm.executeUpdate();
            data.findJournal(journal.getId(), journal.getDate()).setMultiplier(multiplier);
            return true;
        } catch (SQLException e) {
            System.err.println(e.getClass() + " : " + e.getMessage());
        } finally {
            try {
                if (stm != null) {
                    stm.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean insertStockModification(RawMaterial rawMaterial, LocalDate date, float quantity) {
        PreparedStatement stm = null;
        try {
            stm = c.prepareStatement("INSERT INTO stock_modification(rawMaterialID, date, quantity) VALUES (?, ?, ?)");
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
        } finally {
            try {
                if (stm != null) {
                    stm.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean updateQuantityStockModification(StockModification stockModification, float quantity) {
        PreparedStatement stm = null;
        try {
            stm = c.prepareStatement("UPDATE stock_modification SET quantity = ? WHERE id = ?;");
            stm.setFloat(1, quantity);
            stm.setInt(2, stockModification.getId());
            stm.executeUpdate();
            data.findStockModification(stockModification.getId(), stockModification.getDate()).setQuantity(quantity);
            return true;
        } catch (SQLException e) {
            System.err.println(e.getClass() + " : " + e.getMessage());
        } finally {
            try {
                if (stm != null) {
                    stm.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean updateDescriptionStockModification(StockModification stockModification, String description) {
        PreparedStatement stm = null;
        try {
            stm = c.prepareStatement("UPDATE stock_modification SET description = ? WHERE id = ?;");
            stm.setString(1, description);
            stm.setInt(2, stockModification.getId());
            stm.executeUpdate();
            stm.close();
            data.findStockModification(stockModification.getId(), stockModification.getDate()).setDescription(description);
            return true;
        } catch (SQLException e) {
            System.err.println(e.getClass() + " : " + e.getMessage());
        } finally {
            try {
                if (stm != null) {
                    stm.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean deleteStockModification(StockModification stockModification) {
        PreparedStatement stm = null;
        try {
            stm = c.prepareStatement("DELETE FROM stock_modification WHERE id = ?;");
            stm.setInt(1, stockModification.getId());
            stm.executeUpdate();
            stm.close();
            data.getStockModificationByDate(stockModification.getDate()).remove(stockModification);
            return true;
        } catch (SQLException e) {
            System.err.println(e.getClass() + " : " + e.getMessage());
        } finally {
            try {
                if (stm != null) {
                    stm.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean isRawMaterialUsed(RawMaterial rawMaterial) {
        PreparedStatement stm1 = null;
        ResultSet rs1 = null;
        PreparedStatement stm2 = null;
        ResultSet rs2 = null;
        try {
            stm1 = c.prepareStatement("SELECT COUNT(*) AS count FROM recipe_line WHERE rawMaterialID = ?");
            stm1.setInt(1, rawMaterial.getId());
            rs1 = stm1.executeQuery();
            boolean usedInRecipeLine = rs1.next() && rs1.getInt("count") > 0;
            stm2 = c.prepareStatement("SELECT COUNT(*) AS count FROM stock_modification WHERE rawMaterialID = ?");
            stm2.setInt(1, rawMaterial.getId());
            rs2 = stm2.executeQuery();
            boolean usedInStockModification = rs2.next() && rs2.getInt("count") > 0;
            return usedInRecipeLine || usedInStockModification;
        } catch (SQLException e) {
            System.err.println(e.getClass() + " : " + e.getMessage());
        } finally {
            closeResultSet(rs1);
            closeStatement(stm1);
            closeResultSet(rs2);
            closeStatement(stm2);
        }
        return false;
    }

    public boolean deleteRawMaterialAndStock(RawMaterial rawMaterial) {
        PreparedStatement stm1 = null;
        PreparedStatement stm2 = null;
        try {
            stm1 = c.prepareStatement("DELETE FROM stock WHERE rawMaterialID = ?");
            stm1.setInt(1, rawMaterial.getId());
            stm1.executeUpdate();
            stm1.close();
            data.removeAllStockViewFromRawMaterial(rawMaterial);

            stm2 = c.prepareStatement("DELETE FROM raw_material WHERE id = ?");
            stm2.setInt(1, rawMaterial.getId());
            stm2.executeUpdate();
            data.rawMaterials.remove(rawMaterial);
            return true;
        } catch (SQLException e) {
            System.err.println(e.getClass() + " : " + e.getMessage());
            return false;
        } finally {
            closeStatement(stm1);
            closeStatement(stm2);
        }
    }

    public boolean isRecipeUsed(Recipe recipe) {
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            stm = c.prepareStatement("SELECT COUNT(*) As count FROM journal WHERE recipeID = ?");
            stm.setInt(1, recipe.getId());
            rs = stm.executeQuery();
            return rs.next() && rs.getInt("count") > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResultSet(rs);
            closeStatement(stm);
        }
        return false;
    }

    public boolean deleteRecipe(Recipe recipe) {
        PreparedStatement stm1 = null;
        PreparedStatement stm2 = null;
        try {
            stm1 = c.prepareStatement("DELETE FROM recipe_line WHERE recipeID = ?");
            stm1.setInt(1, recipe.getId());
            stm1.executeUpdate();

            stm2 = c.prepareStatement("DELETE FROM recipe WHERE id = ?");
            stm2.setInt(1, recipe.getId());
            stm2.executeUpdate();

            data.recipes.remove(recipe);

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeStatement(stm1);
            closeStatement(stm2);
        }
        return false;
    }
}
