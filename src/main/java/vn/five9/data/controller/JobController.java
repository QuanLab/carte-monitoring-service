package vn.five9.data.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;
import vn.five9.data.model.*;
import vn.five9.data.service.JobService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
public class JobController {

    private static final Logger logger  = LogManager.getLogger();

    @RequestMapping("/api/v1/jobs")
    public JobList jobList(@RequestParam(value="limit", defaultValue="20") int limit,
                            @RequestParam(value="offset", defaultValue="0") int offset) {

        return JobService.getJobList(limit, offset);
    }

    @PostMapping("/api/v1/job")
    public ResponseData updateJob(@RequestBody Job job) {
        System.out.println(job.toString());
        ResponseData responseData = new ResponseData();
        boolean status = JobService.updateJob(job);
        if(status) {
            responseData.setStatus(1);
            responseData.setMessage("Job has been updated");
        } else {
            responseData.setStatus(-1);
            responseData.setMessage("Failed to update job");
        }
        return responseData;
    }

    @RequestMapping(value = "/api/v1/jobStatus", produces = "application/json")
    public JobStatus jobStatus(@RequestParam(value="name", defaultValue="") String name,
                               @RequestParam(value="id", defaultValue="") String id) {
        return JobService.getJobStatus(name, id);
    }

    @RequestMapping(value = "/api/v1/jobStatusList", produces = "application/json")
    public List<JobStatus> jobStatusList(@RequestParam(value="name", defaultValue="") String name) {
        return JobService.getJobStatusList(name);
    }


    @RequestMapping("/api/v1/search")
    public List<Job> searchJobs(@RequestParam(value="q", defaultValue="") String term,
                             @RequestParam(value="limit", defaultValue="10") int limit) {
        return JobService.searchJobs(term, limit);
    }

    @RequestMapping("/api/v1/advancedSearch")
    public List<Job> advancedSearchJobs(@RequestParam(value="q", defaultValue="") String term,
                                        @RequestParam(value="status", defaultValue="") String status,
                                        @RequestParam(value="created_date", defaultValue="") String createdDate,
                                        @RequestParam(value="scheduler_type", defaultValue="-1") int schedulerType,
                                @RequestParam(value="limit", defaultValue="10") int limit) {

        Date created = null;
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            created = dateFormat.parse(createdDate);
        }catch (Exception e) {
            logger.info("Created date is not set on filter condition");
        }

        return JobService.advancedSearchJobs(term, status, created, schedulerType, limit);
    }

    @PostMapping("/api/v1/startJob")
    public ResponseData startJob(@RequestParam(value="name", defaultValue="") String jobName,
                                 @RequestBody JobInstance jobInstance) {

        logger.info(jobInstance.toString());
        logger.info("jobName " + jobName);

        ResponseData responseData = new ResponseData();
        WebResult webResult;

        if(jobInstance.getInstanceId()!= null) {
            webResult = JobService.startJob(jobInstance.getName(), jobInstance.getInstanceId());
        } else {
            webResult = JobService.startJob(jobName, null);
        }
        logger.info(webResult);

        if("OK".equals(webResult.getResult())) {
            responseData.setStatus(1);
            responseData.setMessage("Job " + jobName + " started success");
        } else {
            responseData.setStatus(-1);
            responseData.setMessage(webResult.getMessage());
        }
        return responseData;
    }

    @PostMapping("/api/v1/stopJob")
    public ResponseData stopJob(@RequestParam(value="name", defaultValue="") String jobName,
                              @RequestParam(value="instanceId", defaultValue="") String instanceId,
                                @RequestBody JobInstance jobInstance) {
        ResponseData responseData = new ResponseData();
        logger.info("jobName " + jobName + "\tinstanceId\t" + instanceId);
        WebResult webResult;
        if (jobInstance.getInstanceId()!= null) {
            webResult = JobService.stopJob(jobName, instanceId);
        } else {
            webResult = JobService.stopJob(jobInstance.getName(), jobInstance.getInstanceId());
        }
        logger.info(webResult);
        if("OK".equals(webResult.getResult())) {
            responseData.setStatus(1);
            responseData.setMessage("Job " + jobName + " stopped success");
        } else {
            responseData.setStatus(-1);
            responseData.setMessage(webResult.getMessage());
        }
        return responseData;
    }
}