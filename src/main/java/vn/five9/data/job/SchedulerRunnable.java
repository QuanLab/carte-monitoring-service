package vn.five9.data.job;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import vn.five9.data.model.Job;
import vn.five9.data.service.JobService;

import java.util.List;

public class SchedulerRunnable implements Runnable {

    private static final Logger logger = LogManager.getLogger();
    private SchedulerManager schedulerManager;

    public SchedulerRunnable() {
        schedulerManager = SchedulerManager.getInstance();
        try {
            if (!schedulerManager.isStarted()) {
                schedulerManager.start();
            }
        }catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }


    @Override
    public void run() {
        try {
            List<Job> jobList = JobService.getListJobCronEnable();
            for (Job job : jobList) {
                schedulerManager.addJob(job);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}
