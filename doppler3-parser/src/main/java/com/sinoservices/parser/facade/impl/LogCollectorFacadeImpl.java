package com.sinoservices.parser.facade.impl;

import com.sinoservices.doppler2.DopplerConstants;
import com.sinoservices.doppler2.bo.LogHttpResBo;
import com.sinoservices.doppler2.facade.LogCollectorFacade;
import com.sinoservices.parser.config.UpMessageConfig;
import com.sinoservices.parser.es.ClientHelper;
import com.sinoservices.parser.es.ESTemplete;
import com.sinoservices.parser.service.LogCollectorService;
import com.sinoservices.parser.util.StringProcess;
import com.sinoservices.util.JsonUtils;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.util.Collections;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Charlie
 *         To change this template use File | Settings | File Templates.
 */
@Service("logCollectorFacade")
public class LogCollectorFacadeImpl implements LogCollectorFacade {
    private static final Logger logger = LoggerFactory.getLogger(LogCollectorFacadeImpl.class);

    @Autowired
    LogCollectorService logCollectorService;

    @Override
    public LogHttpResBo log(String host, String message, long offset, String fileName) {
        logger.info("log invoke host={},offset={},fileName="+fileName+",message="+message,host,offset);
        LogHttpResBo logHttpResBo = logCollectorService.log(host,message,offset,fileName);
        logger.info("log invoke end,logHttpResBo={}", JsonUtils.object2Json(logHttpResBo));
        return logHttpResBo;
    }

    @Override
    public LogHttpResBo log(String host, String[] messages, long offset, String fileName) {
        logger.info("log2 invoke host={},offset={},fileName="+fileName,host,offset);
        LogHttpResBo logHttpResBo = logCollectorService.log(host,messages,offset,fileName);
        logger.info("log2 invoke end,logHttpResBo={}", JsonUtils.object2Json(logHttpResBo));
        return logHttpResBo;
    }

    @Override
    public String deleteAllIndex() {
        try {

            Settings settings = Settings.settingsBuilder()
                    .put("cluster.name", "ruite-log-es")
                    .put("tclient.transport.sniff", true).build();
            Map<String,Integer> map =  StringProcess.getClusterAddr(UpMessageConfig.es_addr,"127.0.0.1",9300);
            TransportClient client = TransportClient.builder().settings(settings).build();
            for(Map.Entry<String,Integer> entry : map.entrySet()) {
                client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(entry.getKey()), entry.getValue()));
            }

            ESTemplete.deleteIndex(client, DopplerConstants.INDEX_NAME);
            ESTemplete.deleteIndex(client, DopplerConstants.INDEX_STAT_NAME);
            ESTemplete.deleteIndex(client, DopplerConstants.INDEX_STAT_JOB_NAME);

            ESTemplete.createDataBatch(client, DopplerConstants.INDEX_NAME, DopplerConstants.INDEX_TYPE, Collections.EMPTY_LIST);
            ESTemplete.createStatBatch(client,DopplerConstants.INDEX_STAT_NAME, DopplerConstants.INDEX_TYPE, Collections.EMPTY_LIST);
            ESTemplete.createStatJobBatch(client, DopplerConstants.INDEX_STAT_JOB_NAME, DopplerConstants.INDEX_TYPE, Collections.EMPTY_LIST);

            ClientHelper.getInstance().close(client);
            return "ok";
        }catch (Exception e){
            return e.getMessage();
        }
    }
}
