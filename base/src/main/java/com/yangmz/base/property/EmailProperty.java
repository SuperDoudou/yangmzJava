package com.yangmz.base.property;

import java.io.*;
import java.util.Properties;

public class EmailProperty {
    private String propertyPath = null;
    private String HOST = "mailHost";
    private String PASSWORD = "mailPassword";
    private String USER_NAME = "mailUserName";
    private String PORT = "mailPort";
    private String NICK_NAME = "mailUserNickName";

    private String host = null;
    private String password = null;
    private String port = null;
    private String userName = null;
    private String nickName = null;

    public EmailProperty(String propertyPath) {
        this.propertyPath = propertyPath;
        Properties properties = new Properties();
        String filePath = this.getClass().getResource(propertyPath).getPath();
        File file = new File(filePath);


        try(FileReader fileReader = new FileReader(file);
            BufferedReader read = new BufferedReader(fileReader) ){
            properties.load(read);
        } catch (IOException e) {
            e.printStackTrace();
        }

        host = properties.getProperty(HOST);
        password = properties.getProperty(PASSWORD);
        port = properties.getProperty(PORT);
        userName = properties.getProperty(USER_NAME);
        nickName = properties.getProperty(NICK_NAME);
    }

    public String getPropertyPath() {
        return propertyPath;
    }

    public void setPropertyPath(String propertyPath) {
        this.propertyPath = propertyPath;
    }

    public String getHost() {
        return host;
    }

    public String getPassword() {
        return password;
    }

    public String getPort() {
        return port;
    }

    public String getUserName(){
        return userName;
    }

    public String getNickName() {
        return nickName;
    }
}
