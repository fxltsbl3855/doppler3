package com.sinoservices.doppler2.exception;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Charlie
 *         To change this template use File | Settings | File Templates.
 */
public class ESParamException extends RuntimeException {

    public ESParamException(){
        super();
    }

    public ESParamException(String msg){
        super(msg);
    }

    public ESParamException(Throwable e){
        super(e);
    }

    public ESParamException(String msg, Throwable e){
        super(msg, e);
    }

}
