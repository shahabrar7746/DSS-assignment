package utils;

import enums.ResponseStatus;

public class Response {
    private  Object response;
    private ResponseStatus statusCode;
    private String message;

    public Response(Object response, ResponseStatus statusCode, String message){
        this.response = response;
        this.statusCode = statusCode;
        this.message=message;
    }

    public Object getResponse() {
        return response;
    }

    public ResponseStatus getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }
}
