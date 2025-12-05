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
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import utilidad.Utilidades;

public class FXMLFormularioAlumnoController implements Initializable {

    // --- VARIABLES CORREGIDAS SEGÚN TU FXML ---
    @FXML
    private ImageView iwFoto;
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
    private ComboBox<Facultad> cbFacultad;
    @FXML
    private ComboBox<Carrera> cbCarrera;
    @FXML
    private DatePicker dpFechaNacimiento;
    
    ObservableList<Facultad> facultades;
    ObservableList<Carrera> carreras; 

    private IObservador observador; 
    private Alumno alumnoEdicion; 
    
    private File fotoSeleccionada;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarFacultades();
        
        // AGREGADO: Listener para que al cambiar de Facultad, se carguen las Carreras
        // (Esto reemplaza la necesidad de onAction en el FXML para este combo)
        cbFacultad.valueProperty().addListener(new ChangeListener<Facultad>() {
            @Override
            public void changed(ObservableValue<? extends Facultad> observable, Facultad oldValue, Facultad newValue) {
                if(newValue != null){
                    cargarCarreras(newValue.getIdFacultad());
                }
            }
        });
    }

    @FXML
    private void clicBtnSeleccionarFoto(ActionEvent event) {
        abrirDialogo();
    }

    @FXML
    private void clicBtnCancelar(ActionEvent event) {
        cerrarVentana();
    }

    @FXML
    private void clicBtnGuardar(ActionEvent event) {
        if (sonCamposValidos()){
            if(alumnoEdicion == null){
                registrarAlumno();
            }else{
                editarAlumno();
            }
        }
    }
    
    private boolean sonCamposValidos() {
        boolean valido = true;
        String mensajeError = "Se encontraron los siguientes errores: \n";
        
        // Validación de campos usando los nuevos nombres (tf...)
        if (tfNombre.getText().isEmpty()) {
            valido = false;
            mensajeError += "- Nombre del alumno requerido.\n";
        }
        if (tfApellidoPaterno.getText().isEmpty()) {
            valido = false;
            mensajeError += "- Apellido Paterno obligatorio.\n";
        }
        if (dpFechaNacimiento.getValue() == null) {
            valido = false;
            mensajeError += "- Fecha de nacimiento requerida.\n";
        }
        if (tfCorreo.getText().isEmpty()){
            valido = false;
            mensajeError += "- El correo es requerido.\n";
        }
        if (cbCarrera.getSelectionModel().isEmpty()) {
            valido = false;
            mensajeError += "- Carrera requerida.\n";
        }
        if (tfMatricula.getText().isEmpty()) {
            valido = false;
            mensajeError += "- Matrícula requerida.\n";
        }

        if (!valido) {
            Utilidades.mostrarAlertaSimple("Campos Vacíos o Inválidos", mensajeError, Alert.AlertType.WARNING);
        }
        return valido;
    }
    
    private Alumno obtenerAlumno() {
        Alumno alumno = new Alumno();
        alumno.setMatricula(tfMatricula.getText());
        alumno.setNombre(tfNombre.getText());
        alumno.setApellidoPaterno(tfApellidoPaterno.getText());
        alumno.setApellidoMaterno(tfApellidoMaterno.getText());
        alumno.setCorreo(tfCorreo.getText());
        alumno.setFechaNacimiento(dpFechaNacimiento.getValue().toString());
        
        Carrera carreraSeleccionada = cbCarrera.getSelectionModel().getSelectedItem();
        alumno.setIdCarrera(carreraSeleccionada.getIdCarrera());
        alumno.setIdFacultad(cbFacultad.getSelectionModel().getSelectedItem().getIdFacultad());
        
        try{
            if (fotoSeleccionada != null) {
                byte[] fotoBytes = Files.readAllBytes(fotoSeleccionada.toPath());
                alumno.setFoto(fotoBytes);
            } else if (alumnoEdicion != null) {
                alumno.setFoto(alumnoEdicion.getFoto()); 
            }
        }catch(IOException ioe){
            Utilidades.mostrarAlertaSimple("Error con la foto", "No se pudo procesar la foto", Alert.AlertType.ERROR);
        }
            
        return alumno;
    }
    
    private void cerrarVentana() {
        ((Stage) tfMatricula.getScene().getWindow()).close();
    }

    private int obtenerFacultadSeleccionada(int idFacultad) {
        if(facultades != null){
            for (int i = 0; i < facultades.size(); i++) {
                if (facultades.get(i).getIdFacultad() == idFacultad) {
                    return i;
                }
            }
        }
        return -1; 
    }
    
    private void abrirDialogo(){
        FileChooser dialogoSeleccion = new FileChooser();
        dialogoSeleccion.setTitle("Seleccione un archivo");
        FileChooser.ExtensionFilter filtroImg = new FileChooser.ExtensionFilter("Archivos de Imagen", "*.png", "*.jpg", "*.gif", "*.jpeg");
        dialogoSeleccion.getExtensionFilters().add(filtroImg);
        fotoSeleccionada = dialogoSeleccion.showOpenDialog(tfCorreo.getScene().getWindow());
        
        if (fotoSeleccionada != null){
            mostrarFoto(fotoSeleccionada);
        }
    }
    
    private void mostrarFoto(File foto){
        try{
            BufferedImage bufferImg = ImageIO.read(foto);
            Image imagen = SwingFXUtils.toFXImage(bufferImg, null);
            iwFoto.setImage(imagen);
        }catch(IOException ioe){
            Utilidades.mostrarAlertaSimple("Error", "No se puede cargar la imagen.", Alert.AlertType.ERROR);
        }
    }
    
    private void cargarFacultades(){
        HashMap<String, Object> respuesta = CatalogoImplementacion.obtenerFacultades();
        
        if(!(boolean)respuesta.get("error")){
            List<Facultad> facultadesBD = (List<Facultad>)respuesta.get("facultades");
            facultades = FXCollections.observableArrayList();
            facultades.addAll(facultadesBD);
            cbFacultad.setItems(facultades);
        }else{
            Utilidades.mostrarAlertaSimple("Error", (String)respuesta.get("mensaje"), Alert.AlertType.ERROR);
        }
    }

    // Modificado para ser llamado internamente
    private void cargarCarreras(int idFacultad) {
        HashMap <String, Object> respuesta = CatalogoImplementacion.obtenerCarreras(idFacultad);
        
        if(!(boolean)respuesta.get("error")){
            List<Carrera> carrerasBD = (List<Carrera>)respuesta.get("carreras");
            carreras = FXCollections.observableArrayList();
            carreras.addAll(carrerasBD);
            cbCarrera.setItems(carreras);
        }else{
            Utilidades.mostrarAlertaSimple("Error", (String)respuesta.get("mensaje"), Alert.AlertType.ERROR);
        }
    }
    
    private void registrarAlumno(){
            Alumno alumno = obtenerAlumno();
            if (AlumnoImplementacion.verificarDuplicado(alumno.getMatricula())){
                Utilidades.mostrarAlertaSimple("Matrícula en uso", "La matrícula ya existe.", Alert.AlertType.WARNING);
                return;
            }
            
            HashMap<String, Object> respuesta = AlumnoImplementacion.registrar(alumno);
            
            if( !(boolean) respuesta.get("error") ){
                Utilidades.mostrarAlertaSimple("Registro exitoso", (String) respuesta.get("mensaje"), Alert.AlertType.INFORMATION);
                observador.notificarOperacionExitosa("registrar", alumno.getNombre());
                cerrarVentana();
            }else{
                Utilidades.mostrarAlertaSimple("Error al registrar", (String) respuesta.get("mensaje"), Alert.AlertType.ERROR);
            }
    }
    
    private void editarAlumno(){
        Alumno alumno = obtenerAlumno();
        alumno.setIdAlumno(this.alumnoEdicion.getIdAlumno());
        
        HashMap<String, Object> resultado = AlumnoImplementacion.editarAlumno(alumno);
        
        if( !(boolean)resultado.get("error") ){
            Utilidades.mostrarAlertaSimple("Éxito", resultado.get("mensaje").toString(), Alert.AlertType.INFORMATION);
            observador.notificarOperacionExitosa("editar", alumno.getNombre());
            cerrarVentana();
        }else{
            Utilidades.mostrarAlertaSimple("Error al editar", resultado.get("mensaje").toString(), Alert.AlertType.ERROR);
        }
    }
    
    public void inicializarDatos(IObservador observador, Alumno alumno) {
        this.observador = observador;
        this.alumnoEdicion = alumno;
        if (alumno != null) {
            tfMatricula.setText(alumno.getMatricula());
            tfNombre.setText(alumno.getNombre());
            tfApellidoPaterno.setText(alumno.getApellidoPaterno());
            tfApellidoMaterno.setText(alumno.getApellidoMaterno());
            tfCorreo.setText(alumno.getCorreo());
            try {
                dpFechaNacimiento.setValue(LocalDate.parse(alumno.getFechaNacimiento()));
            } catch (Exception e) { 
                // Manejo de error si la fecha viene nula o mal formato
            }

            tfMatricula.setEditable(false);
            tfMatricula.setDisable(true);

            int posFacultad = obtenerFacultadSeleccionada(alumno.getIdFacultad());
            cbFacultad.getSelectionModel().select(posFacultad);

            if(posFacultad != -1){
                cargarCarreras(alumno.getIdFacultad());
                int posCarrera = obtenerCarreraSeleccionada(alumno.getIdCarrera());
                cbCarrera.getSelectionModel().select(posCarrera);
            }

            recuperarFoto(alumno.getIdAlumno());
        }
    }
    
    private void recuperarFoto(int idAlumno){
        HashMap<String, Object> respuesta = AlumnoImplementacion.obtenerFoto(idAlumno);
        
        try{
            if (!(boolean)respuesta.get("error")) {
                byte[] fotoBytes = (byte[]) respuesta.get("foto");
                if (fotoBytes != null && fotoBytes.length > 0) {
                    ByteArrayInputStream bis = new ByteArrayInputStream(fotoBytes);
                    Image img = new Image(bis);
                    iwFoto.setImage(img);
                    this.alumnoEdicion.setFoto(fotoBytes); 
                }
            }
        }catch(Exception e){
            System.out.println("No se pudo cargar la foto o no existe: " + e.getMessage());
        }
    }
        
    private int obtenerCarreraSeleccionada(int idCarrera) {
        if (carreras != null) {
            for (int i = 0; i < carreras.size(); i++) {
                if (carreras.get(i).getIdCarrera() == idCarrera) {
                    return i;
                }
            }
        }
        return -1;
    }
}