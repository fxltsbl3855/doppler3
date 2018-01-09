package com.sinoservices;

import com.sinoservices.parser.util.FastJsonUtil;
import com.sinoservices.util.JsonUtils;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Charlie
 *         To change this template use File | Settings | File Templates.
 */
public class TestToString {

    public static void main(String[] args) {
        Order o = new Order();
        o.setName("dsa");
        o.setId(3);
        System.out.println(beanToStr(""));
    }

    public static String beanToStr(Object oo){
        return FastJsonUtil.object2Json(oo);
    }
}

class Order{
    private String name;
    private int id;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

}
