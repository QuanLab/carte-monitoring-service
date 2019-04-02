package vn.five9.data.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class JobList {

    @JsonProperty("jobs")
    private List<Job> jobList;

    @JsonProperty("has_next")
    private boolean hasNext;

    public List<Job> getJobList() {
        return jobList;
    }

    public void setJobList(List<Job> jobList) {
        this.jobList = jobList;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    @Override
    public String toString() {
        return "JobList{" +
                "jobList=" + jobList +
                ", hasNext=" + hasNext +
                '}';
    }
}
