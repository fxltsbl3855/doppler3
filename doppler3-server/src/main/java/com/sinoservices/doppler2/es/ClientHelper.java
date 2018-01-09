package com.sinoservices.doppler2.es;

import com.sinoservices.doppler2.Constant;
import com.sinoservices.doppler2.ShutDownHook;
import com.sinoservices.doppler2.config.UpMessageConfig;
import com.sinoservices.doppler2.util.StringProcess;
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
    private static int CONN_SIZE = 3;
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
//        Client client =   TransportClient.builder().build()
//                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(StringUtil.replaceNullByDefault(UpMessageConfig.es_ip,"127.0.0.1")), NumberUtil.formatNumber(UpMessageConfig.es_port,9300)));
        Settings settings = Settings.settingsBuilder()
                .put("cluster.name", StringUtil.replaceNullByDefault(UpMessageConfig.es_cluster_name, Constant.ES_CLISTER_NAME))
                .put("tclient.transport.sniff", true).build();
        Map<String,Integer> map =  StringProcess.getClusterAddr(UpMessageConfig.es_addr,"127.0.0.1",9300);
        TransportClient tc = TransportClient.builder().settings(settings).build();
        for(Map.Entry<String,Integer> entry : map.entrySet()){
            tc.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(entry.getKey()), entry.getValue()));
        }
        return tc;
    }

    public void close(Client client){
        if(client != null){
            client.close();
            logger.debug("[conn] close conn over,"+client);
        }
    }

    public void closePool(){
        for(Client client : queue){
            close(client);
        }
    }

    public void returnPool(Client client){
        if(client != null) {
            queue.push(client);
            logger.debug("[conn] returnPool conn over,queue="+queue.size());
        }else{
            logger.error("[conn] returnPool conn error, client is null,queue="+queue.size() );
        }
    }

    public Client getClient(){
        Client client = null;
        try {
            client = queue.poll(2L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            logger.error("getClient exception,queue="+queue.size(),e);
        }
        if(client == null){
            logger.error("[conn] getClient conn error,client is null,queue="+queue.size());
            throw new RuntimeException("ES client is null,queue="+queue.size() );
        }
        logger.debug("[conn] getClient conn over,queue="+queue.size());
         return client;
    }

}