/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package appcontrolescolarfx.controlador;

import appcontrolescolarfx.AppControlEscolarFX;
import appcontrolescolarfx.modelo.pojo.Profesor;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import utilidad.Utilidades;

/**
 * FXML Controller class
 *
 * @author Ana Georgina
 */
public class FXMLPrincipalController implements Initializable {

    @FXML
    private Label lbNombre;
    @FXML
    private Label lbNoPersonal;
    @FXML
    private Label lbRol;
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
    }    
    
    public void obtenerSesion(Profesor profesorSesion){
        lbNombre.setText(profesorSesion.getNombre() + " "
                + profesorSesion.getApellidoPaterno() + " " +
                profesorSesion.getApellidoMaterno());
        
        lbNoPersonal.setText(profesorSesion.getNoPersonal());
        lbRol.setText("Rol: " + profesorSesion.getRol());
    }

    @FXML
    private void clicAdminProfesores(ActionEvent event) {
        try {
            Parent vista = FXMLLoader.load(AppControlEscolarFX.class.getResource("vista/FXMLAdminProfesor.fxml"));
            Scene escenaAdmin = new Scene(vista);
            Stage stAdmin = new Stage();
            stAdmin.setScene(escenaAdmin);
            stAdmin.setTitle("Administración de profesores");
            stAdmin.initModality(Modality.APPLICATION_MODAL);
            stAdmin.showAndWait();
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }       
    }

    @FXML
    private void clicCerrarSesion(ActionEvent event) throws IOException {
        try{
            Parent vista = FXMLLoader.load(AppControlEscolarFX.class.getResource("vista/FXMLInicioSesion.fxml"));
            Scene escena = new Scene(vista);
            Stage stPrincipal = (Stage) lbNombre.getScene().getWindow();
            stPrincipal.setScene(escena);
            stPrincipal.setTitle("Iniciar Sesión");
            stPrincipal.show();
            
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }

    @FXML
    private void clicAdminAlumnos(ActionEvent event) {
          try {
            FXMLLoader cargador = Utilidades.obtenerVista("vista/FXMLAdminAlumno.fxml");
            Parent vista = cargador.load();
            //De otra manera para hacer:
            //Parent vista = FXMLLoader.load(AppControlEscolarFX.class.getResource("vista/FXMLAdminAlumno.fxml"));
            
            Scene escena = new Scene(vista);
            Stage escenario = new Stage();
            escenario.setScene(escena);
            escenario.setTitle("Administración de alumnos");
            escenario.initModality(Modality.APPLICATION_MODAL);
            escenario.showAndWait();
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }  
    }   
}
