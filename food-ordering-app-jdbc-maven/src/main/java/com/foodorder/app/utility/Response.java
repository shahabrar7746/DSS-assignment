package com.foodorder.app.utility;


import com.foodorder.app.enums.ResponseStatus;

public class Response {
    private Object data;
    private ResponseStatus responseStatus;
    private final String message;

    public Response(Object data, ResponseStatus responseStatus, String message) {
        this.data = data;
        this.responseStatus = responseStatus;
        this.message = message;
    }

    public Response(ResponseStatus responseStatus, String message) {
        this.responseStatus = responseStatus;
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public ResponseStatus getResponseStatus() {
        return responseStatus;
    }

    public String getMessage() {
        return message;
    }

    public Boolean isSuccess() {
        return responseStatus.equals(ResponseStatus.SUCCESS);
    }
}