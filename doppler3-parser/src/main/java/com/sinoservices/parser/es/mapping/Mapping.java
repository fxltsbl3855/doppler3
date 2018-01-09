package com.sinoservices.parser.es.mapping;

import com.sinoservices.parser.config.UpMessageConfig;
import com.sinoservices.util.NumberUtil;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.Requests;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Charlie
 *         To change this template use File | Settings | File Templates.
 */
public class Mapping {

    private static final Logger logger = LoggerFactory.getLogger(Mapping.class);
    private static String SOURCE_VALUE = "true";

    public static void createStatJobMapping(Client client, String indexName, String indexType) throws IOException {
        XContentBuilder builder= XContentFactory
                .jsonBuilder()
                .startObject().startObject(indexType)
                .startObject("_source").field("enabled", SOURCE_VALUE).endObject()
                .startObject("_all").field("enabled", "false").endObject()
                .startObject("_ttl").field("enabled", "true").field("default", NumberUtil.formatNumber(UpMessageConfig.es_ttl_job,15)+"d").endObject()
                .startObject("properties")
                .startObject("action").field("type", "string").field("store", "false").field("index","not_analyzed").endObject()
                .startObject("appName").field("type", "string").field("store", "false").field("index","not_analyzed").endObject()
                .startObject("host").field("type", "ip").field("store", "false").field("index","not_analyzed").endObject()
                .startObject("timestamp").field("type", "date").field("store", "true").field("index","not_analyzed").endObject()
                .startObject("className") .field("type", "string").field("store", "false").field("index","not_analyzed").endObject()
                .startObject("method").field("type", "string").field("store", "false") .field("index","not_analyzed").endObject()
                .startObject("result").field("type", "string").field("store", "false") .field("index","not_analyzed").endObject()
                .startObject("param").field("type", "string").field("store", "false").field("index","not_analyzed") .endObject()
                .startObject("jobId") .field("type", "string").field("store", "false").field("index","not_analyzed").endObject()
                .startObject("time") .field("type", "long").field("store", "false") .field("index","not_analyzed").endObject()
                .endObject().endObject().endObject();
        PutMappingRequest mapping = Requests.putMappingRequest(indexName).type(indexType).source(builder);
        PutMappingResponse res = client.admin().indices().putMapping(mapping).actionGet();
        System.out.println(res.toString());
        logger.info("ES create_mapping 创建es索引mapping成功，索引名称："+indexName+",type="+indexType);
    }

    public static void createMapping(Client client,String indexName,String indexType) throws IOException {
        XContentBuilder builder= XContentFactory
                .jsonBuilder()
                .startObject().startObject(indexType)
                .startObject("_source").field("enabled", SOURCE_VALUE).endObject()
                .startObject("_all") .field("enabled", "false").endObject()
                .startObject("_ttl").field("enabled", "true") .field("default", NumberUtil.formatNumber(UpMessageConfig.es_ttl,7)+"d").endObject()
                .startObject("properties")
                .startObject("appName").field("type", "string").field("store", "false") .field("index","not_analyzed").endObject()
                .startObject("host").field("type", "ip").field("store", "false").field("index","not_analyzed").endObject()
                .startObject("timestamp").field("type", "date").field("store", "true").field("index","not_analyzed").endObject()
                .startObject("logLevel").field("type", "string").field("store", "false").field("index","not_analyzed").endObject()
                .startObject("ex") .field("type", "string").field("store", "false").field("index","not_analyzed") .endObject()
                .startObject("original").field("type", "string").field("store", "true").field("index","analyzed").field("analyzer","ik").endObject()
                .startObject("position").field("type", "long").field("store", "false").field("index","not_analyzed").endObject()
                .startObject("file").field("type", "string").field("store", "false") .field("index","not_analyzed").endObject()
                .endObject().endObject().endObject();
        PutMappingRequest mapping = Requests.putMappingRequest(indexName).type(indexType).source(builder);
        client.admin().indices().putMapping(mapping).actionGet();
        logger.info("ES create_mapping 创建es索引mapping成功，索引名称："+indexName+",type="+indexType);
    }

    public static void createStatMapping(Client client, String indexName, String indexType) throws IOException {
        XContentBuilder builder= XContentFactory
                .jsonBuilder()
                .startObject().startObject(indexType)
                .startObject("_source").field("enabled",SOURCE_VALUE).endObject()
                .startObject("_all").field("enabled", "false").endObject()
                .startObject("properties")
                .startObject("appName").field("type", "string").field("store", "false").field("index","not_analyzed").endObject()
                .startObject("host").field("type", "ip").field("store", "false").field("index","not_analyzed").endObject()
                .startObject("timestamp").field("type", "date").field("store", "true").field("index","not_analyzed").endObject()
                .startObject("className").field("type", "string").field("store", "false").field("index","not_analyzed").endObject()
                .startObject("method").field("type", "string").field("store", "false").field("index","not_analyzed").endObject()
                .startObject("result").field("type", "string").field("store", "false").field("index","not_analyzed").endObject()
                .startObject("param").field("type", "string").field("store", "false").field("index","not_analyzed").field("ignore_above",256).endObject()
                .startObject("time").field("type", "long").field("store", "false").field("index","not_analyzed").endObject()
                .startObject("appType").field("type", "string").field("store", "false").field("index","not_analyzed").endObject()
                .startObject("username").field("type", "string").field("store", "false").field("index","not_analyzed").endObject()
                .startObject("clientIp").field("type", "string").field("store", "false").field("index","not_analyzed").endObject()
                .startObject("brower").field("type", "string").field("store", "false").field("index","not_analyzed").endObject()
                .startObject("header").field("type", "string").field("store", "false").field("index","not_analyzed").endObject()
                .startObject("code").field("type", "integer").field("store", "false").field("index","not_analyzed").endObject()
                .endObject().endObject().endObject();
        PutMappingRequest mapping = Requests.putMappingRequest(indexName).type(indexType).source(builder);
        client.admin().indices().putMapping(mapping).actionGet();
        logger.info("ES create_mapping 创建es索引mapping成功，索引名称："+indexName+",type="+indexType);
    }

    public static void createExStatMapping(Client client, String indexExName, String indexType) throws IOException {
        XContentBuilder builder= XContentFactory
                .jsonBuilder()
                .startObject().startObject(indexType)
                .startObject("_source").field("enabled",SOURCE_VALUE).endObject()
                .startObject("_all").field("enabled", "false").endObject()
                .startObject("properties")
                .startObject("timestamp").field("type", "date").field("store", "true").field("index","not_analyzed").endObject()
                .startObject("appName").field("type", "string").field("store", "false").field("index","not_analyzed").endObject()
                .startObject("host").field("type", "ip").field("store", "false").field("index","not_analyzed").endObject()
                .startObject("exName").field("type", "string").field("store", "false").field("index","not_analyzed").endObject()
                .startObject("className").field("type", "string").field("store", "false").field("index","not_analyzed").endObject()
                .startObject("method").field("type", "string").field("store", "false").field("index","not_analyzed").endObject()
                .startObject("lineNum").field("type", "long").field("store", "false").field("index","not_analyzed").endObject()
                .startObject("original").field("type", "string").field("store", "false").field("index","analyzed").field("analyzer","ik").endObject()
                .startObject("position").field("type", "long").field("store", "false").field("index","not_analyzed").endObject()
                .startObject("file").field("type", "string").field("store", "false").field("index","not_analyzed").endObject()
                .endObject().endObject().endObject();
        PutMappingRequest mapping = Requests.putMappingRequest(indexExName).type(indexType).source(builder);
        client.admin().indices().putMapping(mapping).actionGet();
        logger.info("ES create_mapping 创建es索引mapping成功，索引名称："+indexExName+",type="+indexType);
    }

    public static void createOpStatMapping(Client client, String indexExName, String indexType) throws IOException {
        XContentBuilder builder= XContentFactory
                .jsonBuilder()
                .startObject().startObject(indexType)
                .startObject("_source").field("enabled",SOURCE_VALUE).endObject()
                .startObject("_all").field("enabled", "false").endObject()
                .startObject("properties")
                .startObject("timestamp").field("type", "date").field("store", "true").field("index","not_analyzed").endObject()
                .startObject("action").field("type", "string").field("store", "false").field("index","not_analyzed").endObject()
                .startObject("appName").field("type", "string").field("store", "false").field("index","not_analyzed").endObject()
                .startObject("host").field("type", "ip").field("store", "false").field("index","not_analyzed").endObject()
                .startObject("className").field("type", "string").field("store", "false").field("index","not_analyzed").endObject()
                .startObject("method").field("type", "string").field("store", "false").field("index","not_analyzed").endObject()
                .startObject("operType").field("type", "string").field("store", "false").field("index","not_analyzed").endObject()
                .startObject("operObj").field("type", "string").field("store", "false").field("index","not_analyzed").endObject()
                .startObject("result").field("type", "string").field("store", "false").field("index","not_analyzed").endObject()
                .startObject("param").field("type", "string").field("store", "false").field("index","not_analyzed").endObject()
                .startObject("busParam").field("type", "string").field("store", "true").field("index","analyzed").field("analyzer","standard").endObject()
                .startObject("time").field("type", "long").field("store", "false").field("index","not_analyzed").endObject()
                .startObject("original").field("type", "string").field("store", "true").field("index","analyzed").field("analyzer","ik").endObject()
                .startObject("position").field("type", "long").field("store", "false").field("index","not_analyzed").endObject()
                .startObject("file").field("type", "string").field("store", "false").field("index","not_analyzed").endObject()
                .endObject().endObject().endObject();
        PutMappingRequest mapping = Requests.putMappingRequest(indexExName).type(indexType).source(builder);
        client.admin().indices().putMapping(mapping).actionGet();
        logger.info("ES create_mapping 创建es索引mapping成功，索引名称："+indexExName+",type="+indexType);
    }
}
