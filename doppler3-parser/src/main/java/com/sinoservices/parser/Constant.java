package com.sinoservices.parser;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Charlie
 *         To change this template use File | Settings | File Templates.
 */
public class Constant {

    public static final String ES_CLISTER_NAME = "ruite-log-es";

    public static final String FILEBEAT_FILEDS_HOST = "ip";

    public static final String REDIS_QUEUE_KEY = "logQueue";

    public static final String APP_TYPE_WEB = "1";
    public static final String APP_TYPE_SERVER = "2";

    public static final String ACTION_JOB_OUT = "ACTION_JOB_OUT";
    public static final String ACTION_JOB_IN = "ACTION_JOB_IN";

    public static final String ACTION_JOB_IN_MATCH = "@action=ACTION_JOB_IN";
    public static final String ACTION_JOB_OUT_MATCH = "@action=ACTION_JOB_OUT";
    public static final String ACTION_REQ_OUT_MATCH = "@action=ACTION_REQ_OUT";
    public static final String ACTION_OPER_OUT_MATCH = "@action=ACTION_OPER_OUT";


    public static final String LOG_COLLECTOR_KAFKA = "kafka";
    public static final String LOG_COLLECTOR_REDIS = "redis";
    public static final String LOG_COLLECTOR_FILE = "file";


}
