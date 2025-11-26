/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package appcontrolescolarfx.dominio;

import appcontrolescolarfx.modelo.ConexionBD;
import appcontrolescolarfx.modelo.dao.CatalogoDAO;
import appcontrolescolarfx.modelo.pojo.Carrera;
import appcontrolescolarfx.modelo.pojo.Facultad;
import appcontrolescolarfx.modelo.pojo.Rol;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 *
 * @author Ana Georgina
 */
public class CatalogoImplementacion {
    
    public static HashMap<String, Object> obtenerRolesProfesor(){
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        try{
            ResultSet resultado = CatalogoDAO.obtenerRoles(ConexionBD.abrirConexionBD());
            ArrayList<Rol> roles = new ArrayList<>();
            while(resultado.next()){
                Rol rol = new Rol();
                rol.setIdRol(resultado.getInt("idRol"));
                rol.setRol(resultado.getString("rol"));
                roles.add(rol);
            }
            respuesta.put("error", false);
            respuesta.put("roles", roles);
            ConexionBD.cerrarConexionBD();
        }catch(SQLException e){
            respuesta.put("error", true);
            respuesta.put("mensaje", e.getMessage());
        }
        return respuesta;
    }
    
    public static HashMap<String, Object> obtenerFacultades(){
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        try{
            ResultSet resultado = CatalogoDAO.obtenerFacultades(ConexionBD.abrirConexionBD());
            ArrayList<Facultad> facultades = new ArrayList<>();
            while(resultado.next()){
                Facultad facultad = new Facultad();
                facultad.setIdFacultad(resultado.getInt("idFacultad"));
                facultad.setFacultad(resultado.getString("facultad"));
                facultades.add(facultad);
            }
            respuesta.put("error", false);
            respuesta.put("facultades", facultades);
            ConexionBD.cerrarConexionBD();
        }catch(SQLException e){
            respuesta.put("error", true);
            respuesta.put("mensaje", e.getMessage());
        }
        return respuesta;
    }
    
    public static HashMap<String, Object> obtenerCarrerasPorFacultad(int idFacultad){
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        try{
            ResultSet resultado = CatalogoDAO.obtenerCarrerasPorFacultad(idFacultad, ConexionBD.abrirConexionBD());
            ArrayList<Carrera> carreras = new ArrayList<>();
            while(resultado.next()){
                Carrera carrera = new Carrera();
                carrera.setIdCarrera(resultado.getInt("idCarrera"));
                carrera.setCarrera(resultado.getString("carrera"));
                carrera.setIdFacultad(resultado.getInt("idFacultad"));
                carreras.add(carrera);
            }
            respuesta.put("error", false);
            respuesta.put("carreras", carreras);
            ConexionBD.cerrarConexionBD();
        }catch(SQLException e){
            respuesta.put("error", true);
            respuesta.put("mensaje", e.getMessage());
        }
        return respuesta;
    }
    
    public static HashMap<String, Object> obtenerTodasCarreras(){
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        try{
            ResultSet resultado = CatalogoDAO.obtenerTodasCarreras(ConexionBD.abrirConexionBD());
            ArrayList<Carrera> carreras = new ArrayList<>();
            while(resultado.next()){
                Carrera carrera = new Carrera();
                carrera.setIdCarrera(resultado.getInt("idCarrera"));
                carrera.setCarrera(resultado.getString("carrera"));
                carrera.setIdFacultad(resultado.getInt("idFacultad"));
                carreras.add(carrera);
            }
            respuesta.put("error", false);
            respuesta.put("carreras", carreras);
            ConexionBD.cerrarConexionBD();
        }catch(SQLException e){
            respuesta.put("error", true);
            respuesta.put("mensaje", e.getMessage());
        }
        return respuesta;
    }
}