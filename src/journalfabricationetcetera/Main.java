package journalfabricationetcetera;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import journalfabricationetcetera.controller.MainController;
import journalfabricationetcetera.db.Data;
import journalfabricationetcetera.db.Database;

import java.io.IOException;

/**
 * Created by Quentin Gangler on 21/10/2016.
 *
 */
public class Main extends Application {

    private MainController controller;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        primaryStage.addEventHandler(WindowEvent.WINDOW_SHOWING, new  EventHandler<WindowEvent>()
        {
            @Override
            public void handle(WindowEvent window)
            {
                System.out.println("[Main] window shown");
                if(controller != null) {
                    controller.afterInitialize();
                }
            }
        });

        Parent root = null;
        controller = null;
        Database d = new Database();
        Data data = new Data(d);

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("vue/main.fxml"));
            root = fxmlLoader.load();
            Scene scene = new Scene(root, 800, 600);
            controller = fxmlLoader.<MainController>getController();
            controller.setData(data);
            primaryStage.setTitle("Journal Fabrication Etcetera");
            primaryStage.getIcons().add(new Image("file:resources/icone.png"));
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

        primaryStage.setOnCloseRequest(event -> {
            d.closeConnection();
        });
    }
}
