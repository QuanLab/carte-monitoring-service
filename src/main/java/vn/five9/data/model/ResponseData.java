package vn.five9.data.model;

public class ResponseData {

    private int status;
    private String message;

    public ResponseData() {

    }

    public ResponseData(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ResponseData{" +
                "status=" + status +
                ", message='" + message + '\'' +
                '}';
    }
}
