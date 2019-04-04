package vn.five9.data.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import vn.five9.data.model.JobStatus;
import vn.five9.data.model.Notification;

import java.util.ArrayList;
import java.util.List;

/**
 * @author quanpv
 */
public class NotificationService {

    private static final Logger logger  = LogManager.getLogger();
    private static NotificationService notificationService = null;

    /**
     * singleton implementation
     * @return
     */
    public static NotificationService getInstance() {
        if (notificationService == null) {
            notificationService = new NotificationService();
        }
        return notificationService;
    }


    /**
     * get all notification base on type
     * @param type
     * @return
     */
    public List<Notification> getNotificationList(Notification.Type type) {
        List<String> jobStatusBlackList = new ArrayList<>();
        switch (type) {
            case INFO:
                jobStatusBlackList.add("STOPPED");
                jobStatusBlackList.add("FINISHED");
                break;
            case WARN:
                jobStatusBlackList.add("HALT");
                break;
            case ERROR:
                jobStatusBlackList.add("FINISHED(WITH ERRORS)");
                break;
        }

        List<Notification> notificationList = new ArrayList<>();
        List<JobStatus> jobStatusList = CarteService.getJobStatusSet();
        logger.info("Get notification from Carte server done");
        for(JobStatus jobStatus : jobStatusList) {
            if (jobStatusBlackList.contains(jobStatus.getStatusDesc().toUpperCase())) {
                Notification notification = new Notification();
                notification.setId(jobStatus.getJobName());
                notificationList.add(notification);
            }
        }
        return notificationList;
    }
}
