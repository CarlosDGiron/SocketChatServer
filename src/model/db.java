/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

/**
 *
 * @author cana0
 */
public class db {

    public dbConection c;

    public db() {
        c = new dbConection();
    }

    public void userLoginSystemAnnouncement(User user) {
        try {
            boolean isQuerySuccesful = true;
            PreparedStatement parametro;
            c.abrir_conexion();
            String query = "INSERT INTO db_chat.messages(senderId,text,timestamp) VALUES(?,?,?);";
            parametro = (PreparedStatement) c.conexionDB.prepareStatement(query);
            parametro.setInt(1, 0);
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            parametro.setTimestamp(3, timestamp);
            parametro.setString(2, "El usuario " + user.getName() + "(" + user.getId() + ") se ha conectado.");
            int ejecutar = parametro.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            c.cerrar_conexion();
        }
    }
    
     public void userLogoffSystemAnnouncement(User user) {
        try {
            boolean isQuerySuccesful = true;
            PreparedStatement parametro;
            c.abrir_conexion();
            String query = "INSERT INTO db_chat.messages(senderId,text,timestamp) VALUES(?,?,?);";
            parametro = (PreparedStatement) c.conexionDB.prepareStatement(query);
            parametro.setInt(1, 0);
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            parametro.setTimestamp(3, timestamp);
            parametro.setString(2, "El usuario " + user.getName() + "(" + user.getId() + ") se ha desconectado.");
            int ejecutar = parametro.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            c.cerrar_conexion();
        }
    }
    
    public Response userLoginCredentialIsValid(User user) {
        boolean responseValue = false;
        User responseUser = null;
        Response response = new Response(responseValue, user);
        try {
            c.abrir_conexion();
            ResultSet res;
            String query = "Select * from db_chat.users where name='" + user.getName() + "';";
            res = c.conexionDB.createStatement().executeQuery(query);
            res.next();
            responseUser = new User(res.getInt("id"), user.getName(), user.getEncryptedPassword());
            responseValue = res.getString("password").equals(user.getEncryptedPassword());
            response = new Response(responseValue, responseUser);
            userLoginSystemAnnouncement(responseUser);
            return response;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return response;
        } finally {
            c.cerrar_conexion();
        }
    }

    public boolean userExists(User user) {
        boolean response = false;
        try {
            c.abrir_conexion();
            ResultSet res;
            String query = "Select * from db_chat.users where name='" + user.getName() + "';";
            res = c.conexionDB.createStatement().executeQuery(query);
            response = res.next();
            c.cerrar_conexion();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            c.cerrar_conexion();
        } finally {
            System.out.println("userExists:" + response);
            return response;
        }
    }

    public boolean userRegistry(User user) {
        if (!userExists(user)) {
            try {
                PreparedStatement parametro;
                c.abrir_conexion();
                String query = "INSERT INTO db_chat.users(name,password,lastconnection) VALUES(?,?,?);";
                parametro = (PreparedStatement) c.conexionDB.prepareStatement(query);
                parametro.setString(1, user.getName());
                parametro.setString(2, user.getEncryptedPassword());
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                parametro.setTimestamp(3, timestamp);
                int ejecutar = parametro.executeUpdate();
                System.out.println("userRegistry:" + ejecutar);
                c.cerrar_conexion();
                return ejecutar > 0;
            } catch (SQLException ex) {
                System.out.println(ex.getMessage() + ex.getSQLState());
                c.cerrar_conexion();
                return false;
            }
        } else {
            System.out.println("userRegistry: Error");
            return false;
        }
    }

    public boolean userLastOnlineUpdate(User user) {
        try {
            c.abrir_conexion();
            PreparedStatement parametro;
            String query = "UPDATE db_chat.users SET lastconnection=? where id=?;";
            parametro = (PreparedStatement) c.conexionDB.prepareStatement(query);
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            parametro.setTimestamp(1, timestamp);
            parametro.setInt(2, user.getId());
            boolean ejecutar = parametro.executeUpdate() > 0;
            c.cerrar_conexion();
            return ejecutar;
        } catch (SQLException ex) {
            System.out.println("Error:"+ex.getMessage());
            c.cerrar_conexion();
            return false;
        }
    }

    public ArrayList<User> getOnlineUser() {
        ArrayList<User> onlineUsers = new ArrayList();
        User iterationUser = null;
        try {
            c.abrir_conexion();
            ResultSet res;
            res = c.conexionDB.createStatement().executeQuery("Select * from db_chat.users;");
            long timestamp = System.currentTimeMillis();
            long iterationTimestamp;
            while (res.next()) {
                iterationTimestamp = res.getTimestamp("lastconnection").getTime() + 5000;
                if (timestamp <= iterationTimestamp) {
                    iterationUser = new User(res.getInt("id"), res.getString("name"), res.getString("password"));
                    onlineUsers.add(iterationUser);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error getOnlineUsers:"+ex.getMessage());
        } finally {
            c.cerrar_conexion();
            return onlineUsers;
        }
    }

    public boolean sendMessage(Message messages) {
        try {
            boolean isQuerySuccesful = true;
            PreparedStatement parametro;
            c.abrir_conexion();
            String query = "INSERT INTO db_chat.messages(senderId,text,timestamp) VALUES(?,?,?);";
            parametro = (PreparedStatement) c.conexionDB.prepareStatement(query);
            parametro.setInt(1, messages.getSender().getId());
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            parametro.setTimestamp(3, timestamp);
            for (String text : messages.getText()) {
                parametro.setString(2, text);
                int ejecutar = parametro.executeUpdate();
                if (ejecutar == 0) {
                    isQuerySuccesful = false;
                }
            }
            return isQuerySuccesful;
        } catch (SQLException ex) {
            System.out.println("sendMessage:"+ex.getMessage());
            return false;
        } finally {
            c.cerrar_conexion();
        }
    }

    public ArrayList<Message> updateMessageList(int lastMessageId) {
        ArrayList<Message> messages = new ArrayList();
        User iterationUser = null;
        Message iterationMessage = null;
        try {
            c.abrir_conexion();
            ResultSet res;
            res = c.conexionDB.createStatement().executeQuery("SELECT u.name as userName, u.id as userId, u.password as userPassword, m.text as messageText, m.id as messageId, m.timestamp as messageTimestamp, m.senderId as senderId from db_chat.users u, db_chat.messages m Where u.id=m.senderId and m.id>"+lastMessageId+" order by m.id asc;");
            while (res.next()) {
                iterationUser = new User(res.getInt("userId"), res.getString("userName"), res.getString("userPassword"));
                iterationMessage = new Message(res.getInt("messageId"), res.getString("messageText"), iterationUser, res.getTimestamp("messageTimestamp"));
                messages.add(iterationMessage);
            }
        } catch (SQLException ex) {
            System.out.println("updateMessageList:"+ex.getMessage());
        } finally {
            c.cerrar_conexion();
            return messages;
        }
    }
}
