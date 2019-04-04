package vn.five9.data.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class Notification {

    public enum Type {
        ERROR, WARN, INFO
    }

    @JsonProperty("id")
    private String id;

    @JsonProperty("type")
    private Type type;

    @JsonProperty("message")
    private String message;

    @JsonProperty("date")
    private Date date;

    @JsonProperty("is_processed")
    private boolean isProcessed;

    public Notification() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isProcessed() {
        return isProcessed;
    }

    public void setProcessed(boolean processed) {
        isProcessed = processed;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "id='" + id + '\'' +
                ", type=" + type +
                ", message='" + message + '\'' +
                ", date=" + date +
                ", isProcessed=" + isProcessed +
                '}';
    }
}
