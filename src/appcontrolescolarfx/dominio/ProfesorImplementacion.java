/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package appcontrolescolarfx.dominio;

import appcontrolescolarfx.modelo.ConexionBD;
import appcontrolescolarfx.modelo.dao.ProfesorDAO;
import appcontrolescolarfx.modelo.pojo.Profesor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 *
 * @author Ana Georgina
 */
public class ProfesorImplementacion {
    
    public static HashMap<String, Object> obtenerProfesores(){
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        try{
            ResultSet resultado = ProfesorDAO.obtenerProfesores(ConexionBD.abrirConexionBD());
            ArrayList<Profesor> profesores = new ArrayList<>();
            while(resultado.next()){
                Profesor profesor = new Profesor();
                profesor.setIdProfesor(resultado.getInt("idProfesor"));
                profesor.setNombre(resultado.getString("nombre"));
                profesor.setApellidoPaterno(resultado.getString("apellidoPaterno"));
                profesor.setApellidoMaterno(resultado.getString("apellidoMaterno"));
                profesor.setNoPersonal(resultado.getString("noPersonal"));
                profesor.setFechaNacimiento(resultado.getString("fechaNacimiento"));
                profesor.setFechaContratacion(resultado.getString("fechaContratacion"));
                profesor.setIdRol(resultado.getInt("idRol"));
                profesor.setRol(resultado.getString("rol"));
                profesor.setPassword(resultado.getString("contraseña"));
                profesores.add(profesor);
            }
            respuesta.put("error", false);
            respuesta.put("profesores", profesores);
            ConexionBD.cerrarConexionBD();
        }catch(SQLException e){
            respuesta.put("error", true);
            respuesta.put("mensaje", e.getMessage());           
        }
        return respuesta;
    }
    
 public static HashMap<String, Object> registrar(Profesor profesor) {
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        Connection conexion = null;
        try {
            conexion = ConexionBD.abrirConexionBD();
            boolean existe = ProfesorDAO.existeNoPersonal(profesor.getNoPersonal(), conexion);
            if (existe) {
                respuesta.put("error", true);
                respuesta.put("mensaje", "El número de personal " 
                        + profesor.getNoPersonal() + " ya está registrado");
            } else {
                int filasAfectadas = ProfesorDAO.registrar(profesor, conexion);
                if (filasAfectadas > 0) {
                    respuesta.put("error", false);
                    respuesta.put("mensaje", "El registro del profesor(a) " 
                            + profesor.getNombre() + " fue guardado correctamente");
                } else {
                    respuesta.put("error", true);
                    respuesta.put("mensaje", "Lo sentimos no se pudo guardar la información "
                            + " del profesor, por favor inténtelo más tarde");
                }
            }
            ConexionBD.cerrarConexionBD();
        } catch (SQLException e) {
            respuesta.put("error", true);
            respuesta.put("mensaje", e.getMessage());
        }
        return respuesta;
    }
 
    public static HashMap<String, Object> editar(Profesor profesor){
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        try{
            int filasAfectadas =  ProfesorDAO.editar(profesor, ConexionBD.abrirConexionBD());
            if (filasAfectadas > 0) {
                    respuesta.put("error", false);
                    respuesta.put("mensaje", "El registro del profesor(a) " 
                            + profesor.getNombre() + " fue guardado correctamente");
            } else {
                    respuesta.put("error", true);
                    respuesta.put("mensaje", "Lo sentimos no se pudo modificar la información "
                            + " del profesor, por favor inténtelo más tarde");
            }
            ConexionBD.cerrarConexionBD();
        }catch (SQLException e){
            respuesta.put("error", true);
            respuesta.put("mensaje", e.getMessage());
        }
        return respuesta;
    }
    
    public static HashMap<String, Object> eliminar(int idProfesor){
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        try{
            int filasAfectadas = ProfesorDAO.eliminar(idProfesor, ConexionBD.abrirConexionBD());
            if (filasAfectadas > 0){
                respuesta.put("error", false);
                respuesta.put("mensaje", "El registro del profesor(a) fue eliminado");
            }else{
                respuesta.put("error", false);
                respuesta.put("mensaje", "Lo sentimos no se pudo eliminar la información "
                            + " del profesor, por favor inténtelo más tarde");
            }
            ConexionBD.cerrarConexionBD();
        }catch (SQLException e){
            respuesta.put("error", true);
            respuesta.put("mensaje", e.getMessage());
        }
        return respuesta;
    }
}
