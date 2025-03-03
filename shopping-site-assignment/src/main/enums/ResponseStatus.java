package main.enums;

public enum ResponseStatus {


    SUCCESSFUL(103),
    ERROR(402);
    private int statusCode;
     ResponseStatus(int statusCode){
        this.statusCode  = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
