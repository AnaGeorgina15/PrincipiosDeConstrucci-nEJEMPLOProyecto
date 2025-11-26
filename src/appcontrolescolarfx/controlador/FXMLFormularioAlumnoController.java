/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package appcontrolescolarfx.controlador;

import appcontrolescolarfx.dominio.AlumnoImplementacion;
import appcontrolescolarfx.dominio.CatalogoImplementacion;
import appcontrolescolarfx.interfaces.IObservador;
import appcontrolescolarfx.modelo.pojo.Alumno;
import appcontrolescolarfx.modelo.pojo.Carrera;
import appcontrolescolarfx.modelo.pojo.Facultad;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import utilidad.Utilidades;

public class FXMLFormularioAlumnoController implements Initializable {

    @FXML
    private TextField tfMatricula;
    @FXML
    private TextField tfNombre;
    @FXML
    private TextField tfApellidoPaterno;
    @FXML
    private TextField tfApellidoMaterno;
    @FXML
    private TextField tfCorreo;
    @FXML
    private DatePicker dpFechaNacimiento;
    @FXML
    private ComboBox<Carrera> cbCarrera;
    @FXML
    private ComboBox<Facultad> cbFacultad;
    @FXML
    private ImageView ivFoto;
    @FXML
    private Label lblFoto;
    
    private ObservableList<Carrera> carreras;
    private ObservableList<Facultad> facultades;
    private IObservador observador;
    private Alumno alumnoEdicion;
    private byte[] fotoBytes;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarFacultades();
        configurarListenerFacultad();
        fotoBytes = null;
    }

    public void inicializarDatos(IObservador observador, Alumno alumno){
        this.observador = observador;
        alumnoEdicion = alumno;

        if (alumno != null){
            tfNombre.setText(alumno.getNombre());
            tfApellidoPaterno.setText(alumno.getApellidoPaterno());
            tfApellidoMaterno.setText(alumno.getApellidoMaterno());
            tfMatricula.setText(alumno.getMatricula());
            tfCorreo.setText(alumno.getCorreo());
            tfMatricula.setEditable(false);
            dpFechaNacimiento.setValue(LocalDate.parse(alumno.getFechaNacimiento()));

            int posicionFacultad = obtenerFacultadSeleccionada(alumno.getIdFacultad());
            if(posicionFacultad != -1){
                cbFacultad.getSelectionModel().select(posicionFacultad);
                cargarCarrerasPorFacultad(alumno.getIdFacultad());
                int posicionCarrera = obtenerCarreraSeleccionada(alumno.getIdCarrera());
                if(posicionCarrera != -1){
                    cbCarrera.getSelectionModel().select(posicionCarrera);
                }
            }

            // CORRECCIÓN: Cargar foto desde el objeto alumno
            if(alumno.getFoto() != null && alumno.getFoto().length > 0){
                fotoBytes = alumno.getFoto();
                cargarImagenDesdeBytes(fotoBytes);
                if(lblFoto != null){
                    lblFoto.setText("Foto del alumno");
                }
            } else {
                fotoBytes = null;
                if(lblFoto != null){
                    lblFoto.setText("Sin foto");
                }
            }
        } else {
            fotoBytes = null;
        }
    }
    
    @FXML
    private void clicBtnGuardar(ActionEvent event) {
        if(sonCamposValidos()){
            if(alumnoEdicion == null){
                registrarAlumno();
            } else{
                editarAlumno();
            }   
        }
    }

    @FXML
    private void clicBtnCancelar(ActionEvent event) {
        cerrarVentana();
    }
    
    @FXML
    private void clicBtnSeleccionarFoto(ActionEvent event) {
        FileChooser selectorArchivo = new FileChooser();
        selectorArchivo.setTitle("Seleccionar fotografía del alumno");
        FileChooser.ExtensionFilter filtroImagen = new FileChooser.ExtensionFilter(
                "Archivos de imagen (*.png, *.jpg, *.jpeg)", "*.PNG", "*.JPG", "*.JPEG");
        selectorArchivo.getExtensionFilters().add(filtroImagen);

        Stage escenario = (Stage) tfNombre.getScene().getWindow();
        File archivoFoto = selectorArchivo.showOpenDialog(escenario);

        if(archivoFoto != null){
            try{
                // CORRECCIÓN: Primero validar el tamaño
                long tamañoArchivo = archivoFoto.length();
                long tamañoMaximo = 5 * 1024 * 1024; // 5MB
                
                if(tamañoArchivo > tamañoMaximo){
                    Utilidades.mostrarAlertaSimple("Archivo muy grande", 
                            "La imagen es demasiado grande. El tamaño máximo permitido es 5MB.", 
                            Alert.AlertType.WARNING);
                    return;
                }

                // Convertir a bytes
                fotoBytes = obtenerBytesImagen(archivoFoto);

                // Cargar la imagen para mostrarla
                cargarImagenDesdeBytes(fotoBytes);
                if(lblFoto != null){
                    lblFoto.setText(archivoFoto.getName());
                }

            }catch(IOException e){
                e.printStackTrace();
                Utilidades.mostrarAlertaSimple("Error al cargar imagen", 
                        "No se pudo cargar la imagen seleccionada: " + e.getMessage(), 
                        Alert.AlertType.ERROR);
                fotoBytes = null;
            }
        }
    }
    
    private void cargarFacultades(){
        HashMap<String, Object> respuesta = CatalogoImplementacion.obtenerFacultades();
        if(!(boolean) respuesta.get("error")){
            List<Facultad> facultadesBD = (List<Facultad>) respuesta.get("facultades");
            facultades = FXCollections.observableArrayList();
            facultades.addAll(facultadesBD);
            cbFacultad.setItems(facultades);
        }else{
            Utilidades.mostrarAlertaSimple("Error al cargar facultades", 
                    respuesta.get("mensaje").toString(), 
                    Alert.AlertType.ERROR);
        }
    }
    
    private void configurarListenerFacultad(){
        cbFacultad.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
            if(newValue != null){
                cargarCarrerasPorFacultad(newValue.getIdFacultad());
            }
        });
    }
    
    private void cargarCarrerasPorFacultad(int idFacultad){
        HashMap<String, Object> respuesta = CatalogoImplementacion.obtenerCarrerasPorFacultad(idFacultad);
        if(!(boolean) respuesta.get("error")){
            List<Carrera> carrerasBD = (List<Carrera>) respuesta.get("carreras");
            carreras = FXCollections.observableArrayList();
            carreras.addAll(carrerasBD);
            cbCarrera.setItems(carreras);
        }else{
            Utilidades.mostrarAlertaSimple("Error al cargar carreras", 
                    respuesta.get("mensaje").toString(), 
                    Alert.AlertType.ERROR);
        }
    }
    
    // VALIDACIÓN COMPLETA DEL FORMULARIO
    private boolean sonCamposValidos(){
        boolean valido = true;
        StringBuilder mensajeError = new StringBuilder("Se encontraron los siguientes errores:\n");

        if (tfNombre.getText().trim().isEmpty()){
            valido = false;
            mensajeError.append("- Nombre del alumno requerido.\n");
        }

        if (tfApellidoPaterno.getText().trim().isEmpty()){
            valido = false;
            mensajeError.append("- Apellido Paterno del alumno requerido.\n");
        }

        if (tfApellidoMaterno.getText().trim().isEmpty()){
            valido = false;
            mensajeError.append("- Apellido Materno del alumno requerido.\n");
        }

        if (tfMatricula.getText().trim().isEmpty()){
            valido = false;
            mensajeError.append("- Matrícula del alumno requerida.\n");
        }

        if (tfCorreo.getText().trim().isEmpty()){
            valido = false;
            mensajeError.append("- Correo electrónico del alumno requerido.\n");
        }else if(!tfCorreo.getText().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")){
            valido = false;
            mensajeError.append("- Formato de correo electrónico inválido.\n");
        }

        if (dpFechaNacimiento.getValue() == null){
            valido = false;
            mensajeError.append("- Fecha de Nacimiento del alumno requerida.\n");
        } else if(dpFechaNacimiento.getValue().isAfter(LocalDate.now())){
            valido = false;
            mensajeError.append("- La fecha de nacimiento no puede ser futura.\n");
        }

        if (cbFacultad.getSelectionModel().isEmpty()){
            valido = false;
            mensajeError.append("- Facultad del alumno requerida.\n");
        }

        if (cbCarrera.getSelectionModel().isEmpty()){
            valido = false;
            mensajeError.append("- Carrera del alumno requerida.\n");
        }

        if(!valido){
            Utilidades.mostrarAlertaSimple("Campos vacíos o inválidos", 
                    mensajeError.toString(), 
                    Alert.AlertType.WARNING);
        }
        return valido;
    }
    
    private void registrarAlumno(){
        Alumno alumnoNuevo = obtenerAlumno();
        HashMap<String, Object> resultado = AlumnoImplementacion.registrar(alumnoNuevo);
        
        if(!(boolean)resultado.get("error")) {
            Utilidades.mostrarAlertaSimple("Alumno registrado correctamente", 
                    resultado.get("mensaje").toString(), 
                    Alert.AlertType.INFORMATION);
            observador.notificarOperacionExitosa("registrar", alumnoNuevo.getNombre());
            cerrarVentana();
        }else{
            Utilidades.mostrarAlertaSimple("Error al registrar", 
                    resultado.get("mensaje").toString(), 
                    Alert.AlertType.ERROR);
        }
    }
    
    private void editarAlumno(){
        Alumno alumnoActualizado = obtenerAlumno();
        alumnoActualizado.setIdAlumno(this.alumnoEdicion.getIdAlumno());
        
        // CORRECCIÓN: Si no se seleccionó nueva foto, mantener la anterior
        if(fotoBytes == null && this.alumnoEdicion.getFoto() != null){
            alumnoActualizado.setFoto(this.alumnoEdicion.getFoto());
        }
        
        HashMap<String, Object> resultado = AlumnoImplementacion.editar(alumnoActualizado);
        
        if(!(boolean)resultado.get("error")) {
            Utilidades.mostrarAlertaSimple("Alumno editado correctamente", 
                    resultado.get("mensaje").toString(), 
                    Alert.AlertType.INFORMATION);
            observador.notificarOperacionExitosa("editar", alumnoActualizado.getNombre());
            cerrarVentana();
        }else{
            Utilidades.mostrarAlertaSimple("Error al editar", 
                    resultado.get("mensaje").toString(), 
                    Alert.AlertType.ERROR);
        }
    }
    
    private Alumno obtenerAlumno(){
        Alumno alumno = new Alumno();
        alumno.setMatricula(tfMatricula.getText().trim());
        alumno.setNombre(tfNombre.getText().trim());
        alumno.setApellidoPaterno(tfApellidoPaterno.getText().trim());
        alumno.setApellidoMaterno(tfApellidoMaterno.getText().trim());
        alumno.setCorreo(tfCorreo.getText().trim());
        alumno.setFechaNacimiento(dpFechaNacimiento.getValue().toString());

        Carrera carreraSeleccionada = cbCarrera.getSelectionModel().getSelectedItem();
        alumno.setIdCarrera(carreraSeleccionada.getIdCarrera());

        Facultad facultadSeleccionada = cbFacultad.getSelectionModel().getSelectedItem();
        alumno.setIdFacultad(facultadSeleccionada.getIdFacultad());

        alumno.setFoto(fotoBytes);

        return alumno;
    }
    
    private void cerrarVentana(){
        ((Stage) tfNombre.getScene().getWindow()).close();
    }
    
    private int obtenerFacultadSeleccionada(int idFacultad){
        for(int i = 0; i < facultades.size(); i++){
            if(facultades.get(i).getIdFacultad() == idFacultad){
                return i;
            }
        }
        return -1;
    }
    
    private int obtenerCarreraSeleccionada(int idCarrera){
        if(carreras != null){
            for(int i = 0; i < carreras.size(); i++){
                if(carreras.get(i).getIdCarrera() == idCarrera){
                    return i;
                }
            }
        }
        return -1;
    }
    
    private byte[] obtenerBytesImagen(File imagen) throws IOException {
        FileInputStream inputStream = null;
        ByteArrayOutputStream outputStream = null;

        try {
            inputStream = new FileInputStream(imagen);
            outputStream = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int bytesLeidos;

            while((bytesLeidos = inputStream.read(buffer)) != -1){
                outputStream.write(buffer, 0, bytesLeidos);
            }

            return outputStream.toByteArray();

        } finally {
            if(inputStream != null){
                try {
                    inputStream.close();
                } catch(IOException e){
                    e.printStackTrace();
                }
            }
            if(outputStream != null){
                try {
                    outputStream.close();
                } catch(IOException e){
                    e.printStackTrace();
                }
            }
        }
    }
    
    private void cargarImagenDesdeBytes(byte[] bytes){
        try{
            if(bytes != null && bytes.length > 0){
                ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
                Image imagen = new Image(bis);
                if(!imagen.isError()){
                    ivFoto.setImage(imagen);
                } else {
                    Utilidades.mostrarAlertaSimple("Error", 
                            "No se pudo cargar la imagen", 
                            Alert.AlertType.ERROR);
                }
                bis.close();
            }
        }catch(Exception e){
            e.printStackTrace();
            Utilidades.mostrarAlertaSimple("Error", 
                    "No se pudo cargar la imagen: " + e.getMessage(), 
                    Alert.AlertType.ERROR);
        }
    }
}