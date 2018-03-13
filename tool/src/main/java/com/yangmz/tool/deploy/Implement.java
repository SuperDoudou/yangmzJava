package com.yangmz.tool.deploy;


public class Implement {
    private String getPropertyPath(String FileName){
        String path = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        if(path.contains(":/") && path.charAt(0)=='/') {
            path = path.substring(1);
        }
        path = path.substring(0,path.lastIndexOf('/')+1);
        path = path + FileName;
        System.out.println("Finding file in:");
        System.out.println(path);
        return path;
    }

    public static void main(String args[]) {
        Implement implement = new Implement();
        String propertyPath = implement.getPropertyPath("host.properties");
        String shellPath = implement.getPropertyPath("deploy.sh");

        HostProperty hostProperty = new HostProperty(propertyPath);
        TaskList taskList = new TaskList(shellPath);

        hostProperty.readProperty();
        SshClient sshClient = new SshClient(hostProperty.getHost(), hostProperty.getUser(), hostProperty.getPassword());
        SftpClient sftpClient = new SftpClient(hostProperty.getHost(), hostProperty.getUser(), hostProperty.getPassword());
        BatClient reader = new BatClient();
        sshClient.connectSsh();
        sftpClient.connectSftp();
        String type;
        while ((type = taskList.readCommand()) != null){
            if (type.equals("sh")) {
                sshClient.execCommand(taskList.getShellCommand());
            }
            if (type.equals("sftp")) {
                sftpClient.uploadFile(taskList.getSrcPath(), taskList.getDesPath());
            }
            if (type.equals("bat")) {
                reader.execCommand(taskList.getBatCommand());
            }
        }
        taskList.close();
        sshClient.disconnectSsh();
        sftpClient.disconnectSftp();
    }
}
