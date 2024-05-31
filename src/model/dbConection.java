/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author cana0
 */
public class dbConection {
    public Connection conexionDB;
    private final String puerto="3306";
    private final String db="db_chat";
    //jdbc:mysql://localhost:3306/?user=usr_empresa
    //jdbc:mysql://localhost:%s/%s?serverTimezone=UTC
    private final String urlConexion=String.format("jdbc:mysql://localhost:%s/%s?serverTimezone=UTC", puerto,db);
    private final String usuario="root";
    private final String pass="root";
    private final String jdbc="com.mysql.cj.jdbc.Driver";
    
    public void abrir_conexion(){
        try{            
            Class.forName(jdbc);
            conexionDB=DriverManager.getConnection(urlConexion, usuario, pass);

        }catch(ClassNotFoundException | SQLException ex){
            System.out.println("Eror abriendo conexion:"+ex.getMessage());
        }
    }
    
    public void cerrar_conexion(){
        try{
            conexionDB.close();
        }catch(SQLException ex){
            System.out.println("Eror cerrando conexion:"+ex.getNextException());
        }
    }
}
