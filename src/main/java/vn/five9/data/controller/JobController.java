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
@RequestMapping("/api/v1/")
public class JobController {

    private static final Logger logger  = LogManager.getLogger();

    @RequestMapping("jobs")
    public JobList jobList(@RequestParam(value="offset", defaultValue="0") Integer offset,
                            @RequestParam(value="limit", defaultValue="20") Integer limit) {
        return JobService.getJobList(offset, limit);
    }

    @PostMapping("job")
    public ResponseData updateJob(@RequestBody Job job) {
        logger.info(job);
        ResponseData responseData = new ResponseData();
        try {
            JobService.updateJob(job);
            responseData.setStatus(1);
            responseData.setMessage("Job has been updated");
        } catch (Exception e) {
            responseData.setStatus(-1);
            responseData.setMessage(e.getMessage());
            logger.error(e.getMessage(), e);
        }
        return responseData;
    }

    @RequestMapping(value = "jobStatus", produces = "application/json")
    public JobStatus jobStatus(@RequestParam(value="name", defaultValue="") String name,
                               @RequestParam(value="id", defaultValue="") String id) {
        return JobService.getJobStatus(name, id);
    }

    @RequestMapping(value = "jobStatusList", produces = "application/json")
    public List<JobStatus> jobStatusList(@RequestParam(value="name", defaultValue="") String name) {
        return JobService.getJobStatusList(name);
    }

    @RequestMapping("advancedSearch")
    public List<Job> advancedSearchJobs(@RequestParam(value="q", defaultValue="") String term,
                                        @RequestParam(value="status", defaultValue="") String status,
                                        @RequestParam(value="from", defaultValue="") String from,
                                        @RequestParam(value="to", defaultValue="") String to,
                                        @RequestParam(value="scheduler_type", defaultValue="-1") int schedulerType,
                                @RequestParam(value="limit", defaultValue="10") int limit) {

        Date fromDate = null;
        Date toDate = null;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            fromDate = dateFormat.parse(from);
        }catch (Exception e) {
            logger.info("Created date is not set on filter condition");
        }

        try {
            toDate = dateFormat.parse(to);
        }catch (Exception e) {
            logger.info("To date is not set on filter condition");
        }

        return JobService.advancedSearchJobs(term, status, fromDate, toDate, schedulerType, limit);
    }

    @PostMapping("startJob")
    public ResponseData startJob(@RequestBody JobInstance jobInstance) {

        logger.info(jobInstance.toString());

        ResponseData responseData = new ResponseData();
        WebResult webResult = JobService.startJob(jobInstance.getName(), jobInstance.getInstanceId());
        logger.info(webResult);

        if("OK".equals(webResult.getResult())) {
            responseData.setStatus(1);
            responseData.setMessage("Job " + jobInstance.getName() + " started success");
        } else {
            responseData.setStatus(-1);
            responseData.setMessage(webResult.getMessage());
        }
        return responseData;
    }

    @RequestMapping("startJob")
    public ResponseData startJob(@RequestParam(value="name", defaultValue="") String jobName) {
        logger.info("jobName " + jobName);

        ResponseData responseData = new ResponseData();
        WebResult  webResult = JobService.startJob(jobName, null);
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

    @PostMapping("stopJob")
    public ResponseData stopJob(@RequestBody JobInstance jobInstance) {
        ResponseData responseData = new ResponseData();
        logger.info(jobInstance.toString());
        WebResult webResult = JobService.stopJob(jobInstance.getName(), jobInstance.getInstanceId());
        logger.info(webResult);
        if("OK".equals(webResult.getResult())) {
            responseData.setStatus(1);
            responseData.setMessage("Job " + jobInstance.getName() + " stopped success");
        } else {
            responseData.setStatus(-1);
            responseData.setMessage(webResult.getMessage());
        }
        return responseData;
    }

    @RequestMapping("stopJob")
    public ResponseData stopJob(@RequestParam(value="name", defaultValue="") String jobName,
                                @RequestParam(value="instanceId", defaultValue="") String instanceId) {
        ResponseData responseData = new ResponseData();
        logger.info("jobName " + jobName + "\tinstanceId\t" + instanceId);
        WebResult webResult = JobService.stopJob(jobName, instanceId);
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