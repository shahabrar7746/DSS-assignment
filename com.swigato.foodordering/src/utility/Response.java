package utility;

import enums.ResponseStatus;

public  class Response<D> {
    private D data;
    private ResponseStatus responseStatus;

    public String getMessage() {
        return message;
    }

    private String message;

    public Response(D data, ResponseStatus responseStatus,String message) {
        this.data = data;
        this.responseStatus = responseStatus;
        this.message = message;
    }

    public Response(ResponseStatus responseStatus,String message) {
        this.responseStatus = responseStatus;
        this.message = message;

    }

    public D getData() {
        return data;
    }

    public void setData(D data) {
        this.data = data;
    }

    public ResponseStatus getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(ResponseStatus responseStatus) {
        this.responseStatus = responseStatus;
    }
}