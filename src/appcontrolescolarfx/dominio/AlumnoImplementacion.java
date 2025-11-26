/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package appcontrolescolarfx.dominio;

import appcontrolescolarfx.modelo.ConexionBD;
import appcontrolescolarfx.modelo.dao.AlumnoDAO;
import appcontrolescolarfx.modelo.pojo.Alumno;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 *
 * @author Ana Georgina
 */
public class AlumnoImplementacion {
    
    public static HashMap<String, Object> obtenerAlumnos(){
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        try{
            ResultSet resultado = AlumnoDAO.obtenerAlumnos(ConexionBD.abrirConexionBD());
            ArrayList<Alumno> alumnos = new ArrayList<>();
            while(resultado.next()){
                Alumno alumno = new Alumno();
                alumno.setIdAlumno(resultado.getInt("idAlumno"));
                alumno.setNombre(resultado.getString("nombre"));
                alumno.setApellidoPaterno(resultado.getString("apellidoPaterno"));
                alumno.setApellidoMaterno(resultado.getString("apellidoMaterno"));
                alumno.setMatricula(resultado.getString("matricula"));
                alumno.setCorreo(resultado.getString("correo"));
                alumno.setFechaNacimiento(resultado.getString("fechaNacimiento"));
                alumno.setIdCarrera(resultado.getInt("idCarrera"));
                alumno.setCarrera(resultado.getString("carrera"));
                alumno.setIdFacultad(resultado.getInt("idFacultad"));
                alumno.setFacultad(resultado.getString("facultad"));
                alumno.setFoto(resultado.getBytes("foto"));
                alumnos.add(alumno);
            }
            respuesta.put("error", false);
            respuesta.put("alumnos", alumnos);
            ConexionBD.cerrarConexionBD();
        }catch(SQLException e){
            respuesta.put("error", true);
            respuesta.put("mensaje", e.getMessage());           
        }
        return respuesta;
    }
    
    public static HashMap<String, Object> registrar(Alumno alumno) {
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        Connection conexion = null;
        try {
            conexion = ConexionBD.abrirConexionBD();
            boolean existe = AlumnoDAO.existeMatricula(alumno.getMatricula(), conexion);
            if (existe) {
                respuesta.put("error", true);
                respuesta.put("mensaje", "La matrícula " 
                        + alumno.getMatricula() + " ya está registrada");
            } else {
                int filasAfectadas = AlumnoDAO.registrar(alumno, conexion);
                if (filasAfectadas > 0) {
                    respuesta.put("error", false);
                    respuesta.put("mensaje", "El registro del alumno(a) " 
                            + alumno.getNombre() + " fue guardado correctamente");
                } else {
                    respuesta.put("error", true);
                    respuesta.put("mensaje", "Lo sentimos no se pudo guardar la información "
                            + "del alumno, por favor inténtelo más tarde");
                }
            }
            ConexionBD.cerrarConexionBD();
        } catch (SQLException e) {
            respuesta.put("error", true);
            respuesta.put("mensaje", e.getMessage());
        }
        return respuesta;
    }
    
    public static HashMap<String, Object> editar(Alumno alumno){
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        try{
            int filasAfectadas = AlumnoDAO.editar(alumno, ConexionBD.abrirConexionBD());
            if (filasAfectadas > 0) {
                respuesta.put("error", false);
                respuesta.put("mensaje", "El registro del alumno(a) " 
                        + alumno.getNombre() + " fue actualizado correctamente");
            } else {
                respuesta.put("error", true);
                respuesta.put("mensaje", "Lo sentimos no se pudo modificar la información "
                        + "del alumno, por favor inténtelo más tarde");
            }
            ConexionBD.cerrarConexionBD();
        }catch (SQLException e){
            respuesta.put("error", true);
            respuesta.put("mensaje", e.getMessage());
        }
        return respuesta;
    }
    
    public static HashMap<String, Object> eliminar(int idAlumno){
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        try{
            int filasAfectadas = AlumnoDAO.eliminar(idAlumno, ConexionBD.abrirConexionBD());
            if (filasAfectadas > 0){
                respuesta.put("error", false);
                respuesta.put("mensaje", "El registro del alumno(a) fue eliminado");
            }else{
                respuesta.put("error", true);
                respuesta.put("mensaje", "Lo sentimos no se pudo eliminar la información "
                        + "del alumno, por favor inténtelo más tarde");
            }
            ConexionBD.cerrarConexionBD();
        }catch (SQLException e){
            respuesta.put("error", true);
            respuesta.put("mensaje", e.getMessage());
        }
        return respuesta;
    }
    
    public static HashMap<String, Object> obtenerFoto(int idAlumno){
    HashMap<String, Object> respuesta = new LinkedHashMap<>();
    try{
        byte[] foto = AlumnoDAO.obtenerFotoPorId(idAlumno, ConexionBD.abrirConexionBD());
        if(foto != null){
            respuesta.put("error", false);
            respuesta.put("foto", foto);
        }else{
            respuesta.put("error", true);
            respuesta.put("mensaje", "No se encontró foto para el alumno.");
        }
        ConexionBD.cerrarConexionBD();
    }catch(SQLException e){
        respuesta.put("error", true);
        respuesta.put("mensaje", e.getMessage());
    }
    return respuesta;
    }
}
