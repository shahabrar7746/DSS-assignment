package org.assignment.enums;

public enum ResponseStatus {


    SUCCESSFUL(103),
    ERROR(402),
    PROCESSING(201);
    private int statusCode;
    ResponseStatus(int statusCode){
       this.statusCode  = statusCode;
   }

    public int getStatusCode() {

         return statusCode;
    }

}
