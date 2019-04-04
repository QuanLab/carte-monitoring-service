package vn.five9.data.controller;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.five9.data.model.Notification;
import vn.five9.data.service.NotificationService;

import java.util.List;


@RestController
public class NotificationController {

    private static final Logger logger  = LogManager.getLogger();

    @RequestMapping("/api/v1/notificationList")
    public List<Notification> notificationList(@RequestParam(value="type", defaultValue="ERROR") String type) {
        logger.info("Type: " + type);
        Notification.Type noType = Notification.Type.ERROR;
        for (Notification.Type baseType : Notification.Type.values()) {
            if(type.equals(baseType.toString())) {
                noType = baseType;
            }
        }
        return NotificationService.getInstance().getNotificationList(noType);
    }

}
