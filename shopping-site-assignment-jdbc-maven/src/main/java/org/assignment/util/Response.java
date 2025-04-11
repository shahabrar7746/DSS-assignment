package org.assignment.util;

import lombok.*;
import org.assignment.enums.ResponseStatus;

import java.util.Objects;


@Data
@Getter
@RequiredArgsConstructor
public class Response {
    private final ResponseStatus status;//
    private final Object data;//change to object class.
    private final String error;

    public Object getData() {
        return data;
    }

    public String getError() {
        return error;
    }

    public ResponseStatus getStatus() {
        return status;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(status);
    }

    @Override
    public String toString() {
        return status == ResponseStatus.ERROR ? "" : data.toString();
    }

}
