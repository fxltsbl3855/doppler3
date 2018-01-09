package com.sinoservices.doppler2.facade.assimble;

import com.sinoservices.doppler2.bo.DashboardBo;
import com.sinoservices.doppler2.bo.ReqDetail;
import com.sinoservices.util.JsonUtils;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Charlie
 *         To change this template use File | Settings | File Templates.
 */
public class FacadeAssimble {
    public static void sortList(DashboardBo dashboardBo) {

        if(dashboardBo.getAppReqList()!=null&& dashboardBo.getAppReqList().size()>0) {
            Collections.sort(dashboardBo.getAppReqList());
        }
        if(dashboardBo.getModuleReqList()!=null&&dashboardBo.getModuleReqList().size()>0) {
            Collections.sort(dashboardBo.getModuleReqList());
        }
        if(dashboardBo.getErrorStat()!=null&&dashboardBo.getErrorStat().size()>0) {
            Collections.sort(dashboardBo.getErrorStat());
        }
    }

    public static void main(String[] a){
//        DashboardBo dashboardBo = new DashboardBo();
//        List<ReqDetail> appReqList= new ArrayList<ReqDetail>();
//        ReqDetail q1 = new ReqDetail();
//        q1.setName("p1");
//        q1.setNum(1);
//        ReqDetail q2 = new ReqDetail();
//        q2.setName("p2");
//        q2.setNum(2);
//        appReqList.add(q2);
//        appReqList.add(q1);
//        dashboardBo.setAppReqList(appReqList);
//        System.out.println(JsonUtils.object2Json(dashboardBo));
//        sortList(dashboardBo);
//        System.out.println(JsonUtils.object2Json(dashboardBo));
    }
}
