package com.sinoservices;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Charlie
 *         To change this template use File | Settings | File Templates.
 */
public class TestThreadTask extends Thread {

    public TestThreadTask(){
        System.out.println(this.getName()+"oooooo1ooo");
        this.setName("Hello-bird");
        System.out.println(this.getName()+"ooooo2oooo");
    }

    public void run(){

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(this.getName()+"oooo3ooooo_"+this.isInterrupted());
    }
}
