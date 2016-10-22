package journalfabricationetcetera.db;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import journalfabricationetcetera.model.RawMaterial;
import journalfabricationetcetera.model.Stock;
import journalfabricationetcetera.model.StockView;
import journalfabricationetcetera.model.Unit;

import java.io.File;
import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by Quentin Gangler on 21/10/2016.
 *
 */
public class Database {

    private Connection c;

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

    private boolean dropTables() {
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
    }

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

    public ObservableList<Unit> selectAllUnit() {
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

    public boolean isRawMaterialNameUsed(String name) {
        try {
            PreparedStatement stm = c.prepareStatement("SELECT * FROM raw_material WHERE name = ?;");
            stm.setString(1, name);
            ResultSet rs = stm.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.err.println(e.getClass() + " : " + e.getMessage());
            return true;
        }
    }

    public StockView insertRawMaterialAndStock(String name, Unit unit, float quantity, LocalDate date) {
        try {
            PreparedStatement stm = c.prepareStatement("INSERT INTO raw_material(name, unitID) VALUES (?, ?)");
            stm.setString(1, name);
            stm.setInt(2, unit.getId());
            stm.executeUpdate();
            ResultSet rs = stm.getGeneratedKeys();
            int id = 0;
            if(rs.next()){
                id = rs.getInt(1);
            }else {
                throw new SQLException("Impossible de récupérer l'id");
            }
            RawMaterial rm = new RawMaterial(id, name, unit);
            Stock stock = insertInStock(rm, quantity, date);
            return new StockView(rm.getId(), stock.getId(), unit.getId(), name, quantity, date.toString(), 0);
        } catch (SQLException e) {
            System.err.println(e.getClass() + " : " + e.getMessage());
        }
        return null;
    }

    public ObservableList<StockView> selectStockView() {
        ObservableList<StockView> result = FXCollections.observableArrayList();
        try {
            Statement stm = c.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM stock_view;");
            while (rs.next()) {
                result.add(new StockView(
                                rs.getInt("rawMaterialID"),
                                rs.getInt("stockID"),
                                rs.getInt("unitID"),
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

    public Stock insertInStock(RawMaterial rm, float quantity, LocalDate date) {
        try {
            PreparedStatement stm = c.prepareStatement("INSERT INTO stock(rawMaterialID, date, quantity) VALUES (?, ?, ?)");
            stm.setInt(1, rm.getId());
            stm.setString(2, date.toString());
            stm.setFloat(3, quantity);
            stm.executeUpdate();
            ResultSet rs = stm.getGeneratedKeys();
            int id = 0;
            if(rs.next()){
                id = rs.getInt(1);
            }else {
                throw new SQLException("Impossible de récupérer l'id");
            }
            return new Stock(id, rm, date, quantity);
        } catch (SQLException e) {
            System.err.println(e.getClass() + " : " + e.getMessage());
        }
        return null;
    }
}
