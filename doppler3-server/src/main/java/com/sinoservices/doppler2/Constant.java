package com.sinoservices.doppler2;

/**
 * Created by Administrator on 2016/1/22.
 */
public class Constant {

    public static final String ES_CLISTER_NAME = "ruite-log-es";

    public static final int LOG_DETAIL_PAGE_SIZE = 15;  //日志每次显示的条数（前多少条，后多少条）
    public static final int LOG_ESTIMATE_PAGE_SIZE = LOG_DETAIL_PAGE_SIZE + 10;   //程序预估的条数，为了能取够LOG_DETAIL_PAGE_SIZE的大小，这个值一定程度上大
    public static final int LINT_MSG_LENGTH = 200;  //预估的每条日志的字节数，此值约接近平均长度越好

    public static final String ES_FIELD_TIMESTAMP = "timestamp";

    public static final String FIELD_APP_NAME = "appName";
    public static final String FIELD_APP_TYPE = "appType";
    public static final String FIELD_CLASS_NAME = "className";
    public static final String FIELD_METHOD = "method";
    public static final String FIELD_ACTION = "action";
    public static final String FIELD_RESULT = "result";
    public static final String FIELD_TIMESTAMP = "timestamp";
    public static final String FIELD_LOGLEVEL = "logLevel";
    public static final String FIELD_ORIGINAL = "original";
    public static final String FIELD_HOST = "host";
    public static final String FIELD_EX = "ex";
    public static final String FIELD_POSITION = "position";
    public static final String FIELD_FILE = "file";
    public static final String FIELD_JOBID = "jobId";

    public static final String FIELD_PARAM = "param";
    public static final String FIELD_TIME = "time";

    public static final String FIELD_BROWER = "brower";
    public static final String FIELD_USERNAME = "username";
    public static final String FIELD_CLIENTIP = "clientIp";
    public static final String FIELD_HEADER = "header";
    public static final String FIELD_CODE = "code";

    public static final String FIELD_ACTION_JOBIN = "ACTION_JOB_IN";
    public static final String FIELD_ACTION_JOBOUT = "ACTION_JOB_OUT";

    public static class ExStat{
        public static final String FIELD_TIMESTAMP = "timestamp";
        public static final String FIELD_APP_NAME = "appName";
        public static final String FIELD_HOST = "host";
        public static final String FIELD_EX_NAME = "exName";
        public static final String FIELD_METHOD_NAME = "method";
        public static final String FIELD_CLASS_NAME = "className";
        public static final String FIELD_LINE_NUM = "lineNum";
        public static final String FIELD_FILE = "file";
    }

    public static class OpStat{
        public static final String FIELD_ACTION_OPIN = "ACTION_OPER_IN";
        public static final String FIELD_ACTION_OPOUT = "ACTION_OPER_OUT";

        public static final String FIELD_TIMESTAMP = "timestamp";
        public static final String FIELD_ACTION = "action";
        public static final String FIELD_APP_NAME = "appName";
        public static final String FIELD_HOST = "host";
        public static final String FIELD_PARAM = "param";
        public static final String FIELD_BUS_PARAM = "busParam";
        public static final String FIELD_RESULT = "result";
        public static final String FIELD_OP_TYPE = "opType";
        public static final String FIELD_OP_OBJ = "opObj";
        public static final String FIELD_TIME = "time";
    }

    public static final String STAT_RESULT_SUCCESS = "SUCCESS";
    public static final String STAT_RESULT_EXCEPTION = "EXCEPTION";


    public static final String ES_ID_DELIMETER = "#";

}
