package vn.five9.data.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import vn.five9.data.util.DateTimeAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Date;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "jobstatus")
public class JobStatus {

    @XmlElement(name = "jobname")
    @JsonProperty("job_name")
    private String jobName;

    @XmlElement(name = "id")
    private String id;

    @XmlElement(name = "status_desc")
    @JsonProperty("status_desc")
    private String statusDesc;

    @XmlElement(name = "logging_string")
    @JsonProperty("logging_string")
    private String loggingString;

    @XmlElement(name = "log_date")
    @JsonProperty("log_date")
    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    private Date logDate;

    @JsonProperty("job_image")
    private String jobImage;

    public JobStatus() {
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public String getLoggingString() {
        return loggingString;
    }

    public void setLoggingString(String loggingString) {
        this.loggingString = loggingString;
    }

    public Date getLogDate() {
        return logDate;
    }

    public void setLogDate(Date logDate) {
        this.logDate = logDate;
    }

    public String getJobImage() {
        return jobImage;
    }

    public void setJobImage(String jobImage) {
        this.jobImage = jobImage;
    }

    @Override
    public String toString() {
        return "JobStatus{" +
                "jobName='" + jobName + '\'' +
                ", id='" + id + '\'' +
                ", statusDesc='" + statusDesc + '\'' +
                ", loggingString='" + loggingString + '\'' +
                ", logDate=" + logDate +
                ", jobImage='" + jobImage + '\'' +
                '}';
    }
}
