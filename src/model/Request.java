/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.Serializable;

/**
 *
 * @author cana0
 */
public class Request implements Serializable{
    private final String requestType;
    private final Object requestData;

    public Request(String requestType, Object requestData){
        this.requestType=requestType;
        this.requestData=requestData;
    }
    
    public String getRequestType() {
        return requestType;
    }

    public Object getRequestData() {
        return requestData;
    }    
}
