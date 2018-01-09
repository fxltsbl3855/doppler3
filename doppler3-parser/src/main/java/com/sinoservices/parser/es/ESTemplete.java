package com.sinoservices.parser.es;

import com.sinoservices.parser.config.UpMessageConfig;
import com.sinoservices.parser.es.entity.*;
import com.sinoservices.parser.es.mapping.Mapping;
import com.sinoservices.util.NumberUtil;
import org.elasticsearch.action.admin.cluster.stats.ClusterStatsNodes;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.Requests;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

import org.elasticsearch.indices.IndexAlreadyExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Charlie
 *         To change this template use File | Settings | File Templates.
 */
public class ESTemplete {
    private static final Logger LOG = LoggerFactory.getLogger(ESTemplete.class);
    private static List<String> indexCache = new ArrayList<String>(3);

    public static void createStatJobBatch(Client client,String indexName,String indexType,List<StatJobEntity> statJbEntityList ){
        long startTime = System.currentTimeMillis();
        if(!indexCache.contains(indexName) && !indexExist(client,indexName)){
            LOG.info("ES create_index 开始创建es索引，索引名称："+indexName+",type="+indexType);
            createIndex(client,indexName);
            try {
                Mapping.createStatJobMapping(client,indexName,indexType);
            } catch (IOException e) {
                LOG.error("ES create_mapping 创建es mapping失败，索引名称："+indexName+",type="+indexType);
                throw new RuntimeException("create index error,e="+e.getMessage());
            }
            LOG.info("ES create_index 创建esmapping ok，索引名称："+indexName+",type="+indexType);
        }

        if(statJbEntityList == null ||  statJbEntityList.size()==0){
            LOG.warn("Data is empty,just create index and mapping,index="+indexName+",type="+indexType);
            return;
        }

        BulkRequestBuilder brb = client.prepareBulk();
        for(StatJobEntity statJobEntity : statJbEntityList){
            brb.add(client.prepareIndex(indexName,indexType,statJobEntity.getId()).setSource(JsonUtil.obj2JsonData(statJobEntity)));
        }
        BulkResponse bulkResponse = brb.execute().actionGet();
        if(bulkResponse.hasFailures()){
            LOG.error("stat job batch insert fail,fail msg = "+bulkResponse.buildFailureMessage());
        }
        long endTime = System.currentTimeMillis();
        if(LOG.isDebugEnabled()){
            LOG.debug("stat job batch insert,logEntityList.size="+statJbEntityList.size()+",time="+(endTime-startTime)+",time2="+bulkResponse.getTookInMillis());
        }
    }

    public static void createStatBatch(Client client,String indexName,String indexType,List<StatEntity> statEntityList ) {
        long startTime = System.currentTimeMillis();
        if (!indexCache.contains(indexName) && !indexExist(client, indexName)) {
            LOG.info("ES create_index 开始创建es索引，索引名称：" + indexName + ",type=" + indexType);
            createIndex(client, indexName);
            try {
                Mapping.createStatMapping(client, indexName, indexType);
            } catch (IOException e) {
                LOG.error("ES create_mapping 创建es mapping失败，索引名称：" + indexName + ",type=" + indexType);
                throw new RuntimeException("create index error,e=" + e.getMessage());
            }
            LOG.info("ES create_index 创建esmapping ok，索引名称：" + indexName + ",type=" + indexType);
        }

        if (statEntityList == null || statEntityList.size() == 0) {
            LOG.warn("Data is empty,just create index and mapping,index=" + indexName + ",type=" + indexType);
            return;
        }
        BulkRequestBuilder brb = client.prepareBulk();
        for(StatEntity statEntity : statEntityList){
            brb.add(client.prepareIndex(indexName,indexType,statEntity.getId()).setSource(JsonUtil.obj2JsonData(statEntity)));
        }
        BulkResponse bulkResponse = brb.execute().actionGet();
        if(bulkResponse.hasFailures()){
            LOG.error("stat batch insert fail,fail msg = "+bulkResponse.buildFailureMessage());
        }
        long endTime = System.currentTimeMillis();
        if(LOG.isDebugEnabled()){
            LOG.debug("stat batch insert,logEntityList.size="+statEntityList.size()+",time="+(endTime-startTime)+",time2="+bulkResponse.getTookInMillis());
        }
    }

    public static boolean createDataBatch(Client client,String indexName,String indexType,List<LogEntity> logEntityList ){
        long startTime = System.currentTimeMillis();
        if(!indexCache.contains(indexName) && !indexExist(client,indexName)){
            LOG.info("ES create_index 开始创建es索引，索引名称："+indexName+",type="+indexType);
            createIndex(client,indexName);
            try {
                Mapping.createMapping(client,indexName,indexType);
            } catch (Exception e) {
                LOG.error("ES create_mapping 创建es mapping失败，索引名称："+indexName+",type="+indexType);
                throw new RuntimeException("create index error,e="+e.getMessage());
            }
            LOG.info("ES create_index 创建esmapping ok，索引名称："+indexName+",type="+indexType);
        }

        if(logEntityList == null ||  logEntityList.size()==0){
            LOG.warn("Data is empty,just create index and mapping,index="+indexName+",type="+indexType);
            return true;
        }
        BulkRequestBuilder brb = client.prepareBulk();
        for(LogEntity logEntity : logEntityList){
            brb.add(client.prepareIndex(indexName,indexType,logEntity.getId()).setSource(JsonUtil.obj2JsonData(logEntity)));
        }
        BulkResponse bulkResponse = brb.execute().actionGet();
        boolean res = true;
        if(bulkResponse.hasFailures()){
            LOG.error("log batch insert fail,fail size = "+logEntityList.size()+" ,msg = "+bulkResponse.buildFailureMessage());
            res = false;
        }
        long endTime = System.currentTimeMillis();
        if(LOG.isDebugEnabled()){
            LOG.debug("log batch insert,logEntityList.size="+logEntityList.size()+",time="+(endTime-startTime));
        }
        return res;
    }

    public static void createIndex(Client client,String indexName)  {
        try {
            client.admin().indices().prepareCreate(indexName).execute().actionGet();
            LOG.info("ES create_index 创建es索引成功，索引名称："+indexName);
        }catch (IndexAlreadyExistsException e){
            LOG.warn("index already created,indexName="+indexName);
        }catch (Exception ee){
            LOG.warn("createIndex error");
        }

    }

    /**
     * 是否存在index
     * @param indexName
     * @return
     */
    public static synchronized boolean indexExist(Client client,String indexName){
        IndicesExistsResponse response = client.admin().indices()
                .prepareExists(indexName)
                .execute().actionGet();
        boolean res = response.isExists();
        if(res) {
            indexCache.add(indexName);
        }
        return res;
    }

    /**
     * delete index
     * @param indexName
     */
    public static void deleteIndex(Client client,String indexName){
        if(!indexExist(client,indexName)){
            return ;
        }
        DeleteIndexResponse deleteIndexResponse =  client.admin().indices()
                    .prepareDelete(indexName)
                    .execute().actionGet();
        indexCache.remove(indexName);
        LOG.info("ES delete_mapping 删除es索引mapping成功，索引名称："+indexName+",res="+deleteIndexResponse.toString());
    }

    public static int getClusterNodeNum(Client client) {
        ClusterStatsNodes.Counts ss = client.admin().cluster().prepareClusterStats().get().getNodesStats().getCounts();
        return ss.getTotal();
    }

    public static void createExBatch(Client client, String indexExName, String indexType, List<ExEntity> exEntityList) {
        long startTime = System.currentTimeMillis();
        if (!indexCache.contains(indexExName) && !indexExist(client, indexExName)) {
            LOG.info("ES create_index 开始创建es索引，索引名称：" + indexExName + ",type=" + indexType);
            createIndex(client, indexExName);
            try {
                Mapping.createExStatMapping(client, indexExName, indexType);
            } catch (IOException e) {
                LOG.error("ES create_mapping 创建es mapping失败，索引名称：" + indexExName + ",type=" + indexType);
                throw new RuntimeException("create index error,e=" + e.getMessage());
            }
            LOG.info("ES create_index 创建esmapping ok，索引名称：" + indexExName + ",type=" + indexType);
        }

        if (exEntityList == null || exEntityList.size() == 0) {
            LOG.warn("Data is empty,just create index and mapping,index=" + indexExName + ",type=" + indexType);
            return;
        }
        BulkRequestBuilder brb = client.prepareBulk();
        for(ExEntity exEntity : exEntityList){
            brb.add(client.prepareIndex(indexExName,indexType,exEntity.getId()).setSource(JsonUtil.obj2JsonData(exEntity)));
        }
        BulkResponse bulkResponse = brb.execute().actionGet();
        if(bulkResponse.hasFailures()){
            LOG.error("exstat batch insert fail,fail msg = "+bulkResponse.buildFailureMessage());
        }
        long endTime = System.currentTimeMillis();
        if(LOG.isDebugEnabled()){
            LOG.debug("exstat batch insert,exEntityList.size="+exEntityList.size()+",time="+(endTime-startTime)+",time2="+bulkResponse.getTookInMillis());
        }
    }

    public static void createOpStatBatch(Client client, String indexExName, String indexType, List<OpEntity> opEntityList) {
        long startTime = System.currentTimeMillis();
        if (!indexCache.contains(indexExName) && !indexExist(client, indexExName)) {
            LOG.info("ES create_index 开始创建es索引，索引名称：" + indexExName + ",type=" + indexType);
            createIndex(client, indexExName);
            try {
                Mapping.createOpStatMapping(client, indexExName, indexType);
            } catch (IOException e) {
                LOG.error("ES create_mapping 创建es mapping失败，索引名称：" + indexExName + ",type=" + indexType);
                throw new RuntimeException("create index error,e=" + e.getMessage());
            }
            LOG.info("ES create_index 创建esmapping ok，索引名称：" + indexExName + ",type=" + indexType);
        }

        if (opEntityList == null || opEntityList.size() == 0) {
            LOG.warn("Data is empty,just create index and mapping,index=" + indexExName + ",type=" + indexType);
            return;
        }
        BulkRequestBuilder brb = client.prepareBulk();
        for(OpEntity opEntity : opEntityList){
            brb.add(client.prepareIndex(indexExName,indexType,opEntity.getId()).setSource(JsonUtil.obj2JsonData(opEntity)));
        }
        BulkResponse bulkResponse = brb.execute().actionGet();
        if(bulkResponse.hasFailures()){
            LOG.error("opstat batch insert fail,fail msg = "+bulkResponse.buildFailureMessage());
        }
        long endTime = System.currentTimeMillis();
        if(LOG.isDebugEnabled()){
            LOG.debug("opstat batch insert,opEntityList.size="+opEntityList.size()+",time="+(endTime-startTime)+",time2="+bulkResponse.getTookInMillis());
        }
    }
}
