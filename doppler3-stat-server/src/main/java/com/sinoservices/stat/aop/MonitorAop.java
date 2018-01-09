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

    private boolean statLogFlag;

    public MonitorAop() {
        logger.info("MonitorAop cons ok");
    }
    public Object invoke(MethodInvocation invocation) throws Throwable{
        if(!statLogFlag){
            return invocation.proceed();
        }
        long startTime = System.currentTimeMillis();
        long endTime;
        String className = invocation.getThis().getClass().getName();
        String method = invocation.getMethod().getName();
        try{
            Object res =  invocation.proceed();
            endTime = System.currentTimeMillis();
            if(logger.isInfoEnabled()) {
                logger.info("[stat] @action=ACTION_REQ_OUT @className="+className+" @method="+method+" @result=SUCCESS @time="+(endTime-startTime)+"ms");
            }
            return res;
        } catch (Exception e) {
            endTime = System.currentTimeMillis();
            logger.error("[stat] @action=ACTION_REQ_OUT @className="+className+" @method="+method+" @result=EXCEPTION @time="+(endTime-startTime)+"ms ",e);
            throw e;
        }
    }

    public boolean isStatLogFlag() {
        return statLogFlag;
    }

    public void setStatLogFlag(boolean statLogFlag) {
        this.statLogFlag = statLogFlag;
    }
}
