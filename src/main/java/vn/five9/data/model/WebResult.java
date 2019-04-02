package vn.five9.data.model;

import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "webresult")
public class WebResult {
    private String result;

    private String message;

    private String id;

    public WebResult() {

    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "WebResult{" +
                "result='" + result + '\'' +
                ", message='" + message + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
