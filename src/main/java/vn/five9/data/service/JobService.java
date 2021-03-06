package vn.five9.data.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.pentaho.di.cluster.HttpUtil;
import org.quartz.SchedulerException;
import vn.five9.data.config.Config;
import vn.five9.data.job.SchedulerManager;
import vn.five9.data.model.*;
import vn.five9.data.repository.JobRepository;
import vn.five9.data.util.RestUtil;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.net.URLEncoder;
import java.util.*;

/**
 * @author quanpv
 */
public class JobService {

    private static final Logger logger  = LogManager.getLogger();

    /**
     * get list job in from static repository
     * @param limit
     *  number of items will be return
     * @param offset
     *  offset from start of result
     * @return
     */
    public static JobList getJobList(Integer offset, Integer limit) {
        List<Job> list = new ArrayList<>();
        try {
            list = JobRepository.getJobs(offset, limit);
            list = updateJobStatus(list);
        }catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        JobList jobList = new JobList();
        if (list.size() > limit) {
            jobList.setJobList(list.subList(0, list.size() - 1));
            jobList.setHasNext(true);
        } else {
            jobList.setJobList(list);
            jobList.setHasNext(false);
        }
        return jobList;
    }


    /**
     * update job status by requesting to Carte Server to get status of job
     * @param list
     * @return
     */
    private static List<Job> updateJobStatus(List<Job> list) throws SchedulerException{
        List<JobStatus> jobStatusList = CarteService.getServerStatus().getJobStatusList();
        for(int i = 0; i < list.size(); i++) {
            Job job = list.get(i);
            Date lastExecuteDate = new Date(0);
            for(JobStatus jobStatus : jobStatusList) {
                if(job.getName().equals(jobStatus.getJobName())) {
                    if (lastExecuteDate.before(jobStatus.getLogDate())) {
                        lastExecuteDate = jobStatus.getLogDate();
                        job.setStatus(jobStatus.getStatusDesc());
                        list.set(i, job);
                    }
                }
            }
        }
        // get list job status running in scheduler manager
        // ignore because schedule manager only persit metadata about the job, exacly job running on Carte server
        // segment of code will be un-comment when running job in this API
//        List<Job> activeJob = SchedulerManager.getInstance().getListJobs();
//        for(int i = 0; i < list.size(); i++) {
//            Job job = list.get(i);
//            for(Job jobStatus : activeJob) {
//                if(job.getName().equals(jobStatus.getName())) {
//                    job.setStatus("Running");
//                    list.set(i, job);
//                }
//            }
//        }
        return list;
    }

    /**
     * search job using similar name
     * @param term
     * @param limit
     *  the number of jobs will be return
     * @return
     */
    public static List<Job> advancedSearchJobs(String term, String status, Date fromDate, Date toDate, int schedulerType, int limit) {
        List<Job> list = new ArrayList<>();
        try {
            list = JobRepository.findJobs(term, status, fromDate, toDate, schedulerType, limit);
        }catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return list;
    }

    /**
     * start a job
     * @param jobName
     *  name of job to start
     */
    public static WebResult startJob(String jobName, String instanceId) {
        String url = Config.CarteHost + Config.CarteStartJob.replace("{{jobName}}", jobName);
        if(instanceId != null) {
            url = url + "&id=" + instanceId;
        }
        try {
            String message = RestUtil.get(url);
            JAXBContext jaxbContext = JAXBContext.newInstance(WebResult.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            return (WebResult)unmarshaller.unmarshal(new StringReader(message));
        }catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * get status of instance currently running
     * @param jobName
     *  name of job to start
     * @param instanceId
     *  id of instance running
     */
    public static JobStatus getJobStatus(String jobName, String instanceId) {
        try {
            String url = Config.CarteHost + Config.CarteJobStatus.replace("{{jobName}}", URLEncoder.encode(jobName, "UTF-8"))
                    .replace("{{id}}", instanceId);
            String message = RestUtil.get(url);
            JAXBContext jaxbContext = JAXBContext.newInstance(JobStatus.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            JobStatus jobStatus = (JobStatus)unmarshaller.unmarshal(new StringReader(message));

            try {
                String loggingString = jobStatus.getLoggingString();
                loggingString = HttpUtil.decodeBase64ZippedString(loggingString.substring(9, loggingString.length() - 3));
                jobStatus.setLoggingString(loggingString);
            }catch (Exception e) {
                e.printStackTrace();
            }

            String imageUrl = Config.CarteHost + Config.CarteJobImage.replace("{{jobName}}", URLEncoder.encode(jobName, "UTF-8"))
                    .replace("{{id}}", jobStatus.getId());
            jobStatus.setJobImage(RestUtil.getByteArrayFromImageURL(imageUrl));
            return jobStatus;
        }catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }


    /**
     * get all status of instances by name of job
     * @param jobName
     *  name of job to start
     */
    public static List<JobStatus> getJobStatusList(String jobName) {

        List<JobStatus> list = CarteService.getServerStatus().getJobStatusList();
        List<JobStatus> result = new ArrayList<>();
        //id, status, last log_date
        for(int i =0 ; i < list.size(); i++) {
            JobStatus jobStatus = list.get(i);
            if(jobStatus.getJobName().equals(jobName)) {
                result.add(jobStatus);
            }
        }
        return result;
    }

    /**
     * stop a running job
     * @param jobName
     *  name of job to stop
     * @param instanceId
     *  id of instance, it is unique
     */
    public static WebResult stopJob(String jobName, String instanceId) {
        try {
            if(instanceId == null) {
                return stopJob(jobName);
            } else {
                String url = Config.CarteHost + Config.CarteStopJob.replace("{{jobName}}", URLEncoder.encode(jobName, "UTF-8"))
                        .replace("{{id}}", instanceId);
                String message = RestUtil.get(url);
                JAXBContext jaxbContext = JAXBContext.newInstance(WebResult.class);
                Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
                return (WebResult)unmarshaller.unmarshal(new StringReader(message));
            }
        }catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     *
     * @param jobName
     * @return
     * @throws Exception
     */
    public static WebResult stopJob(String jobName) throws Exception {
        WebResult webResult = new WebResult();
        Job job = new Job();
        job.setName(jobName);

        SchedulerManager schedulerManager = SchedulerManager.getInstance();
        if (schedulerManager.isExists(job)) {
            schedulerManager.removeJob(job);
        }

        List<JobStatus> jobStatusList = getJobStatusList(jobName);
        for(JobStatus jobStatus: jobStatusList) {
            if("RUNNING".equals(jobStatus.getStatusDesc().toUpperCase()) && jobStatus.getId()!= null) {
                webResult = stopJob(jobStatus.getJobName(), jobStatus.getId());
                if(!"OK".equals(webResult.getResult())) {
                    return webResult;
                }
            }
        }
        return webResult;
    }

    /**
     * update Job Scheduler
     * @param job
     * @return
     */
    public static void updateJob(Job job) throws Exception {
        JobRepository.updateJobScheduler(job);
    }

    /**
     *
     * @return
     */
    public static List<Job> getListJobCronEnable() {
        return JobRepository.getListJobCronEnable();
    }


    /**
     *
     * @return
     */
    public static Map<String, Job> getMapJobCronEnable() {
        return JobRepository.getMapJobCronEnable();
    }


}
