package vn.five9.data.model;

import javax.xml.bind.annotation.*;
import java.util.List;


@XmlRootElement(name = "serverstatus")
@XmlAccessorType(XmlAccessType.FIELD)
public class ServerStatus {

    @XmlElement(name="statusdesc")
    private String statusDesc;

    @XmlElementWrapper(name = "jobstatuslist")
    @XmlElement(name="jobstatus")
    private List<JobStatus> jobStatusList;

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public List<JobStatus> getJobStatusList() {
        return jobStatusList;
    }

    public void setJobStatusList(List<JobStatus> jobStatusList) {
        this.jobStatusList = jobStatusList;
    }

    @Override
    public String toString() {
        return "ServerStatus{" +
                "statusDesc='" + statusDesc + '\'' +
                ", jobStatusList=" + jobStatusList +
                '}';
    }
}
