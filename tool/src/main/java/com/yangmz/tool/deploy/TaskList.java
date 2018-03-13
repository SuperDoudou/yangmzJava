package com.yangmz.tool.deploy;

import java.io.*;

public class TaskList {

    private File file = null;
    private BufferedReader bufferReader = null;
    private String srcPath;
    private String desPath;
    private String shellCommand;



    private String batCommand;
    private String commandType;

    /**
     * 读入命令文件
     * @param filePath
     */
    public TaskList(String filePath){

        file = new File(filePath);
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(file);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
        if (fileReader != null) {
            bufferReader = new BufferedReader(fileReader);
        }
    }

    /**
     * 将命令读进TaskList类中
     * @return commandType
     */
    public String readCommand(){
        String command = null;
        commandType = "";
        try {
            command = bufferReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //读到文件尾部，返回null
        if(command == null){
            return null;
        }
        if(command.length() == 0) {
            return commandType;
        }
        if(command.matches("^sh:[\\w\\W]*")){
            commandType = "sh";
            command = command.replaceFirst("^sh:","");
            command = command.trim();
            shellCommand = command;
            return  commandType;
        }
        if(command.matches("^sftp:[\\w\\W]*")){
            commandType = "sftp";
            command = command.replaceAll("^sftp:","");
            command = command.trim();
            String[] sftpPath = command.split("[ ]+");
            srcPath = sftpPath[0];
            desPath = sftpPath[1];
            return commandType;
        }
        if(command.matches("^bat:[\\w\\W]*")){
            commandType = "bat";
            command = command.replaceFirst("^bat:","");
            command = command.trim();
            batCommand = command;
            return  commandType;
        }
        return commandType;
    }

    /**
     * 必须关闭
     */
    public void close(){
        try {
            bufferReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String getBatCommand() {
        return batCommand;
    }
    public String getSrcPath() {
        return srcPath;
    }

    public String getDesPath() {
        return desPath;
    }

    public String getShellCommand() {
        return shellCommand;
    }

    public String getCommandType() {
        return commandType;
    }
}
