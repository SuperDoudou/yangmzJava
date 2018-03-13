package com.yangmz.tool.test.JobTest;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Ttest {
    void test () {
        ConcurrentHashMap<String,String> map =  new ConcurrentHashMap<String, String>();
        String hehe = map.get("hehe");

        HashMap<String,String> m1 = new HashMap<String, String>();
        m1.put("hehe","dada");
        m1.get("haha");
        Back back = new Back();
        back.run();
        back.start();
        new Thread(new Run()).start();
    }
    Executors.newScheduledThreadPool

    class Run implements Runnable {

        public void run() {

        }
    }
    class Back extends Thread {
        public void run() {
        }
    }
}
