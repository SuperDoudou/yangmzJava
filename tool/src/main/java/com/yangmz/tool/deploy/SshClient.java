package com.yangmz.tool.deploy;

import com.jcraft.jsch.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SshClient extends JschClient{
    private ChannelShell channel;
    private InputStream inputStream;
    private OutputStream outputStream;
    private int sleepTime = 800;
    public SshClient(String host,String user,String password){
        this.host = host;
        this.user = user;
        this.password = password;
    }
    public void connectSsh() {
        try {
            session = getSession(host,user,password);
            channel = (ChannelShell)session.openChannel("shell");
            channel.connect();
            inputStream = channel.getInputStream();
            outputStream = channel.getOutputStream();
            Thread.sleep(1000);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
    public void execCommand(String command) {
        try {
            outputStream.write((command+"\n").getBytes());
            outputStream.flush();
            Thread.sleep(sleepTime);
            printResult(inputStream);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void disconnectSsh(){

        channel.disconnect();
        session.disconnect();
        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
