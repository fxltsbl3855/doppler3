package com.sinoservices.parser.es;

import com.sinoservices.parser.Constant;
import com.sinoservices.parser.ShutDownHook;
import com.sinoservices.parser.config.UpMessageConfig;
import com.sinoservices.parser.util.StringProcess;
import com.sinoservices.util.NumberUtil;
import com.sinoservices.util.StringUtil;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;


/**
 * Created by tgg on 16-3-17.
 */
public class ClientHelper {
    private static final Logger logger = LoggerFactory.getLogger(ClientHelper.class);
    private static int CONN_SIZE = 5;
    private static ClientHelper instance ;
    private LinkedBlockingDeque<Client> queue = new LinkedBlockingDeque<Client>(CONN_SIZE);

    private ClientHelper(){
        try {
            for(int i=0;i<CONN_SIZE;i++) {
                queue.add(createClient());
            }
        } catch (UnknownHostException e) {
            throw new RuntimeException("UnknownHostException occured when init es client, es ip adress may be wrong,please check it." );
        }
        logger.info("[conn] init conn over,size="+queue.size());

        Runtime rt = Runtime.getRuntime();
        rt.addShutdownHook(new Thread(new ShutDownHook()));
    }

    private static synchronized void  init(){
        if (instance == null) {
            instance =  new ClientHelper();
        }
    }

    public static ClientHelper getInstance() {
       if (instance == null) {
           init();
       }
        return instance;
    }

    private Client createClient() throws UnknownHostException {
        Settings settings = Settings.settingsBuilder()
                .put("cluster.name", StringUtil.replaceNullByDefault(UpMessageConfig.es_cluster_name, Constant.ES_CLISTER_NAME))
                .put("tclient.transport.sniff", true).build();
        Map<String,Integer> map =  StringProcess.getClusterAddr(UpMessageConfig.es_addr,"127.0.0.1",9300);
        TransportClient tc = TransportClient.builder().settings(settings).build();
        int ycl = 1;
        if(ycl < 0 && map.size() > 1){
            logger.warn("current es is cluster, this sysytem is not support. please upgrade to Enterprise Edition, otherwise system will be error!!!");
        }
        for(Map.Entry<String,Integer> entry : map.entrySet()) {
            tc.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(entry.getKey()), entry.getValue()));
        }
        logger.info("create es conn,conn hashcode = {}",tc.hashCode());
        return tc;
    }

    public void close(Client client){
        if(client != null){
            client.close();
            logger.info("[conn] close es conn over,"+client);
        }
    }

    public void closePool(){
        for(Client client : queue){
            close(client);
        }
    }

    public void returnPool(Client client){
        queue.push(client);
    }

    public Client getClient(){
        Client client = null;
        try {
            client = queue.poll(10L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            logger.error("getClient InterruptedException ",e);
        }
         return client;
    }


}