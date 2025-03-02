package util;

import enums.ResponseStatus;

public class Response <D>{
private D data;
private ResponseStatus status;

    public D getData() {
        return data;
    }

    public ResponseStatus getStatus() {
        return status;
    }

    public Response(D data, ResponseStatus status){
    this.data = data;
    this.status = status;
}
public static Response success(Object data){
        return new Response<>(data, ResponseStatus.SUCESSFULL);
}
public  static  Response error(String message){
        return new Response(message, ResponseStatus.ERROR);
}

    @Override
    public String toString() {
      return  data.toString();
    }
}
