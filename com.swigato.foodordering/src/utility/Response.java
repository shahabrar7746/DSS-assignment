package utility;

import enums.ResponseStatus;

public class Response<D> {
    private D data;
    private ResponseStatus responseStatus;

    public Response(D data, ResponseStatus responseStatus) {
        this.data = data;
        this.responseStatus = responseStatus;
    }

    public Response(ResponseStatus responseStatus) {
        this.responseStatus = responseStatus;
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