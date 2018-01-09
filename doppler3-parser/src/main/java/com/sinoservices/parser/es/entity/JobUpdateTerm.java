package com.sinoservices.parser.es.entity;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Charlie
 *         To change this template use File | Settings | File Templates.
 */
public class JobUpdateTerm {

    Map<String,String> term;

    public Map<String, String> getTerm() {
        return term;
    }

    public void setTerm(Map<String, String> term) {
        this.term = term;
    }
}
