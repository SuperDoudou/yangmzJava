package com.yangmz.tool.deploy;

import java.io.FileInputStream;
import java.util.Properties;

public class HostProperty {
    private String filePath;
    private static final String HOST = "host";
    private static final String USER = "user";
    private static final String PASSWORD = "password";
    private String host;
    private String user;
    private String password;
    public HostProperty(String filePath) {
        this.filePath = filePath;
    }
    public void readProperty(){
        Properties properties = new Properties();
        try{
            properties.load(new FileInputStream(filePath));
        }catch (Exception e){
            System.out.printf(e.toString());
        }
        host = properties.getProperty(HOST);
        user = properties.getProperty(USER);
        password = properties.getProperty(PASSWORD);
        if(host == null || host.length() == 0){
            System.out.println("Can't get value in key(host)");
        }
        if(user == null || user.length() == 0){
            System.out.println("Can't get value in key(user)");
        }
        if(password == null || password.length() == 0){
            System.out.println("Can't get value in key(password)");
        }
    }
    public String getHost() {
        return host;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }
}
