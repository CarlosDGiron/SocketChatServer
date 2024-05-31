/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;

/**
 *
 * @author cana0
 */
public class Message implements Serializable {
    private int id;
    private final ArrayList<String> text;
    private final User sender;
    private final Timestamp timestamp;

    public Message(int id,String text, User sender, Timestamp timestamp) {
        this.id=id;
        this.sender = sender;
        this.timestamp = timestamp;
        this.text = trimMessage(text);
    }

    public ArrayList<String> trimMessage(String untrimmedText) {
        ArrayList<String> trimmedText = new ArrayList();
        int iterator = 0;
        String currentIndexText = "";
        for (String iterationString : untrimmedText.split(" ")) {
            iterator += iterationString.length() + 1;
            if (iterator > 80) {
                trimmedText.add(currentIndexText);
                currentIndexText = iterationString + " ";
                iterator = iterationString.length() + 1;
            } else {
                currentIndexText += iterationString + " ";
            }
        }
        trimmedText.add(currentIndexText);
        return trimmedText;
    }

    public int getId() {
        return id;
    }

    public ArrayList<String> getText() {
        return text;
    }

    public User getSender() {
        return sender;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

}
