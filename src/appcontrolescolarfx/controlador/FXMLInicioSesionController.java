/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package appcontrolescolarfx.controlador;

import appcontrolescolarfx.AppControlEscolarFX;
import appcontrolescolarfx.dominio.AutenticacionImplementacion;
import appcontrolescolarfx.modelo.pojo.Profesor;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import utilidad.Utilidades;

/**
 * FXML Controller class
 *
 * @author Ana Georgina
 */
public class FXMLInicioSesionController implements Initializable {

    @FXML
    private TextField tfNoPersonal;
    @FXML
    private PasswordField pfContraseña;
    @FXML
    private Label lbErrorNoPersonal;
    @FXML
    private Label lbErrorContraseña;
   
  
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        
    }    

    @FXML
    private void clicIngresar(ActionEvent event) {
        String noPersonal = tfNoPersonal.getText();
        String password = pfContraseña.getText();
        if(sonDatosValidos(noPersonal, password)){
            validarSesion(noPersonal, password);
        }
    }
    
    private boolean sonDatosValidos(String noPersonal, String password){
        boolean correcto = true;
        
        lbErrorNoPersonal.setText("");
        lbErrorContraseña.setText("");
        
        if(noPersonal == null || noPersonal.isEmpty()){
            correcto = false;
            lbErrorNoPersonal.setText("Número de personal obligatorio");
        }
        if(password == null || password.isEmpty()){
            correcto = false;
            lbErrorContraseña.setText("Contraseña obligatoria");
        }
        return correcto;
    }
    
    private void validarSesion(String noPersonal, String password){
        HashMap<String, Object> respuesta = AutenticacionImplementacion.verificarSesionProfesor(noPersonal, password);
                
        boolean error = (boolean) respuesta.get("error"); //Casteo
        if(!error){
            //Credenciales correctas T0D0 ir pantalla principal
            Profesor profesorSesion = (Profesor) respuesta.get("profesor");
            Utilidades.mostrarAlertaSimple("Credenciales correctas", "Bienvenido(a) profesor(a)" + profesorSesion.getNombre() + 
                    ", al sistema de administración escolar.", Alert.AlertType.INFORMATION);
            irPantallaPrincipal(profesorSesion);
                        
        }else{
            //Mensaje de error
            Utilidades.mostrarAlertaSimple("Credenciales incorrectas", 
                    "No. de personal y/o contraseña incorrectos, por favor verifica la información", Alert.AlertType.ERROR);
        }
    }
    
    private void irPantallaPrincipal(Profesor profesorSesion){
        try{
            FXMLLoader cargador = new FXMLLoader(AppControlEscolarFX.class.getResource("vista/FXMLPrincipal.fxml"));
            Parent vista = cargador.load();
            FXMLPrincipalController controlador = cargador.getController();
            controlador.obtenerSesion(profesorSesion);
            
            Scene escena = new Scene(vista);
            Stage escenario = (Stage) tfNoPersonal.getScene().getWindow();
            escenario.setScene(escena);
            escenario.setTitle("Inicio");
            escenario.show();
                        
        }catch (IOException e){
            e.printStackTrace();
        }
        
    }
           
}
