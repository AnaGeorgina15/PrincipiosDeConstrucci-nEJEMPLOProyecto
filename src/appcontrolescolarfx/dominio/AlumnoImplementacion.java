/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package appcontrolescolarfx.dominio;

import appcontrolescolarfx.modelo.ConexionBD;
import appcontrolescolarfx.modelo.dao.AlumnoDAO;
import appcontrolescolarfx.modelo.pojo.Alumno;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class AlumnoImplementacion {

    public static HashMap<String, Object> registrar(Alumno alumno) {
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("error", true); // Asumimos error por defecto

        try {
            int filasAfectadas = AlumnoDAO.registrar(alumno, ConexionBD.abrirConexionBD());
            if (filasAfectadas > 0) {
                respuesta.put("error", false);
                respuesta.put("mensaje", "Información del alumno guardada de manera exitosa");
            } else {
                respuesta.put("mensaje", "Lo sentimos :(, no pudimos guardar la información");
            }
        } catch (SQLException sqle) {
            respuesta.put("mensaje", sqle.getMessage());
        } finally {
            ConexionBD.cerrarConexionBD();
        }
        return respuesta;
    }

    public static HashMap<String, Object> editarAlumno(Alumno alumno) {
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("error", true);

        try {
            // Nota: Llamamos a AlumnoDAO.editar, coincidiendo con tu DAO
            int filasAfectadas = AlumnoDAO.editar(alumno, ConexionBD.abrirConexionBD());

            if (filasAfectadas > 0) {
                respuesta.put("error", false);
                respuesta.put("mensaje", "La información del alumno " + alumno.getNombre() + " fue actualizada correctamente");
            } else {
                respuesta.put("mensaje", "No se pudo actualizar la información, inténtelo más tarde");
            }
        } catch (SQLException sqle) {
            respuesta.put("mensaje", sqle.getMessage());
        } finally {
            ConexionBD.cerrarConexionBD();
        }
        return respuesta;
    }

    public static HashMap<String, Object> eliminar(int idAlumno) {
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("error", true);

        try {
            int filasAfectadas = AlumnoDAO.eliminar(idAlumno, ConexionBD.abrirConexionBD());

            if (filasAfectadas > 0) {
                respuesta.put("error", false);
                respuesta.put("mensaje", "El alumno ha sido eliminado correctamente.");
            } else {
                respuesta.put("mensaje", "No se pudo eliminar el alumno.");
            }
        } catch (SQLException sqle) {
            respuesta.put("mensaje", "Error en BD: " + sqle.getMessage());
        } finally {
            ConexionBD.cerrarConexionBD();
        }
        return respuesta;
    }

    public static HashMap<String, Object> obtenerAlumnos() {
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        try {
            ResultSet resultado = AlumnoDAO.obtenerAlumnos(ConexionBD.abrirConexionBD());
            ArrayList<Alumno> alumnos = new ArrayList<>();
            while (resultado.next()) {
                Alumno alumno = new Alumno();
                alumno.setIdAlumno(resultado.getInt("idAlumno"));
                alumno.setNombre(resultado.getString("nombre"));
                alumno.setApellidoPaterno(resultado.getString("apellidoPaterno"));
                alumno.setApellidoMaterno(resultado.getString("apellidoMaterno"));
                alumno.setCorreo(resultado.getString("correo"));
                alumno.setMatricula(resultado.getString("matricula"));
                alumno.setIdCarrera(resultado.getInt("idCarrera"));
                alumno.setCarrera(resultado.getString("carrera"));
                alumno.setIdFacultad(resultado.getInt("idFacultad"));
                alumno.setFacultad(resultado.getString("facultad"));
                alumno.setFechaNacimiento(resultado.getString("fechaNacimiento"));
                // No cargamos la foto aquí para no hacer pesada la consulta de lista
                alumnos.add(alumno);
            }
            respuesta.put("error", false);
            respuesta.put("alumnos", alumnos);
        } catch (SQLException sqle) {
            respuesta.put("error", true);
            respuesta.put("mensaje", sqle.getMessage());
        } finally {
            ConexionBD.cerrarConexionBD();
        }
        return respuesta;
    }

    public static HashMap<String, Object> obtenerFoto(int idAlumno) {
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("error", true);
        
        try {
            // Nota: Llamamos a AlumnoDAO.obtenerFotoPorId
            byte[] foto = AlumnoDAO.obtenerFotoPorId(idAlumno, ConexionBD.abrirConexionBD());
            
            if (foto != null) {
                respuesta.put("error", false);
                respuesta.put("foto", foto);
            } else {
                respuesta.put("mensaje", "El alumno no tiene foto o no se encontró.");
            }
        } catch (SQLException sqle) {
            respuesta.put("mensaje", sqle.getMessage());
        } finally {
            ConexionBD.cerrarConexionBD();
        }
        return respuesta;
    }

    public static boolean verificarDuplicado(String matricula) {
        boolean duplicado = false;
        try {
            duplicado = AlumnoDAO.existeMatricula(matricula, ConexionBD.abrirConexionBD());
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            ConexionBD.cerrarConexionBD();
        }
        return duplicado;
    }
}