package vn.five9.data.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import vn.five9.data.database.DatabaseMeta;
import vn.five9.data.database.MySQLDatabase;
import vn.five9.data.exception.DatabaseException;
import vn.five9.data.model.Job;
import vn.five9.data.model.JobStatus;
import vn.five9.data.service.CarteService;

import java.sql.*;
import java.util.*;
import java.util.Date;


public class JobRepository {

    private static final Logger logger = LogManager.getLogger();

    /**
     * get list of job paging
     *
     * @param limit
     * @param offset
     * @return
     * @throws SQLException
     */
    public static List<Job> getJobs(Integer offset, Integer limit) throws SQLException {
        String query = "SELECT" +
                "        A.ID_JOB," +
                "        NAME," +
                "        CASE WHEN DESCRIPTION IS NULL THEN '' ELSE DESCRIPTION END AS DESCRIPTION," +
                "        CREATED_DATE," +
                "        MODIFIED_DATE," +
                "        CASE WHEN B.VALUE_NUM IS NULL THEN 0 ELSE B.VALUE_NUM END AS SCHEDULER_TYPE," +
                "        CASE WHEN C.VALUE_STR IS NULL THEN 'N' ELSE C.VALUE_STR END AS IS_REPEAT," +
                "        CASE WHEN J.CRON_ENABLE IS NULL THEN FALSE ELSE J.CRON_ENABLE END AS CRON_ENABLE," +
                "        J.CRON_EXPRESSION, " +
                "        J.CRON_START_DATE, " +
                "        J.CRON_END_DATE," +
                "        CASE WHEN D.VALUE_NUM IS NULL THEN 0 ELSE D.VALUE_NUM END AS INTERVAL_SECONDS," +
                "        CASE WHEN E.VALUE_NUM IS NULL THEN 0 ELSE E.VALUE_NUM END AS INTERVAL_MINUTES," +
                "        CASE WHEN F.VALUE_NUM IS NULL THEN 0 ELSE F.VALUE_NUM END AS HOUR," +
                "        CASE WHEN G.VALUE_NUM IS NULL THEN 0 ELSE G.VALUE_NUM END AS MINUTES," +
                "        CASE WHEN H.VALUE_NUM IS NULL THEN 0 ELSE H.VALUE_NUM END AS WEEK_DAY," +
                "        CASE WHEN I.VALUE_NUM IS NULL THEN 0 ELSE I.VALUE_NUM END AS DAY_OF_MONTH " +
                "FROM R_JOB AS A " +
                "LEFT JOIN (" +
                "        SELECT DISTINCT ID_JOB, VALUE_NUM FROM R_JOBENTRY_ATTRIBUTE WHERE CODE = 'schedulerType'" +
                ") AS B ON  A.ID_JOB = B.ID_JOB " +
                "LEFT JOIN (" +
                "        SELECT DISTINCT ID_JOB, VALUE_STR FROM R_JOBENTRY_ATTRIBUTE WHERE CODE = 'repeat'" +
                ") AS C ON  A.ID_JOB = C.ID_JOB " +
                "LEFT JOIN (" +
                "        SELECT DISTINCT ID_JOB, VALUE_NUM FROM R_JOBENTRY_ATTRIBUTE WHERE CODE = 'intervalSeconds'" +
                ") AS D ON  A.ID_JOB = D.ID_JOB " +
                "LEFT JOIN (" +
                "        SELECT DISTINCT ID_JOB, VALUE_NUM FROM R_JOBENTRY_ATTRIBUTE WHERE CODE = 'intervalMinutes'" +
                ") AS E ON  A.ID_JOB = E.ID_JOB " +
                "LEFT JOIN (" +
                "        SELECT DISTINCT ID_JOB, VALUE_NUM FROM R_JOBENTRY_ATTRIBUTE WHERE CODE = 'hour'" +
                ") AS F ON  A.ID_JOB = F.ID_JOB " +
                "LEFT JOIN (" +
                "        SELECT DISTINCT ID_JOB, VALUE_NUM FROM R_JOBENTRY_ATTRIBUTE WHERE CODE = 'minutes'" +
                ") AS G ON  A.ID_JOB = G.ID_JOB " +
                "LEFT JOIN (" +
                "         SELECT DISTINCT ID_JOB, VALUE_NUM FROM R_JOBENTRY_ATTRIBUTE WHERE CODE = 'weekDay'" +
                ") AS H ON  A.ID_JOB = H.ID_JOB " +
                "LEFT JOIN (" +
                "          SELECT DISTINCT ID_JOB, VALUE_NUM FROM R_JOBENTRY_ATTRIBUTE WHERE CODE = 'dayOfMonth' " +
                ") AS I ON  A.ID_JOB = I.ID_JOB " +
                "LEFT JOIN R_JOBENTRY_ATTRIBUTE_EXT AS J ON J.ID_JOB = A.ID_JOB " +
                "LIMIT ? OFFSET ? ;";

        List<Job> jobList = new ArrayList<>();
        Connection conn = null;
        try {
            conn = MySQLDatabase.getInstance().getConnection();
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setInt(1, limit + 1);
            pst.setInt(2, offset);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Job job = new Job();
                job.setID(rs.getInt(DatabaseMeta.FIELD_JOB_ID));
                job.setName(rs.getString(DatabaseMeta.FIELD_JOB_NAME));
                job.setDescription(rs.getString(DatabaseMeta.FIELD_JOB_DESCRIPTION));
                job.setCreatedDate(rs.getDate(DatabaseMeta.FIELD_JOB_CREATED_DATE));
                job.setModifiedDate(rs.getDate(DatabaseMeta.FIELD_JOB_MODIFIED_DATE));
                job.setIsRepeat(rs.getString(DatabaseMeta.FIELD_JOB_IS_REPEAT));
                job.setCronEnable(rs.getBoolean(DatabaseMeta.FIELD_JOB_CRON_ENABLE));
                job.setCron(rs.getString(DatabaseMeta.FIELD_JOB_CRON_EXPRESSION));
                job.setCronStartDate(rs.getDate(DatabaseMeta.FIELD_JOB_CRON_START_DATE));
                job.setCronEndDate(rs.getDate(DatabaseMeta.FIELD_JOB_CRON_END_DATE));
                job.setSchedulerType(rs.getInt(DatabaseMeta.FIELD_JOB_SCHEDULER_TYPE));
                job.setIntervalSeconds(rs.getInt(DatabaseMeta.FIELD_JOB_INTERVAL_SECONDS));
                job.setIntervalMinutes(rs.getInt(DatabaseMeta.FIELD_JOB_INTERVAL_MINUTES));
                job.setHours(rs.getInt(DatabaseMeta.FIELD_JOB_HOUR));
                job.setMinutes(rs.getInt(DatabaseMeta.FIELD_JOB_MINUTES));
                job.setWeekDay(rs.getInt(DatabaseMeta.FIELD_JOB_WEEK_DAY));
                job.setDayOfMonth(rs.getInt(DatabaseMeta.FIELD_JOB_DAY_OF_MONTH));
                jobList.add(job);
            }
            conn.close();
        } catch (SQLException e) {
            throw new SQLException(e.getMessage(), e);
        }
        return jobList;
    }

    /**
     * get list of job
     */
    public static List<Job> findJobs(String term, String status, Date fromDate, Date toDate, int schedulerType, int limit)
            throws SQLException, NullPointerException {
        List<Job> jobList = new ArrayList<>();
        StringBuilder query = new StringBuilder();
        query.append("SELECT" +
                "        A.ID_JOB," +
                "        NAME," +
                "        CASE WHEN DESCRIPTION IS NULL THEN '' ELSE DESCRIPTION END AS DESCRIPTION," +
                "        CREATED_DATE," +
                "        MODIFIED_DATE," +
                "        CASE WHEN B.VALUE_NUM IS NULL THEN 0 ELSE B.VALUE_NUM END AS SCHEDULER_TYPE," +
                "        CASE WHEN C.VALUE_STR IS NULL THEN 'N' ELSE C.VALUE_STR END AS IS_REPEAT," +
                "        CASE WHEN J.CRON_ENABLE IS NULL THEN FALSE ELSE J.CRON_ENABLE END AS CRON_ENABLE," +
                "        J.CRON_EXPRESSION, " +
                "        J.CRON_START_DATE, " +
                "        J.CRON_END_DATE," +
                "        CASE WHEN D.VALUE_NUM IS NULL THEN 0 ELSE D.VALUE_NUM END AS INTERVAL_SECONDS," +
                "        CASE WHEN E.VALUE_NUM  IS NULL THEN 0 ELSE E.VALUE_NUM END AS INTERVAL_MINUTES," +
                "        CASE WHEN F.VALUE_NUM  IS NULL THEN 0 ELSE F.VALUE_NUM END AS HOUR," +
                "        CASE WHEN G.VALUE_NUM IS NULL THEN 0 ELSE G.VALUE_NUM END AS MINUTES," +
                "        CASE WHEN H.VALUE_NUM IS NULL THEN 0 ELSE H.VALUE_NUM END AS WEEK_DAY," +
                "        CASE WHEN I.VALUE_NUM IS NULL THEN 0 ELSE I.VALUE_NUM END AS DAY_OF_MONTH " +
                "FROM R_JOB A " +
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
                "LEFT JOIN R_JOBENTRY_ATTRIBUTE_EXT AS J ON J.ID_JOB = A.ID_JOB " +
                "WHERE 1=1 ");

        Connection conn = MySQLDatabase.getInstance().getConnection();
        PreparedStatement pst = null;
        if (status.equals("")) {
            if (!term.equals("")) {
                query.append("AND (A.NAME LIKE ? OR A.DESCRIPTION LIKE ?) ");
            }
            if (fromDate != null) {
                if (toDate != null) {
                    query.append("AND A.CREATED_DATE BETWEEN ? AND ?");
                } else {
                    query.append("AND A.CREATED_DATE >= ? ");
                }
            } else {
                if (toDate != null) {
                    query.append("AND A.CREATED_DATE <= ? ");
                }
            }
            if (schedulerType != -1) {
                query.append("AND B.VALUE_NUM =? ");
            }
            query.append("LIMIT ?;");

            logger.info(query.toString());
            pst = conn.prepareStatement(query.toString());

            int index = 1;
            if (!term.equals("")) {
                pst.setString(index, "%" + term + "%");
                index++;
                pst.setString(index, "%" + term + "%");
                index++;
            }

            if (fromDate != null) {
                if (toDate != null) {
                    pst.setDate(index, new java.sql.Date(fromDate.getTime()));
                    index++;
                    pst.setDate(index, new java.sql.Date(toDate.getTime()));
                    index++;
                } else {
                    pst.setDate(index, new java.sql.Date(fromDate.getTime()));
                    index++;
                }
            } else {
                if (toDate != null) {
                    pst.setDate(index, new java.sql.Date(toDate.getTime()));
                    index++;
                }
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
                if (jobStatus.getStatusDesc().toUpperCase().equals(status.toUpperCase())) {
                    jobNameList.add(jobStatus.getJobName());
                }
            }

            if (!term.equals("")) {
                query.append("AND (A.NAME LIKE ? OR A.DESCRIPTION LIKE ?) ");
            }

            if (jobNameList.size() > 0) {
                query.append("AND A.NAME IN ( ");
                for (int i = 0; i < jobNameList.size(); i++) {
                    query.append("?, ");
                }
                query.append(" '')");

                if (schedulerType != -1) {
                    query.append("AND B.VALUE_NUM =? ");
                }

                System.out.println(query.toString());
                System.out.println(jobNameList);
                pst = conn.prepareStatement(query.toString());
                int index = 1;
                if (!term.equals("")) {
                    pst.setString(index, "%" + term + "%");
                    index++;
                    pst.setString(index, "%" + term + "%");
                    index++;
                }
                for (String name : jobNameList) {
                    pst.setString(index, name);
                    index++;
                }
                if (schedulerType != -1) {
                    pst.setInt(index, schedulerType);
                }

            } else {
                conn.close();
                return jobList;
            }
        }

        ResultSet rs = pst.executeQuery();
        while (rs.next()) {
            Job job = new Job();
            job.setID(rs.getInt(DatabaseMeta.FIELD_JOB_ID));
            job.setName(rs.getString(DatabaseMeta.FIELD_JOB_NAME));
            job.setDescription(rs.getString(DatabaseMeta.FIELD_JOB_DESCRIPTION));
            job.setCreatedDate(rs.getDate(DatabaseMeta.FIELD_JOB_CREATED_DATE));
            job.setModifiedDate(rs.getDate(DatabaseMeta.FIELD_JOB_MODIFIED_DATE));
            job.setIsRepeat(rs.getString(DatabaseMeta.FIELD_JOB_IS_REPEAT));
            job.setCronEnable(rs.getBoolean(DatabaseMeta.FIELD_JOB_CRON_ENABLE));
            job.setCron(rs.getString(DatabaseMeta.FIELD_JOB_CRON_EXPRESSION));
            job.setCronStartDate(rs.getDate(DatabaseMeta.FIELD_JOB_CRON_START_DATE));
            job.setCronEndDate(rs.getDate(DatabaseMeta.FIELD_JOB_CRON_END_DATE));
            job.setSchedulerType(rs.getInt(DatabaseMeta.FIELD_JOB_SCHEDULER_TYPE));
            job.setIntervalSeconds(rs.getInt(DatabaseMeta.FIELD_JOB_INTERVAL_SECONDS));
            job.setIntervalMinutes(rs.getInt(DatabaseMeta.FIELD_JOB_INTERVAL_MINUTES));
            job.setHours(rs.getInt(DatabaseMeta.FIELD_JOB_HOUR));
            job.setMinutes(rs.getInt(DatabaseMeta.FIELD_JOB_MINUTES));
            job.setWeekDay(rs.getInt(DatabaseMeta.FIELD_JOB_WEEK_DAY));
            job.setDayOfMonth(rs.getInt(DatabaseMeta.FIELD_JOB_DAY_OF_MONTH));
            if (!status.equals("")) {
                job.setStatus(status);
            }
            jobList.add(job);
        }
        conn.close();
        return jobList;
    }


    /**
     * update job scheduler
     *
     * @param job
     * @return
     */
    public static void updateJobScheduler(Job job) throws DatabaseException {
        Connection conn = null;
        try {
            conn = MySQLDatabase.getInstance().getConnection();
            conn.setAutoCommit(false);
            Statement stmt = conn.createStatement();

            String sql = "UPDATE R_JOBENTRY_ATTRIBUTE SET VALUE_STR ='" + job.getIsRepeat() +
                    "' WHERE ID_JOB= " + job.getID() + " AND CODE = 'repeat'";
            stmt.addBatch(sql);

            if (job.getSchedulerType() == 0) {  //no need to update other, this is no scheduler
                stmt.addBatch(getSQLQuery(0, job.getID(), "schedulerType"));

                StringBuilder sb = new StringBuilder();
                sb.append("UPDATE R_JOBENTRY_ATTRIBUTE_EXT SET CRON_EXPRESSION =?, CRON_ENABLE = ? ");
                if (job.getCronStartDate() != null) {
                    sb.append(", CRON_START_DATE=? ");
                }
                if (job.getCronEndDate() != null) {
                    sb.append(", CRON_END_DATE=? ");
                }
                sb.append("WHERE ID_JOB=?");

                PreparedStatement pst = conn.prepareStatement(sb.toString());
                int i = 1;
                pst.setString(i, job.getCron());
                i++;
                pst.setBoolean(i, job.isCronEnable());
                i++;
                if (job.getCronStartDate() != null) {
                    pst.setDate(i, new java.sql.Date(job.getCronStartDate().getTime()));
                    i++;
                }
                if (job.getCronEndDate() != null) {
                    pst.setDate(i, new java.sql.Date(job.getCronEndDate().getTime()));
                    i++;
                }
                pst.setInt(i, job.getID());
                pst.executeUpdate();

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
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new DatabaseException(e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception e) {
                    //pass
                }
            }
        }
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

    /**
     * @return list job enable cron scheduler
     */
    public static List<Job> getListJobCronEnable() {
        String sql = "SELECT " +
                "   A.ID_JOB," +
                " NAME," +
                " CASE WHEN J.CRON_ENABLE IS NULL THEN FALSE ELSE J.CRON_ENABLE END AS CRON_ENABLE, " +
                " J.CRON_EXPRESSION, " +
                " J.CRON_START_DATE, " +
                " J.CRON_END_DATE " +
                "FROM R_JOB AS A " +
                "LEFT JOIN R_JOBENTRY_ATTRIBUTE_EXT AS J ON J.ID_JOB = A.ID_JOB " +
                "WHERE CRON_ENABLE = true";
        List<Job> jobList = new ArrayList<>();
        Connection conn = MySQLDatabase.getInstance().getConnection();
        try {
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Job job = new Job();
                job.setID(rs.getInt(DatabaseMeta.FIELD_JOB_ID));
                job.setName(rs.getString(DatabaseMeta.FIELD_JOB_NAME));
                job.setCronEnable(rs.getBoolean(DatabaseMeta.FIELD_JOB_CRON_ENABLE));
                job.setCron(rs.getString(DatabaseMeta.FIELD_JOB_CRON_EXPRESSION));
                job.setCronStartDate(rs.getDate(DatabaseMeta.FIELD_JOB_CRON_START_DATE));
                job.setCronEndDate(rs.getDate(DatabaseMeta.FIELD_JOB_CRON_END_DATE));
                jobList.add(job);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception e) {
                    // pass
                }
            }
        }
        return jobList;
    }


    /**
     * @return list job enable cron scheduler
     */
    public static Map<String, Job> getMapJobCronEnable() {
        String sql = "SELECT " +
                "   A.ID_JOB," +
                " NAME," +
                " CASE WHEN J.CRON_ENABLE IS NULL THEN FALSE ELSE J.CRON_ENABLE END AS CRON_ENABLE, " +
                " J.CRON_EXPRESSION, " +
                " J.CRON_START_DATE, " +
                " J.CRON_END_DATE " +
                "FROM R_JOB AS A " +
                "LEFT JOIN R_JOBENTRY_ATTRIBUTE_EXT AS J ON J.ID_JOB = A.ID_JOB " +
                "WHERE CRON_ENABLE = true";
        Map<String, Job> jobMap = new HashMap<>();
        Connection conn = MySQLDatabase.getInstance().getConnection();
        try {
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Job job = new Job();
                job.setID(rs.getInt(DatabaseMeta.FIELD_JOB_ID));
                job.setName(rs.getString(DatabaseMeta.FIELD_JOB_NAME));
                job.setCronEnable(rs.getBoolean(DatabaseMeta.FIELD_JOB_CRON_ENABLE));
                job.setCron(rs.getString(DatabaseMeta.FIELD_JOB_CRON_EXPRESSION));
                job.setCronStartDate(rs.getTimestamp(DatabaseMeta.FIELD_JOB_CRON_START_DATE));
                job.setCronEndDate(rs.getTimestamp(DatabaseMeta.FIELD_JOB_CRON_END_DATE));
                jobMap.put(job.getName(), job);
            }
            pst.close();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception e) {
                    // pass
                }
            }
        }
        return jobMap;
    }
}