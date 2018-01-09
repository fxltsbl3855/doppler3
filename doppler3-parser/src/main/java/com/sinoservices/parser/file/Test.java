package com.sinoservices.parser.file;

import com.sinoservices.parser.config.UpMessageConfig;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Charlie
 *         To change this template use File | Settings | File Templates.
 */
public class Test {

    public static void main(String[] a){
        FileMonitor ff = new FileMonitor();
        UpMessageConfig.source_dir = "E:/ss/";
        UpMessageConfig.bak_dir = "E:/dd/";
        UpMessageConfig.es_addr="192.168.0.88:9300";
        UpMessageConfig.es_addr_http="192.168.0.88:9200";

        ff.start();
    }


}
