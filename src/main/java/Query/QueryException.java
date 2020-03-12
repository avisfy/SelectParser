package Query;

public class QueryException extends Exception {
    private String code;
    private String message;

    public QueryException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public String getError() {
        return code + ": " + message;
    }

    public String getCode() {
        return code;
    }
}
