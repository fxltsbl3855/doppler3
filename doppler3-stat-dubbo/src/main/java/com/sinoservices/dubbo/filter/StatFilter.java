
package com.sinoservices.dubbo.filter;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@Activate(group = {Constants.PROVIDER})
public class StatFilter implements Filter {

    private static Logger logger = LoggerFactory.getLogger(StatFilter.class);
    private static List<String> list = new ArrayList<String>();

    // 调用过程拦截
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        if(logger.isDebugEnabled()) {
            logger.debug("StatFilter invoke...");
        }
        String serviceName = RpcContext.getContext().getUrl().getServiceInterface();
        String method = invocation.getMethodName();
        String param = StatUtil.toStr(invocation.getArguments());
        String result = "SUCCESS";
        long start = System.currentTimeMillis();

        Result invokeResult;
        try {
            invokeResult = invoker.invoke(invocation);
            if(invokeResult.hasException()){
                result = "EXCEPTION";
            }
            return invokeResult;
        }catch(RpcException e){
            result = "EXCEPTION";
            throw e;
        }finally {
            long time = System.currentTimeMillis() - start;
            if(logger.isInfoEnabled()) {
                logger.info("[stat] @action=ACTION_REQ_OUT @className="+serviceName+" @method="+method+" @result="+result+" @param="+param+" @time="+time+"ms");
            }
        }

    }
}