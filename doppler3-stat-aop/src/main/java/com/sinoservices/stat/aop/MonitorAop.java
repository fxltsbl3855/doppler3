package com.sinoservices.stat.aop;



import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Charlie
 *         To change this template use File | Settings | File Templates.
 */
public class MonitorAop implements MethodInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(MonitorAop.class);

    private boolean statLogFlag = true;
    private String action;


    public MonitorAop(String actionValue) {
        action = (actionValue==null||"".equals(actionValue.trim()))?AOPUtil.ACTION_TYPE_REQ:actionValue.trim();
        logger.info("MonitorAop cons ok,action={},actionValue={}",action,actionValue);
        if(!AOPUtil.ACTION_TYPE_REQ.equals(action) && !AOPUtil.ACTION_TYPE_JOB.equals(action)){
            logger.error("param action is invalid.action="+actionValue+". Correct value is {} or {}",AOPUtil.ACTION_TYPE_REQ,AOPUtil.ACTION_TYPE_JOB);
        }
    }
    public Object invoke(MethodInvocation invocation) throws Throwable{
        if(!statLogFlag){
            return invocation.proceed();
        }
        String className = invocation.getThis().getClass().getName();
        String method = invocation.getMethod().getName();
        Object[] params = invocation.getArguments();
        String paramStr = toStr(params);
        long startTime = System.currentTimeMillis(),endTime;
        String jobId = AOPUtil.getRandom(startTime);
        try{
            if(logger.isInfoEnabled()) {
                logger.info("[stat] @action=ACTION_"+action+"_IN @className="+className+" @method="+method+" @result= @param="+paramStr+" @jobId="+jobId+" @time=0ms");
            }
            Object res =  invocation.proceed();
            endTime = System.currentTimeMillis();
            if(logger.isInfoEnabled()) {
                logger.info("[stat] @action=ACTION_"+action+"_OUT @className="+className+" @method="+method+" @result=SUCCESS @param="+paramStr+" @jobId="+jobId+" @time="+(endTime-startTime)+"ms");
            }
            return res;
        } catch (Exception e) {
            endTime = System.currentTimeMillis();
            logger.error("[stat] @action=ACTION_"+action+"_OUT @className="+className+" @method="+method+" @result=EXCEPTION @param="+paramStr+" @jobId="+jobId+" @time="+(endTime-startTime)+"ms",e);
            throw e;
        }
    }

    private String toStr(Object[] params) {
        if(params == null || params.length == 0){
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for(Object temp : params){
            if(temp == null) continue;
            sb.append(temp.toString());
            sb.append("#");
        }
        return sb.toString();
    }

    public boolean isStatLogFlag() {
        return statLogFlag;
    }

    public void setStatLogFlag(boolean statLogFlag) {
        this.statLogFlag = statLogFlag;
    }
}
