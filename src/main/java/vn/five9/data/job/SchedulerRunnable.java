package vn.five9.data.job;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import vn.five9.data.model.Job;
import vn.five9.data.service.JobService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SchedulerRunnable implements Runnable {

    private static final Logger logger = LogManager.getLogger();
    private SchedulerManager schedulerManager;
    private static List<Job> jobCronEnableOld;

    public SchedulerRunnable() {
        schedulerManager = SchedulerManager.getInstance();
        try {
            if (!schedulerManager.isStarted()) {
                schedulerManager.start();
            }
            jobCronEnableOld = new ArrayList<>();
        }catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }


    @Override
    public void run() {
        try {
            List<Job> jobCronEnable = JobService.getListJobCronEnable();

            logger.info(jobCronEnableOld.size());
            List<Job> jobToRemove = jobCronEnableOld.stream().filter(job -> !jobCronEnable.contains(job))
                    .collect(Collectors.toList());
            for(Job job : jobToRemove) {
                logger.info("Remove job from scheduler " + job.getName());
                schedulerManager.removeJob(job);
            }

            for(Job job : jobCronEnable) {
                int index = jobCronEnableOld.indexOf(job);
                if(index == -1) {
                    continue;
                }
                Job oldJob = jobCronEnableOld.get(index);
                logger.info(jobCronEnableOld.indexOf(job));
                if(!job.getCron().equals(oldJob.getCron())
                        || job.getCronStartDate() != oldJob.getCronStartDate()
                        || job.getCronEndDate() != oldJob.getCronEndDate()) {
                    logger.info(">>>=====================>>>   Detect changed");
                }
            }
            jobCronEnableOld = jobCronEnable;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}
