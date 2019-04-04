package vn.five9.data.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.pentaho.di.cluster.HttpUtil;
import vn.five9.data.config.Config;
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
     * get list job in from public repository
     * @param limit
     *  number of items will be return
     * @param offset
     *  offset from start of result
     * @return
     */
    public static JobList getJobList(int limit, int offset) {
        List<Job> list = new ArrayList<>();
        try {
            list = JobRepository.getJobs(limit, offset);
        }catch (Exception e) {
            e.printStackTrace();
        }
        list = updateJobStatus(list);

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
    private static List<Job> updateJobStatus(List<Job> list) {
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
        return list;
    }


    /**
     * search job using similar name
     * @param name
     * @param limit
     *  the number of jobs will be return
     * @return
     */
    public static List<Job> searchJobs(String name, int limit) {
        List<Job> list = new ArrayList<>();
        try {
            list = JobRepository.findJobs(name, limit);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * search job using similar name
     * @param name
     * @param limit
     *  the number of jobs will be return
     * @return
     */
    public static List<String> suggestJobName(String name, int limit) {
        List<String> list = new ArrayList<>();
        try {
            list = JobRepository.findJobByName(name, limit);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * search job using similar name
     * @param name
     * @param limit
     *  the number of jobs will be return
     * @return
     */
    public static List<Job> advancedSearchJobs(String term, String status, Date createdDate, int schedulerType, int limit) {
        List<Job> list = new ArrayList<>();
        try {
            list = JobRepository.findJobs(term, status, createdDate, schedulerType, limit);
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

    public static WebResult stopJob(String jobName) {
        WebResult webResult = null;
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
    public static boolean updateJob(Job job) {
        return JobRepository.updateJob(job);
    }

}
