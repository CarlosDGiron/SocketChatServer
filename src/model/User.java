/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;  
import java.security.MessageDigest;  
/**
 *
 * @author cana0
 */
public class User implements Serializable{
    private final int id;
    private final String name;
    private final String encryptedPassword;
    
    public User(int id, String name, String password){
        this.name=name;
        this.id=id;
        this.encryptedPassword=encryptPassword(password);
        
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }
    
    private String encryptPassword(String password){
        String passwordEncrypted="";
        try   
        {  
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(password.getBytes());  
            byte[] bytes = m.digest();  
            StringBuilder s = new StringBuilder();  
            for(int i=0; i< bytes.length ;i++)  
            {  
                s.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));  
            } 
            passwordEncrypted = s.toString();  
        }   
        catch (NoSuchAlgorithmException e)   
        {  
            System.out.println("Error encriptando la contraseña: "+e);
        }
        return passwordEncrypted; 
    }
}
