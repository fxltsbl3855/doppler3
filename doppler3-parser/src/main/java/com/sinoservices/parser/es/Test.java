package com.sinoservices.parser.es;

import com.sinoservices.doppler2.DopplerConstants;
import com.sinoservices.parser.config.UpMessageConfig;
import com.sinoservices.parser.es.entity.*;
import com.sinoservices.parser.util.HttpRequest;
import com.sinoservices.util.NumberUtil;
import com.sinoservices.util.StringUtil;
import org.elasticsearch.action.admin.cluster.stats.ClusterStatsNodeResponse;
import org.elasticsearch.action.admin.cluster.stats.ClusterStatsNodes;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import java.io.IOException;
import java.net.InetAddress;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Charlie
 *         To change this template use File | Settings | File Templates.
 */
public class Test {

    public static void main(String[] a) throws IOException {
//        UpMessageConfig.es_addr="192.168.0.88:9300";
//        UpMessageConfig.es_addr_http="192.168.0.88:9200";
//        Client client = ClientHelper.getInstance().getClient();

        //cluster client
//        Client client =   TransportClient.builder().build()
//                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168.0.89"), 9300))
//                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168.0.90"), 9300));
        Settings settings = Settings.settingsBuilder()
                                .put("cluster.name", "ruite-log-es")
                                .put("tclient.transport.sniff", true).build();
        Client client = TransportClient.builder().settings(settings).build()
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168.0.89"), 9300));

//        String res = HttpRequest.restPostUpdateJob(JobUpdate.fillData("",0,"98765"));
//        System.out.println("===="+res);
//        ESTemplete.deleteIndex(client,"index20160801");
//        ESTemplete.deleteIndex(client,"appstat");
//        ESTemplete.deleteIndex(client,"appstatjob");
//        ESTemplete.deleteIndex(client,"exstat");
//        ESTemplete.deleteIndex(client,"opstat");

//        ESTemplete.createDataBatch(client,"index20160801","SYSLOG", Collections.EMPTY_LIST);
//        ESTemplete.createStatBatch(client,"appstat","SYSLOG", Collections.EMPTY_LIST);
//        ESTemplete.createStatJobBatch(client,"appstatjob","SYSLOG", Collections.EMPTY_LIST);
//        ESTemplete.createExBatch(client,"exstat_test","SYSLOG", Collections.EMPTY_LIST);
//        ESTemplete.createOpBatch(client,"opstat","SYSLOG", Collections.EMPTY_LIST);
//        System.out.println("delete index ok");

//        setJobStatData(client);
//        setData(client,Collections.EMPTY_LIST);
//        setStatData(client,Collections.EMPTY_LIST);

//        System.out.println("create index and mapping ok");

        setOpStatData(client);
        ClientHelper.getInstance().close(client);




    }

    public static void setOpStatData(Client client){
        List<OpEntity> opStatEntityList = new ArrayList<OpEntity>();
        OpEntity q1 = new OpEntity("ruite-web.2017-08-17.0.log#6714",new Date(),"ACTION_OPER_OUT","ruite-web2","192.168.0.88","com.sinoservices.Order","getOrderById",
                "get","Order","SUCCESS","param1#23#param2#esasd","sysid_OCR175,orderid_12381,hello_world","ori",20,5703,"ruite-web2.2017-08-17.0.log");
        OpEntity q2 = new OpEntity("ruite-web.2017-08-17.0.log#6705",new Date(),"ACTION_OPER_OUT","ruite-web2","192.168.0.88","com.sinoservices.Order","updateOrderById",
                "update","Order","EXCEPTION","param1#23#param2#esasd","sysid_OCR176,orderid_12392,test1_value1","ori",45,5714,"ruite-web2.2017-08-17.0.log");
        opStatEntityList.add(q1);
        opStatEntityList.add(q2);
        ESTemplete.createOpStatBatch(client,"opstat","SYSLOG", opStatEntityList);

    }
    public static void setJobStatData(Client client){
        List<StatJobEntity> statJobEntityList = new ArrayList<StatJobEntity>();
        StatJobEntity q1 = new StatJobEntity("ruite-web.2017-02-07.0.log#5902","ruite-web","192.168.0.88",new Date());
        q1.setAction("ACTION_JOB_IN");
        q1.setJobId("123456789");
        q1.setClassName("com.job");
        q1.setMethod("aa");
        q1.setResult("");
        q1.setParam("orderId=12");
        q1.setTime(0);
        statJobEntityList.add(q1);

        StatJobEntity q2 = new StatJobEntity("ruite-web.2017-02-07.0.log#6902","ruite-web","192.168.0.88",new Date());
        q2.setAction("ACTION_JOB_OUT");
        q2.setJobId("123456789");
        q2.setClassName("com.job");
        q2.setMethod("aa");
        q2.setResult("SUCCESS");
        q2.setParam("orderId=22");
        q2.setTime(300);
        statJobEntityList.add(q2);

        StatJobEntity q3 = new StatJobEntity("ruite-web.2017-02-07.0.log#7902","ruite-web","192.168.0.88",new Date());
        q3.setAction("ACTION_JOB_IN");
        q3.setJobId("98765");
        q3.setClassName("com.job");
        q3.setMethod("aa");
        q3.setResult("");
        q3.setParam("orderId=3");
        q3.setTime(0);
        statJobEntityList.add(q3);

        StatJobEntity q4 = new StatJobEntity("ruite-web.2017-02-07.0.log#8902","ruite-web","192.168.0.88",new Date());
        q4.setAction("ACTION_JOB_OUT");
        q4.setJobId("98765");
        q4.setClassName("com.job");
        q4.setMethod("aa");
        q4.setResult("SUCCESS");
        q4.setParam("orderId=3");
        q4.setTime(100);
        statJobEntityList.add(q4);

//        ExEntity ss = new ExEntity();
//        ss.setId("iiid4");
//        ss.setAppName("app1");
//        ss.setExName("NullPon");
//        ss.setHost("1.1.1.1");
//        ss.setClassName("com.aa.BB");
//        ss.setMethod("method");
//        ss.setTimestamp(new Date());
//        ss.setFile("das");
//        ss.setLineNum(2L);
//        statJobEntityList.add(ss);

        ESTemplete.createStatJobBatch(client,"appstatjob","SYSLOG", statJobEntityList);
    }

    public static void setData(Client client,List<LogEntity> logEntityList){
        ESTemplete.createDataBatch(client,"index20160801","SYSLOG", logEntityList);
    }

    public static void setStatData(Client client,List<StatEntity> statEntityList){
        statEntityList = new ArrayList<StatEntity>();
        statEntityList.add(getStatEntity("q1","ruite-web","192.168.0.1",new Date(),1,"SUCCESS","com.class1","method1","a=1","1","u1","192.168.0.1","chrom 1.1","header",200));

        ESTemplete.createStatBatch(client,"appstat","SYSLOG", statEntityList);
    }

    public static StatEntity getStatEntity(String id,String appName,String host,Date timestamp,int time,String result,String className,String method,String param,
                          String appType,String username,String clientIp,String brower,String header,int code){
        StatEntity statEntity1 = new StatEntity(id,appName,host,timestamp);
        statEntity1.setTime(time);statEntity1.setResult(result);statEntity1.setClassName(className);statEntity1.setMethod(method);statEntity1.setParam(param);
        statEntity1.setAppType(appType);
        statEntity1.setUsername(username);
        statEntity1.setClientIp(clientIp);
        statEntity1.setBrower(brower);
        statEntity1.setHeader(header);
        statEntity1.setCode(code);
        return statEntity1;
    }
}
