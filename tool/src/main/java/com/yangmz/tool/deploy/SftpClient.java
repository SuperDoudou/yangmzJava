package com.yangmz.tool.deploy;

import com.jcraft.jsch.*;


public class SftpClient extends JschClient{

    private ChannelSftp channel;
    private Boolean busy;
    private SftpProgressMonitor monitor;
    private int sleepTime = 1000;

    public SftpClient(String host,String user,String password){
        this.host = host;
        this.user = user;
        this.password = password;
        busy = false;
        monitor = new MyProgressMonitor();
    }

    public void connectSftp() {
        try {
            session = getSession(host,user,password);
            channel = (ChannelSftp) session.openChannel("sftp");
            channel.connect();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public void uploadFile(String srcFilePath, String desFilePath) {
        int mode=ChannelSftp.OVERWRITE;
        try {

            busy = true;
            channel.put(srcFilePath,desFilePath,monitor,mode);
            Thread.sleep(sleepTime);
            while( busy ){
                Thread.sleep(sleepTime);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void disconnectSftp(){
        channel.disconnect();
        session.disconnect();
    }

    public class MyProgressMonitor implements SftpProgressMonitor{

        long count=0;
        long max=0;
        public void init(int op, String src, String dest, long max){

            System.out.println("sftp>> put " + src +" to " + dest);
            this.max = max;
        }
        public boolean count(long count){
            this.count = count;
            return true;
        }
        public void end(){
            busy = false;
            System.out.println("sftp>> upload end");
        }
    }

}
