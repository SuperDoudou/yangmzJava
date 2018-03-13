package com.yangmz.tool.deploy;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Logger;
import com.jcraft.jsch.Session;

import java.io.IOException;
import java.io.InputStream;

public class JschClient {
    protected String host;
    protected String user;
    protected String password ;
    protected Session session;
    private Logger log;
    protected void openLog(){
        log = new com.jcraft.jsch.Logger() {
            public boolean isEnabled(int i) {
                //开启、关闭调试
                return true;
            }
            public void log(int i, String s) {
                //打印日志
                System.out.println(s);
            }
        };
        JSch.setLogger(log);
    }
    protected Session getSession(String host,String user,String password){
        JSch jsch = new JSch();

        try {
            session = jsch.getSession(user, host, 22);
            session.setConfig("PreferredAuthentications","password");
            session.setConfig("StrictHostKeyChecking","no");
            session.setPassword(password);
            session.connect(3000);   // making a connection with timeout.
            if(session.isConnected()){
                System.out.println(this.getClass().getSimpleName() + "session connect success");
            }else{
                System.out.println(this.getClass().getSimpleName() + "session connect fail");
            }
        } catch (JSchException e) {
            e.printStackTrace();
        }
        return  session;
    }

    protected void printResult(InputStream input){
        try {
            if(input.available()>0) {
                byte[] data = new byte[input.available()];
                input.read(data);
                String result = new String(data,"UTF-8");
                System.out.print(result);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
