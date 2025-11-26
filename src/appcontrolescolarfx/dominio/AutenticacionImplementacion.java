/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package appcontrolescolarfx.dominio;

import appcontrolescolarfx.modelo.ConexionBD;
import appcontrolescolarfx.modelo.dao.AutenticacionDAO;
import appcontrolescolarfx.modelo.pojo.Profesor;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 *
 * @author Ana Georgina
 */
public class AutenticacionImplementacion {
    
    public static HashMap<String, Object> verificarSesionProfesor(String noPersonal, String password){
        
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        try{
        ResultSet resultado = AutenticacionDAO.autenticarUsuario(noPersonal, password, ConexionBD.abrirConexionBD());
        if(resultado.next()){
                //Funciona como matrices y apunta a los elementos
                
                //Si entra, las credenciales son correctas
            Profesor profesorSesion = new Profesor();
            profesorSesion.setIdProfesor(resultado.getInt("idProfesor"));
            profesorSesion.setNombre(resultado.getString("nombre"));
            profesorSesion.setApellidoPaterno(resultado.getString("apellidoPaterno"));
            profesorSesion.setApellidoMaterno(resultado.getString("apellidoMaterno"));
            profesorSesion.setNoPersonal(resultado.getString("noPersonal"));
            profesorSesion.setIdRol(resultado.getInt("idRol"));
            profesorSesion.setRol(resultado.getString("rol"));
            respuesta.put("error", false);
            respuesta.put("mensaje", "Credenciales correctas.");
            respuesta.put("profesor", profesorSesion);
        }else{
            //Si no, las credenciales son incorrectas
            respuesta.put("error", true);
            respuesta.put("mensaje", "Las credenciales son incorrectas, por favor verifique la información.");
        }
        ConexionBD.cerrarConexionBD();
        }catch(SQLException ex){
            respuesta.put("error", true);
            respuesta.put("mensaje", ex.getMessage());
        }
        return respuesta;
    }
}
