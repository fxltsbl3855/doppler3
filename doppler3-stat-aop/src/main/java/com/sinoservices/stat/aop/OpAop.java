package com.sinoservices.stat.aop;



import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Charlie
 *         To change this template use File | Settings | File Templates.
 */
public class OpAop implements MethodInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(OpAop.class);
    private String action = "OPER";
    private ThreadLocal<Map> threadLocal;

    public OpAop(ThreadLocal<Map> threadLocal) {
        this.threadLocal = threadLocal;
        logger.info("OpAop cons ok");
    }
    public Object invoke(MethodInvocation invocation) throws Throwable{
        String className = invocation.getThis().getClass().getName();
        String method = invocation.getMethod().getName();
        Object[] params = invocation.getArguments();
        String paramStr = toStr(params);
        long startTime = System.currentTimeMillis(),endTime;
        String[] operValue = AOPUtil.parserOper(method);
        String busParam = "";
        String result ="";
        try{
            if(logger.isInfoEnabled()) {
                logger.info("[stat] @action=ACTION_"+action+"_IN @className="+className+" @method="+method+" @operType="+operValue[0]+" @operObj="+operValue[1]+" @result= @param="+paramStr+" @busParam= @time=0ms");
            }
            Object res =  invocation.proceed();
            result = AOPUtil.getValueFromThreadlocal(threadLocal,AOPUtil.PROCESS_RES);
            busParam = AOPUtil.getValueFromThreadlocal(threadLocal,AOPUtil.BUS_PROPERTY);
            endTime = System.currentTimeMillis();
            if(logger.isInfoEnabled()) {
                logger.info("[stat] @action=ACTION_"+action+"_OUT @className="+className+" @method="+method+" @operType="+operValue[0]+" @operObj="+operValue[1]+" @result="+result+" @param="+paramStr+" @busParam="+busParam+" @time="+(endTime-startTime)+"ms");
            }
            return res;
        } catch (Exception e) {
            endTime = System.currentTimeMillis();
            logger.error("[stat] @action=ACTION_"+action+"_OUT @className="+className+" @method="+method+" @operType="+operValue[0]+" @operObj="+operValue[1]+" @result=EXCEPTION @param="+paramStr+" @busParam="+busParam+" @time="+(endTime-startTime)+"ms");
            logger.error("OpAOP target method throw exception",e);
            throw e;
        }
    }

    private String toStr(Object[] params) {
        if(params == null || params.length == 0){
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for(Object temp : params){
            sb.append(temp.toString());
            sb.append("#");
        }
        return sb.toString();
    }
}
