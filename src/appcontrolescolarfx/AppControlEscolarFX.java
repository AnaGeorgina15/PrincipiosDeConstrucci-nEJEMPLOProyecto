/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMain.java to edit this template
 */
package appcontrolescolarfx;

import java.io.IOException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author Ana Georgina
 */
public class AppControlEscolarFX extends Application {
    
    @Override
    public void start(Stage primaryStage) {
         try {
            //T0D0 LLAMAR VISTA
            Parent root = FXMLLoader.load(getClass().getResource("vista/FXMLInicioSesion.fxml"));
            Scene sceneComponentes = new Scene(root);
            primaryStage.setScene(sceneComponentes);
            primaryStage.show();
            
        } catch (IOException ex) {
            ex.printStackTrace();
    }
       
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
