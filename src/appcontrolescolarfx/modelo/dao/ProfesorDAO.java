/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package appcontrolescolarfx.modelo.dao;

import appcontrolescolarfx.modelo.pojo.Profesor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Ana Georgina
 */
public class ProfesorDAO {
        public static ResultSet obtenerProfesores(Connection conexionBD)throws SQLException {
        if(conexionBD != null){
                String consulta = "select idProfesor, nombre, apellidoPaterno, apellidoMaterno, noPersonal, "
                        + "fechaNacimiento, fechaContratacion, profesor.idRol, rol, contraseña from profesor "
                        + "inner join rol on rol.idRol = profesor.idRol;";
                PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
                return sentencia.executeQuery();
                
            }
        throw new SQLException("No hay conexión a la Base de Datos.");
    }
        
    public static int registrar(Profesor profesor, Connection conexionBD) throws SQLException {
        if(conexionBD != null){
            String insercion = "insert into profesor (idRol, nombre, apellidoPaterno, "
                    + "apellidoMaterno, noPersonal, contraseña, fechaNacimiento, fechaContratacion) "
                    + "values (?, ?, ?, ?, ?, ?, ?, ?);";
            PreparedStatement sentencia = conexionBD.prepareStatement(insercion);
            sentencia.setInt(1, profesor.getIdRol());
            sentencia.setString(2, profesor.getNombre());
            sentencia.setString(3, profesor.getApellidoPaterno());
            sentencia.setString(4, profesor.getApellidoMaterno());
            sentencia.setString(5, profesor.getNoPersonal());
            sentencia.setString(6, profesor.getPassword());
            sentencia.setString(7, profesor.getFechaNacimiento());
            sentencia.setString(8, profesor.getFechaContratacion());
            return sentencia.executeUpdate();
        }
        throw new SQLException ("No hay conexión a la base de datos.");
        
    }
    
    public static boolean existeNoPersonal(String noPersonal, Connection conexionBD) throws SQLException {
        if (conexionBD != null) {
            String existe = "select count(*) as total from profesor where noPersonal = ?";
            PreparedStatement sentencia = conexionBD.prepareStatement(existe);
            sentencia.setString(1, noPersonal);
            ResultSet rs = sentencia.executeQuery();
            if (rs.next()) {
                return rs.getInt("total") > 0;
            }
            rs.close();
            sentencia.close();
            return false;
        }
        throw new SQLException ("No hay conexión a la base de datos");
    }
    
public static int editar(Profesor profesor, Connection conexionBD) throws SQLException {
    if(conexionBD != null){
        String actualizacion = "update profesor set idRol = ?, nombre = ?, apellidoPaterno = ?, "
                + "apellidoMaterno = ?, contraseña = ?, fechaNacimiento = ?, fechaContratacion = ? "
                + "where idProfesor = ?";
        PreparedStatement sentencia = conexionBD.prepareStatement(actualizacion);
        sentencia.setInt(1, profesor.getIdRol());
        sentencia.setString(2, profesor.getNombre());
        sentencia.setString(3, profesor.getApellidoPaterno());
        sentencia.setString(4, profesor.getApellidoMaterno());
        sentencia.setString(5, profesor.getPassword());
        sentencia.setString(6, profesor.getFechaNacimiento());
        sentencia.setString(7, profesor.getFechaContratacion());
        sentencia.setInt(8, profesor.getIdProfesor());
        return sentencia.executeUpdate();
    }
    throw new SQLException("No hay conexión a la base de datos");
}
    
    public static int eliminar(int idProfesor, Connection conexionBD)throws SQLException {
        if (conexionBD != null){
            String eliminacion = "delete from profesor where idProfesor = ?";
            PreparedStatement sentencia = conexionBD.prepareStatement(eliminacion);
            sentencia.setInt(1, idProfesor);
            return sentencia.executeUpdate();
        }
        throw new SQLException ("No hay conexión a la base de datos");
    }
}
