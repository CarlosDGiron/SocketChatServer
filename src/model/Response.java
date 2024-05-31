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
public class Response implements Serializable{
    private final boolean responseValue;
    private final Object requestData;

    public Response(boolean responseValue, Object requestData){
        this.responseValue=responseValue;
        this.requestData=requestData;
    }
    
    public boolean getResponseValue() {
        return responseValue;
    }

    public Object getRequestData() {
        return requestData;
    }    
}
