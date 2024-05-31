/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Message;
import model.Request;
import model.Response;
import model.User;
import model.db;

/**
 *
 * @author cana0
 */
public class ClientHandler implements Runnable {

    private final Socket clientSocket;
    private final db db;
    private ArrayList<User>onlineUsers;
    ArrayList<User> logoffUsers;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        onlineUsers=new ArrayList();
        logoffUsers=new ArrayList();
        this.db = new db();
    }

    @Override
    public void run() {
        try {
            OutputStream outputStream = clientSocket.getOutputStream();
            ObjectOutputStream clientResponse = new ObjectOutputStream(outputStream);
            InputStream inputStream = clientSocket.getInputStream();
            ObjectInputStream clientRequest = new ObjectInputStream(inputStream);
            Request request = (Request) clientRequest.readObject();
            switch (request.getRequestType()) {
                case "userLogin" ->
                    clientResponse.writeObject(userLogin((User) request.getRequestData()));
                case "userRegistry" ->
                    clientResponse.writeObject(userRegistry((User) request.getRequestData()));
                case "userOnlineCheck" -> {
                    clientResponse.writeObject(userLastConnection((User) request.getRequestData()));
                }
                case "sendMessage" ->
                    sendMessage((Message) request.getRequestData());
                case "updateMessageList"->
                    clientResponse.writeObject(updateMessageList((int)request.getRequestData()));
            }
            clientSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Response userLastConnection(User user) {
        db.userLastOnlineUpdate(user);
        Response response = new Response(true, db.getOnlineUser());
        onlineUsers=(ArrayList < User >)response.getRequestData();
        return response;
    }

    private Response userRegistry(User user) {
        Response response = new Response(db.userRegistry(user), user);
        return response;
    }

    private Response userLogin(User user) {
        Response response = db.userLoginCredentialIsValid(user);
        return response;
    }

    private void sendMessage(Message message) {
        db.sendMessage(message);
    }
    
    private Response updateMessageList(int lastMessageId){
        Response response=new Response(true,db.updateMessageList(lastMessageId));        
        return response;
    }
    public void checkUsersOffline(){        
        logoffUsers=onlineUsers;
        onlineUsers=new ArrayList();
        onlineUsers=db.getOnlineUser();
        logoffUsers.removeAll(onlineUsers);
        for(User iterator:logoffUsers){
            System.out.println(iterator.getName());
            db.userLogoffSystemAnnouncement(iterator);
        }
    }
}
