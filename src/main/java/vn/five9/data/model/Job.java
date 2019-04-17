package vn.five9.data.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;


public class Job {
    @JsonProperty("id")
    private int ID;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("created_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdDate;

    @JsonProperty("modified_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modifiedDate;

    @JsonProperty("scheduler_type")
    private int schedulerType;

    @JsonProperty("is_repeat")
    private String isRepeat;

    @JsonProperty("cron_enable")
    private boolean cronEnable;

    @JsonProperty("cron")
    private String cron;

    @JsonProperty("cron_start")
    private Date cronStartDate;

    @JsonProperty("cron_end")
    private Date cronEndDate;

    @JsonProperty("interval_seconds")
    private int intervalSeconds;

    @JsonProperty("interval_minutes")
    private int intervalMinutes;

    @JsonProperty("hours")
    private int hours;

    @JsonProperty("minutes")
    private int minutes;

    @JsonProperty("week_day")
    private int weekDay;

    @JsonProperty("day_of_month")
    private int dayOfMonth;

    @JsonProperty("status")
    private String status;

    public Job() {
        this.status = "N/A";
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public int getSchedulerType() {
        return schedulerType;
    }

    public void setSchedulerType(int schedulerType) {
        this.schedulerType = schedulerType;
    }

    public String getIsRepeat() {
        return isRepeat;
    }

    public void setIsRepeat(String isRepeat) {
        this.isRepeat = isRepeat;
    }

    public boolean isCronEnable() {
        return cronEnable;
    }

    public void setCronEnable(boolean cronEnable) {
        this.cronEnable = cronEnable;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public Date getCronStartDate() {
        return cronStartDate;
    }

    public void setCronStartDate(Date cronStartDate) {
        this.cronStartDate = cronStartDate;
    }

    public Date getCronEndDate() {
        return cronEndDate;
    }

    public void setCronEndDate(Date cronEndDate) {
        this.cronEndDate = cronEndDate;
    }

    public int getIntervalSeconds() {
        return intervalSeconds;
    }

    public void setIntervalSeconds(int intervalSeconds) {
        this.intervalSeconds = intervalSeconds;
    }

    public int getIntervalMinutes() {
        return intervalMinutes;
    }

    public void setIntervalMinutes(int intervalMinutes) {
        this.intervalMinutes = intervalMinutes;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(int weekDay) {
        this.weekDay = weekDay;
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Job{" +
                "ID=" + ID +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", createdDate=" + createdDate +
                ", modifiedDate=" + modifiedDate +
                ", schedulerType=" + schedulerType +
                ", isRepeat='" + isRepeat + '\'' +
                ", cronEnable=" + cronEnable +
                ", cron='" + cron + '\'' +
                ", cronStartDate=" + cronStartDate +
                ", cronEndDate=" + cronEndDate +
                ", intervalSeconds=" + intervalSeconds +
                ", intervalMinutes=" + intervalMinutes +
                ", hours=" + hours +
                ", minutes=" + minutes +
                ", weekDay=" + weekDay +
                ", dayOfMonth=" + dayOfMonth +
                ", status='" + status + '\'' +
                '}';
    }
}
