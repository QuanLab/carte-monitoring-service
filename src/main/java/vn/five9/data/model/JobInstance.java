package vn.five9.data.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JobInstance {

    @JsonProperty("instanceid")
    private String instanceId;

    @JsonProperty("name")
    private String name;

    public JobInstance() {
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "JobInstance{" +
                "instanceId='" + instanceId + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
