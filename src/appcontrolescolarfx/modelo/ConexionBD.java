/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package appcontrolescolarfx.modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Ana Georgina
 */
public class ConexionBD {
    
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String NOMBRE_BD = "escolarPCS";
    private static final String IP = "localhost";
    private static final String PUERTO = "3306";
    private static final String URL_CONEXION = "jdbc:mysql://" + IP + ":" + PUERTO + "/" + NOMBRE_BD + 
            "?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC";
    private static final String USUARIO = "root";
    private static final String PASSWORD = "REOA040715";
    private static Connection CONEXION = null;
    
    public static Connection abrirConexionBD(){
        Connection conexion = null;
        try {
            Class.forName(DRIVER);
            CONEXION = DriverManager.getConnection(URL_CONEXION, USUARIO, PASSWORD);
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }catch (SQLException ex){
            ex.printStackTrace();
        }
        return CONEXION;  
    }
    
    public static void cerrarConexionBD(){
        try{
            if(CONEXION != null){
                CONEXION.close();
            }
        }catch (SQLException ex){
            ex.printStackTrace();
        }
    }
    
}
