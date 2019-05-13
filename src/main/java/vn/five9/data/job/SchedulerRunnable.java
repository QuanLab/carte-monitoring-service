package vn.five9.data.job;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import vn.five9.data.model.Job;
import vn.five9.data.service.JobService;

import java.util.HashMap;
import java.util.Map;


public class SchedulerRunnable implements Runnable {

    private static final Logger logger = LogManager.getLogger();
    private SchedulerManager schedulerManager;
    private static Map<String, Job> jobMapCronEnableOld;

    public SchedulerRunnable() {
        schedulerManager = SchedulerManager.getInstance();
        try {
            if (!schedulerManager.isStarted()) {
                schedulerManager.start();
                jobMapCronEnableOld = new HashMap<>();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }


    @Override
    public void run() {
        try {
            Map<String, Job> jobMapCronNew = JobService.getMapJobCronEnable();

            //remove old job
            Map<String, Job> jobMapCurrent = schedulerManager.getMapJobs();

            for (String key : jobMapCurrent.keySet()) {
                Job job = jobMapCurrent.get(key);
                if (!jobMapCronNew.containsKey(key)) {
                    logger.info("Remove job from scheduler: " + job.getName());
                    schedulerManager.removeJob(job);
                }
            }

            //add new job if not exists
            for (String key : jobMapCronNew.keySet()) {
                if (jobMapCronEnableOld.containsKey(key)) {
                    Job jobNew = jobMapCronNew.get(key);
                    Job jobOld = jobMapCronEnableOld.get(key);

                    boolean isUpdate = false;
                    if (!jobNew.getCron().equals(jobOld.getCron())) {
                        isUpdate = true;
                    }

                    if(jobNew.getCronStartDate() == null) {
                        //start date is null
                        if(jobOld.getCronStartDate()!= null) {
                            isUpdate = true;
                        }
                        if(jobNew.getCronEndDate() == null) {
                            if(jobOld.getCronEndDate() != null) {
                                isUpdate = true;
                            }
                        } else {
                            if(!jobNew.getCronEndDate().equals(jobOld.getCronEndDate())) {
                                isUpdate = true;
                            }
                        }

                    } else {
                        //start date not null
                        if(!jobNew.getCronStartDate().equals(jobOld.getCronStartDate())) {
                            isUpdate = true;
                        }

                        if(jobNew.getCronEndDate() == null) {
                            if(jobOld.getCronEndDate()!= null) {
                                isUpdate = true;
                            }
                        } else {
                            if(!jobNew.getCronEndDate().equals(jobOld.getCronEndDate())) {
                                isUpdate = true;
                            }
                        }
                    }

                    if(isUpdate) {
                        logger.info("Update schedule for job: " + jobNew.getName());
                        schedulerManager.updateJob(jobNew);
                    }
                } else {
                    logger.info("Add job to scheduler: " + key);
                    schedulerManager.addJob(jobMapCronNew.get(key));
                }
            }
            jobMapCronEnableOld = jobMapCronNew;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}
