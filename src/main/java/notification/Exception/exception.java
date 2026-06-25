package notification.Exception;

import java.time.LocalDateTime;
import java.util.Map;

public class exception extends Exception{
    private final String errorCode;
    private final LocalDateTime timestamp;
    private final Map<String, Object> metadata;

    public exception(String msg) {
        super(msg);
        this.errorCode = "";
        this.timestamp =  LocalDateTime.now();
        this.metadata = null;
    }

    public exception(String msg, Throwable cause) {
        super(msg, cause);
        this.errorCode = "";
        this.timestamp =  LocalDateTime.now();
        this.metadata = null;
    }

    public exception(String msg, Throwable cause, String errorCode, Map<String, Object> metadata) {
        super(msg, cause);
        this.errorCode = errorCode;
        this.timestamp = LocalDateTime.now();
        this.metadata = metadata;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    @Override
    public String toString() {
        return "exception [errorCode=" + errorCode + ", timestamp=" + timestamp + ", metadata=" + metadata + "]";
    }
    
}
