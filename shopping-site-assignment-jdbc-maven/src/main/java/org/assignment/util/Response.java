package org.assignment.util;

import org.assignment.enums.ResponseStatus;

import java.util.Objects;

public class Response {
    private Object data;//change to object class.
    private ResponseStatus status;//
private String error;
    public Object getData() {
        return data;
    }

    public String getError() {
        return error;
    }

    public ResponseStatus getStatus() {
        return status;
    }

    public Response(Object data) {
        this.data = data;
        this.status = ResponseStatus.SUCCESSFUL;
    }
    public Response(Object data, String error){
        this.data = data;
        this.error = error;
        this.status = ResponseStatus.ERROR;
    }

    @Override
    public String toString() {
        if(status == ResponseStatus.ERROR){
            return "";
        }
        return data.toString();
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        Response response = (Response) object;
        return status == response.status;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(status);
    }
}
