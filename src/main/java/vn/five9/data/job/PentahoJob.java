package vn.five9.data.job;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.*;
import vn.five9.data.service.JobService;

import java.util.Date;


public class PentahoJob implements Job {

    private static final Logger logger = LogManager.getLogger();

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String jobName = context.getJobDetail().getKey().getName();
        logger.info("Run \'" + jobName + "\' at: " + new Date());
//        JobService.startJob(jobName, null);
    }
}
