/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.*;
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
public class ChatServer {

    static final int PORT = 5050;
    private db db;

    public ChatServer() {
        db = new db();
        
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Listening on port " + PORT + " en " + serverSocket.getInetAddress());
            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler clientHandler=new ClientHandler(clientSocket);
                Thread clientThread=new Thread(clientHandler);
                clientHandler.checkUsersOffline();
                clientThread.start();
            }
        } catch (IOException ex) {       
            System.out.println(ex.getMessage());
        }
    }
}
