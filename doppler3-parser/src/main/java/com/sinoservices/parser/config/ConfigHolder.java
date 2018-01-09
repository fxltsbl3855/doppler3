package com.sinoservices.parser.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Charlie
 *         To change this template use File | Settings | File Templates.
 */
@Repository
public class ConfigHolder {

    @Value("#{propertiesReader[sourceDir]}")
    public static String sourceDir = "";
    @Value("#{propertiesReader[bakDir]}")
    public static String bakDir = "";


    @Value("#{propertiesReader[es_host]}")
    public static String esHost = "";
    @Value("#{propertiesReader[es_port]}")
    public static String esPort = "";

    public static String getSourceDir() {
        return sourceDir;
    }

    public static void setSourceDir(String sourceDir) {
        ConfigHolder.sourceDir = sourceDir;
    }

    public static String getBakDir() {
        return bakDir;
    }

    public static void setBakDir(String bakDir) {
        ConfigHolder.bakDir = bakDir;
    }

    public static String getEsHost() {
        return esHost;
    }

    public static void setEsHost(String esHost) {
        ConfigHolder.esHost = esHost;
    }

    public static String getEsPort() {
        return esPort;
    }

    public static void setEsPort(String esPort) {
        ConfigHolder.esPort = esPort;
    }
}
