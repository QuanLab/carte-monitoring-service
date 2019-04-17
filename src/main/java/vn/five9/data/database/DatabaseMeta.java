package vn.five9.data.database;

/**
 * metadata about database connection and table
 * @author quanpv
 */
public abstract class DatabaseMeta {

    public static final String FIELD_JOB_ID = "ID_JOB";

    public static final String FIELD_JOB_NAME = "NAME";

    public static final String FIELD_JOB_DESCRIPTION = "DESCRIPTION";

    public static final String FIELD_JOB_CREATED_DATE = "CREATED_DATE";

    public static final String FIELD_JOB_MODIFIED_DATE = "MODIFIED_DATE";

    public static final String FIELD_JOB_IS_REPEAT = "IS_REPEAT";

    public static final String FIELD_JOB_CRON_ENABLE = "CRON_ENABLE";

    public static final String FIELD_JOB_CRON_EXPRESSION = "CRON_EXPRESSION";

    public static final String FIELD_JOB_CRON_START_DATE = "CRON_START_DATE";

    public static final String FIELD_JOB_CRON_END_DATE = "CRON_END_DATE";

    public static final String FIELD_JOB_SCHEDULER_TYPE = "SCHEDULER_TYPE";

    public static final String FIELD_JOB_INTERVAL_SECONDS = "INTERVAL_SECONDS";

    public static final String FIELD_JOB_INTERVAL_MINUTES = "INTERVAL_MINUTES";

    public static final String FIELD_JOB_HOUR = "HOUR";

    public static final String FIELD_JOB_MINUTES = "MINUTES";

    public static final String FIELD_JOB_WEEK_DAY = "WEEK_DAY";

    public static final String FIELD_JOB_DAY_OF_MONTH = "DAY_OF_MONTH";

}
