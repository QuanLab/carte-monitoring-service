package vn.five9.data.service;

import vn.five9.data.config.Config;
import vn.five9.data.model.JobStatus;
import vn.five9.data.model.ServerStatus;
import vn.five9.data.util.RestUtil;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class CarteService {


    /**
     * Get server status from Carte server
     * @return
     */
    public static ServerStatus getServerStatus() {
        ServerStatus serverStatus = new ServerStatus();
        String url = Config.CarteHost + Config.CarteKettleStatus;
        try {
            String message = RestUtil.get(url);
            JAXBContext jaxbContext = JAXBContext.newInstance(ServerStatus.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            serverStatus = (ServerStatus)unmarshaller.unmarshal(new StringReader(message));
        }catch (Exception e) {
            e.printStackTrace();
        }
        return serverStatus;
    }

    /**
     * get all status of instances by name of job
     *  name of job to start
     */
    public static List<JobStatus> getJobStatusSet() {
        List<JobStatus> list = CarteService.getServerStatus().getJobStatusList();
        Map<String,JobStatus> jobStatusMap = new HashMap<>();
        for(JobStatus jobStatus : list) {
            if(jobStatusMap.containsKey(jobStatus.getJobName())) {
                JobStatus tmp = jobStatusMap.get(jobStatus.getJobName());

                if(jobStatus.getLogDate().before(tmp.getLogDate())) {
                    jobStatusMap.put(jobStatus.getJobName(), jobStatus);
                }

            } else {
                jobStatusMap.put(jobStatus.getJobName(), jobStatus);
            }
        }
        return jobStatusMap.values().stream().collect(Collectors.toList());
    }

}
