package dk.lyngby.exceptions;
public class API_Exception extends Exception {
    int errorCode;
    Object object;

    public API_Exception(int errCode, String message, Throwable course) {
        super(message,course);
        this.errorCode = errCode;
    }

    public API_Exception(int errCode, String message, Object object) {
        super(message);
        this.errorCode = errCode;
        this.object = object;
    }

    public API_Exception(String message, int errCode) {
        super(message);
        this.errorCode = errCode;
    }
    public API_Exception(String message) {
        super(message);
        this.errorCode = 400;
    }
    
    public int getErrorCode() {
        return errorCode;
    }    
}