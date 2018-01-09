package com.sinoservices.doppler2;

import com.sinoservices.doppler2.es.ClientHelper;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Charlie
 *         To change this template use File | Settings | File Templates.
 */
public class ShutDownHook implements Runnable {

    public void run() {
        ClientHelper.getInstance().closePool();
        System.out.println("[conn] hook closePool");
    }


}
