/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package appcontrolescolarfx.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Ana Georgina
 */
public class CatalogoDAO {
    
    public static ResultSet obtenerRoles(Connection conexionBD) throws SQLException {
        if(conexionBD != null){
            String consulta = "SELECT * FROM rol";
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            return sentencia.executeQuery();
        }
        throw new SQLException("No hay conexión a la base de datos.");
    }
    
    public static ResultSet obtenerFacultades(Connection conexionBD) throws SQLException {
        if(conexionBD != null){
            String consulta = "SELECT * FROM facultad ORDER BY facultad";
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            return sentencia.executeQuery();
        }
        throw new SQLException("No hay conexión a la base de datos.");
    }
    
    public static ResultSet obtenerCarrerasPorFacultad(int idFacultad, Connection conexionBD) throws SQLException {
        if(conexionBD != null){
            String consulta = "SELECT * FROM carrera WHERE idFacultad = ? ORDER BY carrera";
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            sentencia.setInt(1, idFacultad);
            return sentencia.executeQuery();
        }
        throw new SQLException("No hay conexión a la base de datos.");
    }
    
    public static ResultSet obtenerTodasCarreras(Connection conexionBD) throws SQLException {
        if(conexionBD != null){
            String consulta = "SELECT * FROM carrera ORDER BY carrera";
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            return sentencia.executeQuery();
        }
        throw new SQLException("No hay conexión a la base de datos.");
    }
}