package enums;

public enum ResponseStatus {
    SUCCESS(200), FAILURE(404);

    private int statusCode;

    ResponseStatus(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
