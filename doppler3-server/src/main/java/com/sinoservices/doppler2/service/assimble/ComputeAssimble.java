package com.sinoservices.doppler2.service.assimble;

import com.sinoservices.doppler2.Constant;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Charlie
 *         To change this template use File | Settings | File Templates.
 */
public class ComputeAssimble {

    public static void main(String[] a){
        long startOffset = ComputeAssimble.getStartOffset(1,-1);
        long endOffset = ComputeAssimble.getEndOffset(1,-1);
        System.out.println(startOffset+"_"+endOffset);
    }


    public static long getStartOffset(long currentOffset, int forward) {
        long startOffset;
        if(forward < 0){
            startOffset = currentOffset - Constant.LINT_MSG_LENGTH * Constant.LOG_ESTIMATE_PAGE_SIZE;
        }else if(forward > 0){
            startOffset = currentOffset +1;
        }else{
            startOffset = currentOffset - Constant.LINT_MSG_LENGTH * Constant.LOG_ESTIMATE_PAGE_SIZE;
        }
        return startOffset < 0 ? 0 : startOffset;
    }

    public static long getEndOffset(long currentOffset, int forward) {
        long endOffset;
        if(forward < 0){
            endOffset = currentOffset -1;
        }else if(forward > 0){
            endOffset = currentOffset + Constant.LINT_MSG_LENGTH * Constant.LOG_ESTIMATE_PAGE_SIZE;
        }else{
            endOffset = currentOffset + Constant.LINT_MSG_LENGTH * Constant.LOG_ESTIMATE_PAGE_SIZE;
        }
        return endOffset < 0 ? 0 : endOffset;
    }



}
