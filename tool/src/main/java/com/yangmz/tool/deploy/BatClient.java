package com.yangmz.tool.deploy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class BatClient {
    public void execCommand(String command) {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
        InputStream is = process.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        try {
            process.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            printResult(reader);
            reader.close();
            is.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        process.destroy();
    }

    private void printResult(BufferedReader reader){
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
