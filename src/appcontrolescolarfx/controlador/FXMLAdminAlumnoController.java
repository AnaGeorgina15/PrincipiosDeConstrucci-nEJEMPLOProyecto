/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package appcontrolescolarfx.controlador;

import appcontrolescolarfx.dominio.AlumnoImplementacion;
import appcontrolescolarfx.interfaces.IObservador;
import appcontrolescolarfx.modelo.pojo.Alumno;
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

/**
 * FXML Controller class
 *
 * @author Ana Georgina
 */
public class FXMLAdminAlumnoController implements Initializable, IObservador {

    @FXML
    private TableView<Alumno> tvAlumnos;
    @FXML
    private TableColumn colMatricula;
    @FXML
    private TableColumn colNombre;
    @FXML
    private TableColumn colApellidoPaterno;
    @FXML
    private TableColumn colApellidoMaterno;
    @FXML
    private TableColumn colFacultad;
    @FXML
    private TableColumn colCarrera;
    @FXML
    private TableColumn colFechaNacimiento;
    
    private ObservableList<Alumno> alumnos;
    @FXML
    private TextField tfBuscar;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        cargarInformacion();
    }    
    
    private void configurarTabla(){
        colMatricula.setCellValueFactory(new PropertyValueFactory("matricula"));
        colNombre.setCellValueFactory(new PropertyValueFactory("nombre"));
        colApellidoPaterno.setCellValueFactory(new PropertyValueFactory("apellidoPaterno"));
        colApellidoMaterno.setCellValueFactory(new PropertyValueFactory("apellidoMaterno"));
        colFacultad.setCellValueFactory(new PropertyValueFactory("facultad"));
        colCarrera.setCellValueFactory(new PropertyValueFactory("carrera"));
        colFechaNacimiento.setCellValueFactory(new PropertyValueFactory("fechaNacimiento"));
    }
    
    private void cargarInformacion(){
        HashMap<String, Object> respuesta = AlumnoImplementacion.obtenerAlumnos();
        boolean error = (boolean) respuesta.get("error");
        if(!error){
            ArrayList<Alumno> alumnosBD = (ArrayList<Alumno>) respuesta.get("alumnos");
            alumnos = FXCollections.observableArrayList();
            alumnos.addAll(alumnosBD);
            tvAlumnos.setItems(alumnos);
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
        Alumno alumnoSeleccion = tvAlumnos.getSelectionModel().getSelectedItem();
        if(alumnoSeleccion != null){
            irFormulario(alumnoSeleccion);
        }else{
            Utilidades.mostrarAlertaSimple("Selección requerida",
                    "Para modificar la información de un alumno, primero debes seleccionarlo.", 
                    Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void clicBtnEliminar(ActionEvent event) {
        Alumno alumno = tvAlumnos.getSelectionModel().getSelectedItem();
        if(alumno != null){
            boolean confirmarEliminacion = Utilidades.mostrarAlertaConfirmacion(
                    "Eliminar el alumno", 
                    "¿Está seguro de eliminar la información del alumno(a) " + alumno.getNombre() 
                    + " " + alumno.getApellidoPaterno() + "?\nAl eliminar un alumno(a), "
                    + "la información no puede ser recuperada.");
            if(confirmarEliminacion){
                eliminarAlumno(alumno.getIdAlumno());
            }
        }else{
            Utilidades.mostrarAlertaSimple("Selección requerida",
                    "Para eliminar la información de un alumno, primero debes seleccionarlo.", 
                    Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void clicBtnExportar(ActionEvent event) {
        // TODO: Implementar funcionalidad de exportación
        Utilidades.mostrarAlertaSimple("Función no disponible",
                "La funcionalidad de exportar aún no está implementada.", 
                Alert.AlertType.INFORMATION);
    }
    
    private void irFormulario(Alumno alumno){
        try{
            FXMLLoader cargador = Utilidades.obtenerVista("vista/FXMLFormularioAlumno.fxml");
            Parent vista = cargador.load();
            FXMLFormularioAlumnoController controlador = cargador.getController();
            controlador.inicializarDatos(this, alumno);
            Scene scene = new Scene(vista);
            Stage stage = new Stage();
            stage.setTitle("Formulario alumnos");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        }catch (IOException ex){
            ex.printStackTrace();
        } 
    }
    
    private void eliminarAlumno(int idAlumno){
        HashMap<String, Object> respuesta = AlumnoImplementacion.eliminar(idAlumno);
        if(!(boolean) respuesta.get("error")){
            tfBuscar.setText("");
            Utilidades.mostrarAlertaSimple("Alumno(a) eliminado", 
                    "La información del alumno(a) ha sido eliminada correctamente.", 
                    Alert.AlertType.INFORMATION);
            cargarInformacion();
        }else{
            Utilidades.mostrarAlertaSimple("Error al eliminar", 
                    respuesta.get("mensaje").toString(), 
                    Alert.AlertType.ERROR);
        }
    }

    @Override
    public void notificarOperacionExitosa(String tipoOperacion, String nombre) {
        System.out.println("Operación: " + tipoOperacion);
        System.out.println("Nombre del alumno: " + nombre);
        tfBuscar.setText("");
        cargarInformacion();
    }
    
    private void configurarBusqueda(){
        if(alumnos != null && alumnos.size() > 0){
            FilteredList<Alumno> filtradoAlumnos = new FilteredList<>(alumnos, p->true);
            
            tfBuscar.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, 
                        String oldValue, String newValue) {
                    filtradoAlumnos.setPredicate(alumno ->{
                        // Caso DEFAULT vacio devolver todos
                        if (newValue == null || newValue.isEmpty()){
                            return true;
                        }
                        String lowerNewValue = newValue.toLowerCase();
                        // 1. Criterio de búsqueda por nombre
                        if(alumno.getNombre().toLowerCase().contains(lowerNewValue)){
                            return true;
                        }
                        // 2. Criterio de búsqueda por matrícula
                        if(alumno.getMatricula().toLowerCase().contains(lowerNewValue)){
                            return true;
                        }
                        // 3. Criterio de búsqueda por apellido paterno
                        if(alumno.getApellidoPaterno().toLowerCase().contains(lowerNewValue)){
                            return true;
                        }
                        // 4. Criterio de búsqueda por apellido materno
                        if(alumno.getApellidoMaterno().toLowerCase().contains(lowerNewValue)){
                            return true;
                        }
                        return false;
                    });
                }
            });
            
            SortedList<Alumno> sortedAlumno = new SortedList<>(filtradoAlumnos);
            sortedAlumno.comparatorProperty().bind(tvAlumnos.comparatorProperty());
            tvAlumnos.setItems(sortedAlumno);
        }
    }
}