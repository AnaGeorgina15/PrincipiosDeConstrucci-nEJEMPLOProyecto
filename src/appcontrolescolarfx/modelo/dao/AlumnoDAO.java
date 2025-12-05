/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package appcontrolescolarfx.modelo.dao;

import appcontrolescolarfx.modelo.pojo.Alumno;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AlumnoDAO {

    public static ResultSet obtenerAlumnos(Connection conexionBD) throws SQLException {
        if(conexionBD != null){
            String consulta = "select idAlumno, nombre, apellidoPaterno, apellidoMaterno, matricula, "
                    + "correo, fechaNacimiento, alumno.idCarrera, carrera, alumno.idFacultad, facultad, foto "
                    + "from alumno "
                    + "inner join carrera on carrera.idCarrera = alumno.idCarrera "
                    + "inner join facultad on facultad.idFacultad = alumno.idFacultad;";
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            return sentencia.executeQuery();
        }
        throw new SQLException("No hay conexión a la Base de Datos.");
    }
    
    public static int registrar(Alumno alumno, Connection conexionBD) throws SQLException {
        if(conexionBD != null){
            String insercion = "insert into alumno (nombre, apellidoPaterno, apellidoMaterno, "
                    + "matricula, correo, fechaNacimiento, idCarrera, idFacultad, foto) "
                    + "values (?, ?, ?, ?, ?, ?, ?, ?, ?);";
            PreparedStatement sentencia = conexionBD.prepareStatement(insercion);
            sentencia.setString(1, alumno.getNombre());
            sentencia.setString(2, alumno.getApellidoPaterno());
            sentencia.setString(3, alumno.getApellidoMaterno());
            sentencia.setString(4, alumno.getMatricula());
            sentencia.setString(5, alumno.getCorreo());
            sentencia.setString(6, alumno.getFechaNacimiento());
            sentencia.setInt(7, alumno.getIdCarrera());
            sentencia.setInt(8, alumno.getIdFacultad());
            
            if(alumno.getFoto() != null){
                sentencia.setBytes(9, alumno.getFoto());
            } else {
                sentencia.setNull(9, java.sql.Types.BLOB);
            }
            
            return sentencia.executeUpdate();
        }
        throw new SQLException("No hay conexión a la base de datos.");
    }
    
    public static boolean existeMatricula(String matricula, Connection conexionBD) throws SQLException {
        if (conexionBD != null) {
            String existe = "select count(*) as total from alumno where matricula = ?";
            PreparedStatement sentencia = conexionBD.prepareStatement(existe);
            sentencia.setString(1, matricula);
            ResultSet rs = sentencia.executeQuery();
            if (rs.next()) {
                return rs.getInt("total") > 0;
            }
            rs.close();
            sentencia.close();
            return false;
        }
        throw new SQLException("No hay conexión a la base de datos");
    }
    
    public static int editar(Alumno alumno, Connection conexionBD) throws SQLException {
        if(conexionBD != null){
            String actualizacion = "update alumno set nombre = ?, apellidoPaterno = ?, "
                    + "apellidoMaterno = ?, correo = ?, fechaNacimiento = ?, idCarrera = ?, "
                    + "idFacultad = ?, foto = ? where idAlumno = ?";
            PreparedStatement sentencia = conexionBD.prepareStatement(actualizacion);
            sentencia.setString(1, alumno.getNombre());
            sentencia.setString(2, alumno.getApellidoPaterno());
            sentencia.setString(3, alumno.getApellidoMaterno());
            sentencia.setString(4, alumno.getCorreo());
            sentencia.setString(5, alumno.getFechaNacimiento());
            sentencia.setInt(6, alumno.getIdCarrera());
            sentencia.setInt(7, alumno.getIdFacultad());
            
            if(alumno.getFoto() != null){
                sentencia.setBytes(8, alumno.getFoto());
            } else {
                sentencia.setNull(8, java.sql.Types.BLOB);
            }
            
            sentencia.setInt(9, alumno.getIdAlumno());
            return sentencia.executeUpdate();
        }
        throw new SQLException("No hay conexión a la base de datos");
    }
    
    public static int eliminar(int idAlumno, Connection conexionBD) throws SQLException {
        if (conexionBD != null){
            String eliminacion = "delete from alumno where idAlumno = ?";
            PreparedStatement sentencia = conexionBD.prepareStatement(eliminacion);
            sentencia.setInt(1, idAlumno);
            return sentencia.executeUpdate();
        }
        throw new SQLException("No hay conexión a la base de datos");
    }
    
    public static byte[] obtenerFotoPorId(int idAlumno, Connection conexionBD) throws SQLException {
        if(conexionBD != null){
            String consulta = "SELECT foto FROM alumno WHERE idAlumno = ?";
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            sentencia.setInt(1, idAlumno);
            ResultSet resultado = sentencia.executeQuery();
            
            if(resultado.next()){
                byte[] foto = resultado.getBytes("foto");
                // Es buena práctica cerrar recursos aunque se cierre la conexión después
                // Pero aquí devolvemos el dato crudo.
                return foto;
            }
            return null;
        }
        throw new SQLException("No hay conexión a la base de datos.");
    }
}