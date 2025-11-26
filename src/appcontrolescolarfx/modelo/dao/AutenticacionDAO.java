/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package appcontrolescolarfx.modelo.dao;

import appcontrolescolarfx.modelo.ConexionBD;
import appcontrolescolarfx.modelo.pojo.Profesor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Ana Georgina
 */
public class AutenticacionDAO {
    
    public static ResultSet autenticarUsuario(String noPersonal, String password, Connection conexionBD) throws SQLException {
        
        Profesor profesorSesion = null;
        //Connection conexionBD = ConexionBD.abrirConexionBD();
        if(conexionBD != null){
            //Hay conexión con la BD
            String consulta = "SELECT idProfesor, nombre, " + 
                    "apellidoPaterno, apellidoMaterno, noPersonal, p.idRol, rol " +
                    "FROM profesor p " + 
                    "INNER JOIN rol r ON r.idRol = p.idRol " +
                    "WHERE noPersonal = ? AND contraseña = ?";
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            sentencia.setString(1, noPersonal);
            sentencia.setString(2, password);
            ResultSet resultado = sentencia.executeQuery();
            //conexionBD.close();   
            return resultado;
        }
        throw new SQLException("No hay conexión a la Base de Datos.");

    }
}
