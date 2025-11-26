/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package appcontrolescolarfx.controlador;

import appcontrolescolarfx.dominio.CatalogoImplementacion;
import appcontrolescolarfx.dominio.ProfesorImplementacion;
import appcontrolescolarfx.interfaces.IObservador;
import appcontrolescolarfx.modelo.pojo.Profesor;
import appcontrolescolarfx.modelo.pojo.Rol;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import utilidad.Utilidades;

/**
 * FXML Controller class
 *
 * @author Ana Georgina
 */
public class FXMLFormularioProfesorController implements Initializable {

    @FXML
    private TextField tfNoPersonal;
    @FXML
    private TextField tfNombre;
    @FXML
    private TextField tfApellidoPaterno;
    @FXML
    private TextField tfApellidoMaterno;
    @FXML
    private DatePicker dpFechaNacimiento;
    @FXML
    private DatePicker dpFechaContratacion;
    @FXML
    private TextField pfPassword;
    @FXML
    private ComboBox<Rol> cbRol;
    private ObservableList<Rol> roles;
    private IObservador observador;
    private Profesor profesorEdicion;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarRolesProfesor();
    }    

    public void inicializarDatos(IObservador observador, Profesor profesor){
        this.observador = observador;
        profesorEdicion = profesor;
        //Cargar datos a la pantalla de edición
        try{
            if (profesor != null){
            tfNombre.setText(profesor.getNombre());
            tfApellidoPaterno.setText(profesor.getApellidoPaterno());
            tfApellidoMaterno.setText(profesor.getApellidoMaterno());
            tfNoPersonal.setText(profesor.getNoPersonal());
            pfPassword.setText(profesor.getPassword());
            tfNoPersonal.setEditable (false);
            dpFechaNacimiento.setValue(LocalDate.parse(profesor.getFechaNacimiento()));
            dpFechaContratacion.setValue(LocalDate.parse(profesor.getFechaContratacion()));
            int posicion = obtenerRolSeleccionado(profesor.getIdRol());
            cbRol.getSelectionModel().select(posicion);
            }   
        }catch (Exception e){
            e.printStackTrace();
        }
        
    }
    
    @FXML
    private void clicBtnGuardar(ActionEvent event) {
        if(sonCamposValidos()){
            if(profesorEdicion == null){
                registrarProfesor();
            } else{
                editarProfesor();
            }   
        }
    }

    @FXML
    private void clicBtnCancelar(ActionEvent event) {
        cerrarVentana();
    }
    
    private void cargarRolesProfesor(){
        HashMap<String, Object> respuesta = CatalogoImplementacion.obtenerRolesProfesor();
        if( !(boolean) respuesta.get("error")){
            List<Rol> rolesBD = (List<Rol>) respuesta.get("roles");
            roles = FXCollections.observableArrayList();
            roles.addAll(rolesBD);
            cbRol.setItems(roles);
        }else{
            Utilidades.mostrarAlertaSimple("Error al cargar roles del sistema.", respuesta.get("mensaje").toString(), Alert.AlertType.ERROR);
        }
    }
    
    private boolean sonCamposValidos(){
        boolean valido = true;
        String mensajeError = "Se encontraron los siguientes errores:\n";
        if (tfNombre.getText().isEmpty()){
            valido = false;
            mensajeError += "Nombre del profesor requerido.\n";
        }
        
        if (tfApellidoPaterno.getText().isEmpty()){
            valido = false;
            mensajeError += "- Apellido Paterno del profesor requerido.\n";
        }
        
        if (tfApellidoMaterno.getText().isEmpty()){
            valido = false;
            mensajeError += "- Apellido Materno del profesor requerido.\n";
        }
        
        if (tfNoPersonal.getText().isEmpty()){
            valido = false;
            mensajeError += "- No. Personal del profesor requerido.\n";
        }
        
        if (pfPassword.getText().isEmpty()){
            valido = false;
            mensajeError += "- Contraseña del profesor requerido.\n";
        }
        
        //dpFechaNacimiento.setValue(LocalDate.MAX);
        
        if (dpFechaNacimiento.getValue() == null){
            valido = false;
            mensajeError += "- Fecha de Nacimiento del profesor requerido.\n";
        }
        
        if (dpFechaContratacion.getValue() == null){
            valido = false;
            mensajeError += "- Fecha de Contratación del profesor requerido.\n";
        }
        
        //cbRol.getSelectionModel().isEmpty();
        
        if (cbRol.getSelectionModel().isEmpty()){
            valido = false;
            mensajeError += "- Fecha de Contratación del profesor requerido.\n";
        }
        
        if(!valido){
            Utilidades.mostrarAlertaSimple("Campos vacíos", mensajeError, Alert.AlertType.WARNING);
        }
        return valido;
    }
    
    private void registrarProfesor(){
        Profesor profesorNuevo = obtenerProfesor();
        HashMap<String, Object> resultado = ProfesorImplementacion.registrar(profesorNuevo);
        
        if(!(boolean)resultado.get("error")) {
            Utilidades.mostrarAlertaSimple("Profesor registrado correctamente.", resultado.get("mensaje").toString(), Alert.AlertType.INFORMATION);
            observador.notificarOperacionExitosa("registrar", profesorNuevo.getNombre());
            cerrarVentana();
        }else{
            Utilidades.mostrarAlertaSimple("Error al registrar.", resultado.get("mensaje").toString(), Alert.AlertType.ERROR);
        }
    }
    
    private void editarProfesor(){
        Profesor profesorEdicion = obtenerProfesor();
        profesorEdicion.setIdProfesor(this.profesorEdicion.getIdProfesor());
        HashMap<String, Object> resultado = ProfesorImplementacion.editar(profesorEdicion);
         if(!(boolean)resultado.get("error")) {
            Utilidades.mostrarAlertaSimple("Profesor editado correctamente.", resultado.get("mensaje").toString(), Alert.AlertType.INFORMATION);
            observador.notificarOperacionExitosa("editar", profesorEdicion.getNombre());
            cerrarVentana();
        }else{
            Utilidades.mostrarAlertaSimple("Error al editar.", resultado.get("mensaje").toString(), Alert.AlertType.ERROR);
        }
    }
    
    private Profesor obtenerProfesor(){
        Profesor profesor = new Profesor();
        profesor.setNoPersonal(tfNoPersonal.getText());
        profesor.setNombre(tfNombre.getText());
        profesor.setApellidoPaterno(tfApellidoPaterno.getText());
        profesor.setApellidoMaterno(tfApellidoMaterno.getText());
        profesor.setPassword(pfPassword.getText());
        profesor.setFechaNacimiento(dpFechaNacimiento.getValue().toString());
        profesor.setFechaContratacion(dpFechaContratacion.getValue().toString());
        Rol rolSeleccionado = cbRol.getSelectionModel().getSelectedItem();
        profesor.setIdRol(rolSeleccionado.getIdRol());
        return profesor;
    }
    
    private void cerrarVentana(){
        //Stage escenario = (Stage) tfNombre.getScene().getWindow();
        //escenario.close();
        ((Stage) tfNombre.getScene().getWindow()).close();
    }
    
    private int obtenerRolSeleccionado(int idRol){
        for(int i = 0; i < roles.size();i++){
            if(roles.get(i).getIdRol() == idRol){
                return i;
            }
        }
        return -1;
    }
    
}
