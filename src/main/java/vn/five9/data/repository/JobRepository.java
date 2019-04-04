package vn.five9.data.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import vn.five9.data.db.ConnectionProvider;
import vn.five9.data.model.Job;
import vn.five9.data.model.JobStatus;
import vn.five9.data.service.CarteService;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class JobRepository {

    private static final Logger logger = LogManager.getLogger();

    /**
     * get list of job
     */
    public static List<Job> getJobs(int limit, int offset) throws SQLException {
        String query = "SELECT" +
                "        A.ID_JOB," +
                "        NAME," +
                "        CASE WHEN DESCRIPTION IS NULL THEN '' ELSE DESCRIPTION END AS DESCRIPTION," +
                "        CREATED_DATE," +
                "        MODIFIED_DATE," +
                "        CASE WHEN B.VALUE_NUM IS NULL THEN 0 ELSE B.VALUE_NUM END AS SCHEDULER_TYPE," +
                "        CASE WHEN C.VALUE_STR IS NULL THEN 'N' ELSE C.VALUE_STR END AS IS_REPEAT," +
                "        CASE WHEN D.VALUE_NUM IS NULL THEN 0 ELSE D.VALUE_NUM END AS INTERVAL_SECONDS," +
                "        CASE WHEN E.VALUE_NUM IS NULL THEN 0 ELSE E.VALUE_NUM END AS INTERVAL_MINUTES," +
                "        CASE WHEN F.VALUE_NUM IS NULL THEN 0 ELSE F.VALUE_NUM END AS HOUR," +
                "        CASE WHEN G.VALUE_NUM IS NULL THEN 0 ELSE G.VALUE_NUM END AS MINUTES," +
                "        CASE WHEN H.VALUE_NUM IS NULL THEN 0 ELSE H.VALUE_NUM END AS WEEK_DAY," +
                "        CASE WHEN I.VALUE_NUM IS NULL THEN 0 ELSE I.VALUE_NUM END AS DAY_OF_MONTH " +
                "FROM (" +
                "                SELECT ID_JOB, NAME, DESCRIPTION, CREATED_DATE, MODIFIED_DATE FROM R_JOB" +
                ") AS A " +
                "LEFT JOIN (" +
                "        SELECT ID_JOB, VALUE_NUM FROM R_JOBENTRY_ATTRIBUTE WHERE CODE = 'schedulerType'" +
                ") AS B ON  A.ID_JOB = B.ID_JOB " +
                "LEFT JOIN (" +
                "        SELECT ID_JOB, VALUE_STR FROM R_JOBENTRY_ATTRIBUTE WHERE CODE = 'repeat'" +
                ") AS C ON  A.ID_JOB = C.ID_JOB " +
                "LEFT JOIN (" +
                "        SELECT ID_JOB, VALUE_NUM FROM R_JOBENTRY_ATTRIBUTE WHERE CODE = 'intervalSeconds'" +
                ") AS D ON  A.ID_JOB = D.ID_JOB " +
                "LEFT JOIN (" +
                "        SELECT ID_JOB, VALUE_NUM FROM R_JOBENTRY_ATTRIBUTE WHERE CODE = 'intervalMinutes'" +
                ") AS E ON  A.ID_JOB = E.ID_JOB " +
                "LEFT JOIN (" +
                "        SELECT ID_JOB, VALUE_NUM FROM R_JOBENTRY_ATTRIBUTE WHERE CODE = 'hour'" +
                ") AS F ON  A.ID_JOB = F.ID_JOB " +
                "LEFT JOIN (" +
                "        SELECT ID_JOB, VALUE_NUM FROM R_JOBENTRY_ATTRIBUTE WHERE CODE = 'minutes'" +
                ") AS G ON  A.ID_JOB = G.ID_JOB " +
                "LEFT JOIN (" +
                "         SELECT ID_JOB, VALUE_NUM FROM R_JOBENTRY_ATTRIBUTE WHERE CODE = 'weekDay'" +
                ") AS H ON  A.ID_JOB = H.ID_JOB " +
                "LEFT JOIN (" +
                "          SELECT ID_JOB, VALUE_NUM FROM R_JOBENTRY_ATTRIBUTE WHERE CODE = 'dayOfMonth'" +
                ") AS I ON  A.ID_JOB = I.ID_JOB " +
                "LIMIT ? OFFSET ? ;";

        Connection conn = ConnectionProvider.getConnection();
        List<Job> jobList = new ArrayList<>();
        PreparedStatement pst = conn.prepareStatement(query);
        pst.setInt(1, limit + 1);
        pst.setInt(2, offset);
        ResultSet rs = pst.executeQuery();

        while (rs.next()) {
            Job job = new Job();
            job.setID(rs.getInt("ID_JOB"));
            job.setName(rs.getString("NAME"));
            job.setDescription(rs.getString("DESCRIPTION"));
            job.setCreatedDate(rs.getDate("CREATED_DATE"));
            job.setModifiedDate(rs.getDate("MODIFIED_DATE"));
            job.setIsRepeat(rs.getString("IS_REPEAT"));
            job.setSchedulerType(rs.getInt("SCHEDULER_TYPE"));
            job.setIntervalSeconds(rs.getInt("INTERVAL_SECONDS"));
            job.setIntervalMinutes(rs.getInt("INTERVAL_MINUTES"));
            job.setHours(rs.getInt("HOUR"));
            job.setMinutes(rs.getInt("MINUTES"));
            job.setWeekDay(rs.getInt("WEEK_DAY"));
            job.setDayOfMonth(rs.getInt("DAY_OF_MONTH"));
            jobList.add(job);
        }
        conn.close();
        return jobList;
    }


    /**
     * get list of job
     */
    public static List<Job> findJobs(String name, int limit) throws SQLException {
        String query =
                "SELECT" +
                        "        A.ID_JOB," +
                        "        NAME," +
                        "        CASE WHEN DESCRIPTION IS NULL THEN '' ELSE DESCRIPTION END AS DESCRIPTION," +
                        "        CREATED_DATE," +
                        "        MODIFIED_DATE," +
                        "        CASE WHEN B.VALUE_NUM IS NULL THEN 0 ELSE B.VALUE_NUM END AS SCHEDULER_TYPE," +
                        "        CASE WHEN C.VALUE_STR IS NULL THEN 'N' ELSE C.VALUE_STR END AS IS_REPEAT," +
                        "        CASE WHEN D.VALUE_NUM IS NULL THEN 0 ELSE D.VALUE_NUM END AS INTERVAL_SECONDS," +
                        "        CASE WHEN E.VALUE_NUM  IS NULL THEN 0 ELSE E.VALUE_NUM END AS INTERVAL_MINUTES," +
                        "        CASE WHEN F.VALUE_NUM  IS NULL THEN 0 ELSE F.VALUE_NUM END AS HOUR," +
                        "        CASE WHEN G.VALUE_NUM IS NULL THEN 0 ELSE G.VALUE_NUM END AS MINUTES," +
                        "        CASE WHEN H.VALUE_NUM IS NULL THEN 0 ELSE H.VALUE_NUM END AS WEEK_DAY," +
                        "        CASE WHEN I.VALUE_NUM IS NULL THEN 0 ELSE I.VALUE_NUM END AS DAY_OF_MONTH " +
                        "FROM (" +
                        "                SELECT ID_JOB, NAME, DESCRIPTION, CREATED_DATE, MODIFIED_DATE FROM R_JOB WHERE NAME LIKE ?" +
                        ") AS A" +
                        "        LEFT JOIN (" +
                        "                SELECT ID_JOB, VALUE_NUM FROM R_JOBENTRY_ATTRIBUTE WHERE CODE = 'schedulerType'" +
                        ") AS B ON  A.ID_JOB = B.ID_JOB" +
                        "        LEFT JOIN (" +
                        "                SELECT ID_JOB, VALUE_STR FROM R_JOBENTRY_ATTRIBUTE WHERE CODE = 'repeat'" +
                        ") AS C ON  A.ID_JOB = C.ID_JOB" +
                        "        LEFT JOIN (" +
                        "        SELECT ID_JOB, VALUE_NUM FROM R_JOBENTRY_ATTRIBUTE WHERE CODE = 'intervalSeconds'" +
                        ") AS D ON  A.ID_JOB = D.ID_JOB" +
                        "        LEFT JOIN (" +
                        "                SELECT ID_JOB, VALUE_NUM FROM R_JOBENTRY_ATTRIBUTE WHERE CODE = 'intervalMinutes'" +
                        ") AS E ON  A.ID_JOB = E.ID_JOB" +
                        "        LEFT JOIN (" +
                        "                SELECT ID_JOB, VALUE_NUM FROM R_JOBENTRY_ATTRIBUTE WHERE CODE = 'hour'" +
                        ") AS F ON  A.ID_JOB = F.ID_JOB" +
                        "        LEFT JOIN (" +
                        "                SELECT ID_JOB, VALUE_NUM FROM R_JOBENTRY_ATTRIBUTE WHERE CODE = 'minutes'" +
                        ") AS G ON  A.ID_JOB = G.ID_JOB " +
                        "        LEFT JOIN (" +
                        "                SELECT ID_JOB, VALUE_NUM FROM R_JOBENTRY_ATTRIBUTE WHERE CODE = 'weekDay'" +
                        ") AS H ON  A.ID_JOB = H.ID_JOB" +
                        "        LEFT JOIN (" +
                        "                SELECT ID_JOB, VALUE_NUM FROM R_JOBENTRY_ATTRIBUTE WHERE CODE = 'dayOfMonth'\n" +
                        ") AS I ON  A.ID_JOB = I.ID_JOB" +
                        "        LIMIT ?;";

        Connection conn = ConnectionProvider.getConnection();
        List<Job> jobList = new ArrayList<>();
        PreparedStatement pst = conn.prepareStatement(query);
        pst.setString(1, "%" + name + "%");
        pst.setInt(2, limit);
        ResultSet rs = pst.executeQuery();

        while (rs.next()) {
            Job job = new Job();
            job.setID(rs.getInt("ID_JOB"));
            job.setName(rs.getString("NAME"));
            job.setDescription(rs.getString("DESCRIPTION"));
            job.setCreatedDate(rs.getDate("CREATED_DATE"));
            job.setModifiedDate(rs.getDate("MODIFIED_DATE"));
            job.setSchedulerType(rs.getInt("SCHEDULER_TYPE"));
            job.setIsRepeat(rs.getString("IS_REPEAT"));
            job.setIntervalSeconds(rs.getInt("INTERVAL_SECONDS"));
            job.setIntervalMinutes(rs.getInt("INTERVAL_MINUTES"));
            job.setHours(rs.getInt("HOUR"));
            job.setMinutes(rs.getInt("MINUTES"));
            job.setWeekDay(rs.getInt("WEEK_DAY"));
            job.setDayOfMonth(rs.getInt("DAY_OF_MONTH"));
            jobList.add(job);
        }
        conn.close();
        return jobList;
    }

    /**
     * get list of job
     */
    public static List<Job> findJobs(String term, String status, Date createdDate, int schedulerType, int limit)
            throws SQLException, NullPointerException {
        List<Job> jobList = new ArrayList<>();
        StringBuilder query =  new StringBuilder();
        query.append("SELECT" +
                "        A.ID_JOB," +
                "        NAME," +
                "        CASE WHEN DESCRIPTION IS NULL THEN '' ELSE DESCRIPTION END AS DESCRIPTION," +
                "        CREATED_DATE," +
                "        MODIFIED_DATE," +
                "        CASE WHEN B.VALUE_NUM IS NULL THEN 0 ELSE B.VALUE_NUM END AS SCHEDULER_TYPE," +
                "        CASE WHEN C.VALUE_STR IS NULL THEN 'N' ELSE C.VALUE_STR END AS IS_REPEAT," +
                "        CASE WHEN D.VALUE_NUM IS NULL THEN 0 ELSE D.VALUE_NUM END AS INTERVAL_SECONDS," +
                "        CASE WHEN E.VALUE_NUM  IS NULL THEN 0 ELSE E.VALUE_NUM END AS INTERVAL_MINUTES," +
                "        CASE WHEN F.VALUE_NUM  IS NULL THEN 0 ELSE F.VALUE_NUM END AS HOUR," +
                "        CASE WHEN G.VALUE_NUM IS NULL THEN 0 ELSE G.VALUE_NUM END AS MINUTES," +
                "        CASE WHEN H.VALUE_NUM IS NULL THEN 0 ELSE H.VALUE_NUM END AS WEEK_DAY," +
                "        CASE WHEN I.VALUE_NUM IS NULL THEN 0 ELSE I.VALUE_NUM END AS DAY_OF_MONTH " +
                "FROM (" +
                "     SELECT ID_JOB, NAME, DESCRIPTION, CREATED_DATE, MODIFIED_DATE FROM R_JOB" +
                ") AS A " +
                "LEFT JOIN (" +
                "     SELECT ID_JOB, VALUE_NUM FROM R_JOBENTRY_ATTRIBUTE WHERE CODE = 'schedulerType'" +
                ") AS B ON  A.ID_JOB = B.ID_JOB " +
                "LEFT JOIN (" +
                "     SELECT ID_JOB, VALUE_STR FROM R_JOBENTRY_ATTRIBUTE WHERE CODE = 'repeat'" +
                ") AS C ON  A.ID_JOB = C.ID_JOB " +
                "LEFT JOIN (" +
                "     SELECT ID_JOB, VALUE_NUM FROM R_JOBENTRY_ATTRIBUTE WHERE CODE = 'intervalSeconds'" +
                ") AS D ON  A.ID_JOB = D.ID_JOB " +
                "LEFT JOIN (" +
                "     SELECT ID_JOB, VALUE_NUM FROM R_JOBENTRY_ATTRIBUTE WHERE CODE = 'intervalMinutes'" +
                ") AS E ON  A.ID_JOB = E.ID_JOB " +
                "LEFT JOIN (" +
                "    SELECT ID_JOB, VALUE_NUM FROM R_JOBENTRY_ATTRIBUTE WHERE CODE = 'hour'" +
                ") AS F ON  A.ID_JOB = F.ID_JOB " +
                "LEFT JOIN (" +
                "    SELECT ID_JOB, VALUE_NUM FROM R_JOBENTRY_ATTRIBUTE WHERE CODE = 'minutes'" +
                ") AS G ON  A.ID_JOB = G.ID_JOB " +
                "LEFT JOIN (" +
                "    SELECT ID_JOB, VALUE_NUM FROM R_JOBENTRY_ATTRIBUTE WHERE CODE = 'weekDay'" +
                ") AS H ON  A.ID_JOB = H.ID_JOB " +
                "LEFT JOIN (" +
                "    SELECT ID_JOB, VALUE_NUM FROM R_JOBENTRY_ATTRIBUTE WHERE CODE = 'dayOfMonth'" +
                ") AS I ON  A.ID_JOB = I.ID_JOB " +
                "WHERE 1=1 ");

        Connection conn = ConnectionProvider.getConnection();
        PreparedStatement pst = null;
        if (status.equals("")) {
            if (!term.equals("")) {
                query.append("AND (A.NAME LIKE ? OR A.DESCRIPTION LIKE ?) ");
            }
            if (createdDate != null) {
                query.append("AND A.CREATED_DATE =? ");
            }
            if (schedulerType != -1) {
                query.append("AND B.VALUE_NUM =? ");
            }
            query.append( "LIMIT ?;");

            pst = conn.prepareStatement(query.toString());

            int index = 1;
            if (!term.equals("")) {
                pst.setString(index, "%" + term + "%");
                index++;
                pst.setString(index, "%" + term + "%");
                index++;
            }
            if (createdDate != null) {
                pst.setDate(index, new java.sql.Date(createdDate.getTime()));
                index++;
            }
            if (schedulerType != -1) {
                pst.setInt(index, schedulerType);
                index++;
            }
            pst.setInt(index, limit);

        } else {
            List<String> jobNameList = new ArrayList<>();
            List<JobStatus> jobStatusList = CarteService.getJobStatusSet();
            for (JobStatus jobStatus : jobStatusList) {
                System.out.println(jobStatus.toString());
                if (jobStatus.getStatusDesc().toUpperCase().equals(status.toUpperCase())) {
                    jobNameList.add(jobStatus.getJobName());
                }
            }

            if (!term.equals("")) {
                query.append("AND (A.NAME LIKE ? OR A.DESCRIPTION LIKE ?) ");
            }

            if (jobNameList.size() > 0) {
                query.append("AND A.NAME IN ( ");
                for (int i =0; i < jobNameList.size(); i++) {
                    query.append("?, ");
                }
                query.append(" '')");

                System.out.println(query.toString());
                System.out.println(jobNameList);
                pst = conn.prepareStatement(query.toString());
                int index = 1;
                if(!term.equals("")) {
                    pst.setString(index, "%" + term + "%");
                    index ++;
                    pst.setString(index, "%" + term + "%");
                    index ++;
                }
                for (String name : jobNameList) {
                    pst.setString(index, name);
                    index++;
                }
            } else {
                conn.close();
                return jobList;
            }
        }

        ResultSet rs = pst.executeQuery();
        while (rs.next()) {
            Job job = new Job();
            job.setID(rs.getInt("ID_JOB"));
            job.setName(rs.getString("NAME"));
            job.setDescription(rs.getString("DESCRIPTION"));
            job.setCreatedDate(rs.getDate("CREATED_DATE"));
            job.setModifiedDate(rs.getDate("MODIFIED_DATE"));
            job.setIsRepeat(rs.getString("IS_REPEAT"));
            job.setSchedulerType(rs.getInt("SCHEDULER_TYPE"));
            job.setIntervalSeconds(rs.getInt("INTERVAL_SECONDS"));
            job.setIntervalMinutes(rs.getInt("INTERVAL_MINUTES"));
            job.setHours(rs.getInt("HOUR"));
            job.setMinutes(rs.getInt("MINUTES"));
            job.setWeekDay(rs.getInt("WEEK_DAY"));
            job.setDayOfMonth(rs.getInt("DAY_OF_MONTH"));
            jobList.add(job);
        }
        conn.close();
        return jobList;
    }


    public static List<String> findJobByName(String term, int limit) {
        List<String> keywords = new ArrayList<>();
        String sql = "SELECT NAME FROM R_JOB WHERE NAME LIKE ? LIMIT ?;";
        Connection conn = ConnectionProvider.getConnection();
        try {
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, "%" + term + "%");
            pst.setInt(2, limit);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                keywords.add(rs.getString("NAME"));
            }
            conn.close();
        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception ex) {

                }
            }
        }
        return keywords;
    }


    /**
     * update job scheduler
     *
     * @param job
     * @return
     */
    public static boolean updateJob(Job job) {
        try {
            Connection conn = ConnectionProvider.getConnection();
            conn.setAutoCommit(false);
            Statement stmt = conn.createStatement();

            String sql = "UPDATE R_JOBENTRY_ATTRIBUTE SET VALUE_STR ='" + job.getIsRepeat() +
                    "' WHERE ID_JOB= " + job.getID() + " AND CODE = 'repeat'";
            stmt.addBatch(sql);

            if (job.getSchedulerType() == 0) {  //no need to update other, this is no scheduler
                stmt.addBatch(getSQLQuery(0, job.getID(), "schedulerType"));

            } else if (job.getSchedulerType() == 1) {   //interval (update intervalSeconds and intervalMinutes)
                stmt.addBatch(getSQLQuery(1, job.getID(), "schedulerType"));
                stmt.addBatch(getSQLQuery(job.getIntervalMinutes(), job.getID(), "intervalMinutes"));
                stmt.addBatch(getSQLQuery(job.getIntervalSeconds(), job.getID(), "intervalSeconds"));

            } else if (job.getSchedulerType() == 2) {   //daily (update hour and minutes)
                stmt.addBatch(getSQLQuery(2, job.getID(), "schedulerType"));
                stmt.addBatch(getSQLQuery(job.getHours(), job.getID(), "hour"));
                stmt.addBatch(getSQLQuery(job.getMinutes(), job.getID(), "minutes"));

            } else if (job.getSchedulerType() == 3) {   //weekly (update hour and minutes, weekDay)
                stmt.addBatch(getSQLQuery(3, job.getID(), "schedulerType"));
                stmt.addBatch(getSQLQuery(job.getHours(), job.getID(), "hour"));
                stmt.addBatch(getSQLQuery(job.getMinutes(), job.getID(), "minutes"));
                stmt.addBatch(getSQLQuery(job.getWeekDay(), job.getID(), "weekDay"));

            } else if (job.getSchedulerType() == 4) {   //monthly (update hour and minutes, dayOfMonth)
                stmt.addBatch(getSQLQuery(4, job.getID(), "schedulerType"));
                stmt.addBatch(getSQLQuery(job.getHours(), job.getID(), "hour"));
                stmt.addBatch(getSQLQuery(job.getMinutes(), job.getID(), "minutes"));
                stmt.addBatch(getSQLQuery(job.getDayOfMonth(), job.getID(), "dayOfMonth"));
            }
            stmt.executeBatch();
            conn.commit();
            conn.close();
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return false;
    }

    /**
     * generate sql query based on code and value
     *
     * @param valueNum
     * @param jobId
     * @param code
     * @return
     */
    private static String getSQLQuery(int valueNum, int jobId, String code) {
        return "UPDATE R_JOBENTRY_ATTRIBUTE SET VALUE_NUM =" + valueNum + " WHERE ID_JOB= " + jobId + " AND CODE = '" + code + "'";
    }
}