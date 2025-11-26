/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package appcontrolescolarfx.controlador;

import appcontrolescolarfx.AppControlEscolarFX;
import appcontrolescolarfx.dominio.ProfesorImplementacion;
import appcontrolescolarfx.interfaces.IObservador;
import appcontrolescolarfx.modelo.pojo.Profesor;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import utilidad.Utilidades;
import static utilidad.Utilidades.mostrarAlertaSimple;

/**
 * FXML Controller class
 *
 * @author Ana Georgina
 */
public class FXMLAdminProfesorController implements Initializable, IObservador {

    @FXML
    private TableView<Profesor> tvProfesores;
    @FXML
    private TableColumn colNoPersonal;
    @FXML
    private TableColumn colNombre;
    @FXML
    private TableColumn colApellidoPaterno;
    @FXML
    private TableColumn colApellidoMaterno;
    @FXML
    private TableColumn colFechaContratacion;
    @FXML
    private TableColumn colRol;
    
    private ObservableList<Profesor> profesores;
    @FXML
    private TextField tfBuscar;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        configurarTabla();
        cargarInformacion();
        //configurarBusqueda();
    }    
    
    private void configurarTabla(){
        colNoPersonal.setCellValueFactory(new PropertyValueFactory("noPersonal"));
        colNombre.setCellValueFactory(new PropertyValueFactory("nombre"));
        colApellidoPaterno.setCellValueFactory(new PropertyValueFactory("apellidoPaterno"));
        colApellidoMaterno.setCellValueFactory(new PropertyValueFactory("apellidoMaterno"));
        colFechaContratacion.setCellValueFactory(new PropertyValueFactory("fechaContratacion"));
        colRol.setCellValueFactory(new PropertyValueFactory("rol"));
    }
    
    private void cargarInformacion(){
        //T0D0 Conectar Implementación
        HashMap<String, Object> respuesta = ProfesorImplementacion.obtenerProfesores();
        boolean error = (boolean) respuesta.get("error");
        if(!error){
            ArrayList<Profesor> profesoresBD = (ArrayList<Profesor>) respuesta.get("profesores");
            profesores = FXCollections.observableArrayList();
            profesores.addAll(profesoresBD);
            tvProfesores.setItems(profesores);
            configurarBusqueda();
        }else{
            Utilidades.mostrarAlertaSimple("Error", respuesta.get("mensaje").toString(), Alert.AlertType.ERROR);
        }
        
    }

    @FXML
    private void clicBtnRegistrar(ActionEvent event) {
        irFormulario(null);
    }

    @FXML
    private void clicBtnModificar(ActionEvent event) {
        Profesor profesorSeleccion = tvProfesores.getSelectionModel().getSelectedItem();
        if(profesorSeleccion != null){
            irFormulario(profesorSeleccion);
        }else{
            Utilidades.mostrarAlertaSimple("","Selecciona un profesor para modificar la información"
                    + " de un profesor. Primero debes seleccionarlo.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void clicBtnEliminar(ActionEvent event) {
        Profesor profesor = tvProfesores.getSelectionModel().getSelectedItem();
        if(profesor != null){
            boolean confirmarEliminacion = Utilidades.mostrarAlertaConfirmacion("Eliminar el profesor", "¿Está "
                    + "seguro de eliminar la información del profesor(a) " + profesor.getNombre() + "?\n Al eliminar"
                            + " un profesor(a), la información no puede ser recuperada.");
            if(confirmarEliminacion){
                eliminarProfesor(profesor.getIdProfesor());
            }
        }else{
            Utilidades.mostrarAlertaSimple("","Selecciona un profesor para eliminar la información"
                    + " de un profesor. Primero debes seleccionarlo.", Alert.AlertType.WARNING);
        }
    }
    
    private void irFormulario(Profesor profesor){
        
        try{
        FXMLLoader cargador = Utilidades.obtenerVista("vista/FXMLFormularioProfesor.fxml");
        Parent vista = cargador.load();
        FXMLFormularioProfesorController controlador = cargador.getController();
        controlador.inicializarDatos(this, profesor);
        Scene scene = new Scene (vista);
        Stage stage = new Stage();
        stage.setTitle("Formulario profesores");
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
        }catch (IOException ex){
            ex.printStackTrace();
        } 
    }
    
    private void eliminarProfesor(int idProfesor){
        HashMap<String, Object> respuesta = ProfesorImplementacion.eliminar(idProfesor);
        if(!(boolean) respuesta.get("error")){
            tfBuscar.setText("");
            Utilidades.mostrarAlertaSimple("Profesor(a) eliminado", "La información del profesor(a) "
                    + "ha sido eliminada correctamente.", Alert.AlertType.INFORMATION);
            cargarInformacion();
            configurarBusqueda();
        }else{
            Utilidades.mostrarAlertaSimple("Error al eliminar", respuesta.get("mensaje").toString(), Alert.AlertType.ERROR);
        }
    }

    @Override
    public void notificarOperacionExitosa(String tipoOperacion, String nombre) {
        System.out.println("Operación: " + tipoOperacion);
        System.out.println("Nombre del profesor: " + nombre);
        tfBuscar.setText("");
        cargarInformacion();
        configurarBusqueda();
    }
    
    private void configurarBusqueda(){
        if(profesores != null && profesores.size() > 0){
            FilteredList<Profesor> filtradoProfesores = new FilteredList<>(profesores, p->true); // cuando p se cumpla
                                                                                    // p representa cada profesor del ObservableList
            tfBuscar.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    filtradoProfesores.setPredicate(profesor ->{
                       //Caso DEFAULT vacio devolver todos
                        if (newValue == null || newValue.isEmpty()){
                           return true;
                        }
                        String lowerNewValue = newValue.toLowerCase();
                        //1. Criterio de busqueda por nombre 
                        if(profesor.getNombre().toLowerCase().contains(lowerNewValue)){
                            return true;
                        }
                        //2. Criterio de busqueda por No.Personal
                        if(profesor.getNoPersonal().toLowerCase().contains(lowerNewValue)){
                            return true;
                        }
                        return false;
                   });
                }
            });
            SortedList<Profesor> sortedProfesor = new SortedList<>(filtradoProfesores);
            sortedProfesor.comparatorProperty().bind(tvProfesores.comparatorProperty());
            tvProfesores.setItems(sortedProfesor);
        }
    }
    
}
