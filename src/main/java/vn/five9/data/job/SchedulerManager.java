package vn.five9.data.job;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;
import vn.five9.data.model.Job;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Global scheduler manager
 * This class responsible for scheduling each time scan database and update scheduler of all the jobs
 */
public class SchedulerManager {

    private static final Logger logger = LogManager.getLogger();
    private static SchedulerManager instance = null;
    private Scheduler scheduler = null;

    private SchedulerManager() {
        try {
            init();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    public static SchedulerManager getInstance() {
        if (instance == null) {
            instance = new SchedulerManager();
        }
        return instance;
    }

    /**
     * init scheduler
     *
     * @throws SchedulerException
     */
    private void init() throws SchedulerException {
        scheduler = new StdSchedulerFactory().getScheduler();
    }

    /**
     * add new scheduler for job if not exist, otherwise update it
     *
     * @param job
     */
    public void addJob(Job job) throws SchedulerException {
        JobKey jobKey = new JobKey(job.getName());
        if (scheduler.checkExists(jobKey)) {
            return;
        }
        JobDetail job1 = JobBuilder.newJob(PentahoJob.class)
                .withIdentity(jobKey)
                .build();

        TriggerBuilder<CronTrigger> triggerBuilder = TriggerBuilder.newTrigger()
                .withIdentity(new TriggerKey(job.getName()))
                .withSchedule(CronScheduleBuilder.cronSchedule(job.getCron()));

        if (job.getCronStartDate() != null) {
            triggerBuilder.startAt(job.getCronStartDate());
        }
        if (job.getCronEndDate() != null) {
            triggerBuilder.endAt(job.getCronEndDate());
        }
        Trigger trigger = triggerBuilder.build();
        scheduler.scheduleJob(job1, trigger);
    }

    /**
     * add new scheduler for job if not exist, otherwise update it
     *
     * @param job
     */
    public void updateJob(Job job) throws SchedulerException {
        JobKey jobKey = new JobKey(job.getName());
        if (scheduler.checkExists(jobKey)) {
            removeJob(job);
        }
        JobDetail job1 = JobBuilder.newJob(PentahoJob.class)
                .withIdentity(jobKey)
                .build();

        TriggerBuilder<CronTrigger> triggerBuilder = TriggerBuilder.newTrigger()
                .withIdentity(new TriggerKey(job.getName()))
                .withSchedule(CronScheduleBuilder.cronSchedule(job.getCron()));

        if (job.getCronStartDate() != null) {
            triggerBuilder.startAt(job.getCronStartDate());
        }
        if (job.getCronEndDate() != null) {
            triggerBuilder.endAt(job.getCronEndDate());
        }
        Trigger trigger = triggerBuilder.build();
        scheduler.scheduleJob(job1, trigger);
    }


    /**
     * check if job is exists in scheduler
     *
     * @param job
     * @return
     * @throws SchedulerException
     */
    public boolean isExists(Job job) throws SchedulerException {
        JobKey jobKey = new JobKey(job.getName());
        return scheduler.checkExists(jobKey);
    }


    /**
     *
     * @param job
     * @return
     * @throws SchedulerException
     */
    public boolean isRunning(Job job) throws SchedulerException {
        List<JobExecutionContext> currentJobs = scheduler.getCurrentlyExecutingJobs();
        for (JobExecutionContext jobCtx : currentJobs) {
            String thisJobName = jobCtx.getJobDetail().getKey().getName();
            if (job.getName().equalsIgnoreCase(thisJobName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * remove job from scheduler
     *
     * @param job
     * @throws SchedulerException
     */
    public void removeJob(Job job) throws SchedulerException {
        JobKey jobKey = new JobKey(job.getName());
        if (scheduler.checkExists(jobKey)) {
            scheduler.deleteJob(jobKey);
        }
    }

    /**
     * @throws SchedulerException
     */
    public void start() throws SchedulerException {
        scheduler.start();
    }

    /**
     * @return
     * @throws SchedulerException
     */
    public boolean isStarted() throws SchedulerException {
        return scheduler.isStarted();
    }

    /**
     * @return
     * @throws SchedulerException
     */
    public List<Job> getListJobs() throws SchedulerException {
        List<Job> jobList = new ArrayList<>();
        for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.anyGroup())) {
            Job job = new Job();
            job.setName(jobKey.getName());
            jobList.add(job);
        }
        return jobList;
    }

    /**
     * @return
     * @throws SchedulerException
     */
    public Map<String, Job> getMapJobs() throws SchedulerException {
        Map<String, Job> jobMap = new HashMap<>();
        for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.anyGroup())) {
            Job job = new Job();
            job.setName(jobKey.getName());
            jobMap.put(job.getName(), job);
        }
        return jobMap;
    }
}
