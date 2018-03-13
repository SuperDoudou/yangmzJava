package com.yangmz.base.helper;

import com.yangmz.base.env.Env;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.*;

@Service
public class BaseHelper {

    public static String RAND_BASE = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIGKLMNOPQRSTUVWXYZ";


    public static String imagePathToMin(String path){
        File file = new File(path);
        String fileName = file.getName();
        if ( fileName.matches(".*_min\\.[\\w]*$") ){
            return path;
        }
        return path.replaceFirst("\\.([\\w]*)$", "_min.$1");
    }

    public static String imagePathToNormal(String path){
        File file = new File(path);
        String fileName = file.getName();
        if ( fileName.matches(".*_normal\\.[\\w]*$") ){
            return path;
        }
        return path.replaceFirst("\\.([\\w]*)$", "_normal.$1");
    }

    public static String imagePathClear(String path){
        File file = new File(path);
        String fileName = file.getName();
        if ( fileName.matches(".*_normal\\.[\\w]*$")){
            return path.replaceFirst("_normal\\.([\\w]*)$", ".$1");
        }
        if ( fileName.matches(".*_min\\.[\\w]*$")) {
            return path.replaceFirst("_min\\.([\\w]*)$", ".$1");
        }
        return path;

    }
    /**
     * 生成6位验证码，不到6位重新生成，
     * 100次失败使用123456
     * @return
     */
    public static String generateVerifyCode(){
        Random random = new Random();
        String code = "123456";
        int maxCode = 1000000;
        for(int i=0;i<100;i++) {
            int intCode = random.nextInt(maxCode);
            if( intCode < maxCode/10){
                continue;
            }
            code = Integer.toString(intCode);
            break;
        }
        return code;
    }
    /**
     * 生成 N位 带字母乱码，
     * @return rand code with length
     */
    public static String generateRandCode(int length){
        Random random = new Random();
        String code = "";
        String chars = RAND_BASE;
        int maxCode = chars.length();
        for(int i=0;i<length;i++) {
            int intCode = random.nextInt(maxCode);

            code = code + chars.charAt(intCode);
        }
        return code;
    }
    public static String generateToken(){

        //token 长度为50
        int length = 50;
        Random random = new Random();
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(RAND_BASE.length());
            stringBuffer.append(RAND_BASE.charAt(number));
        }
        return stringBuffer.toString();

    }

    public static boolean isValidEmail(String email) {
        if ( email == null ) {
            return false;
        }

        if (email.matches("^\\w+([\\.-]?\\w+)*@\\w+[-\\w]+(\\.\\w{1,4}){1,3}$")){
            return true;
        }
        return false;
    }

    public static Boolean chown(String user, String file){
        String command = "chown " + user + " " + file;
        try {
            Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
    public static Boolean chown(String user, String group, String file){
        String command = "chown " + user + ":" + group + " " + file;
        try {
            Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static Boolean deleteFile(String file){
        if (file == null){
            return false;
        }
        File filePath = new File(file);
        return filePath.delete();
    }

    public static Boolean mkdir(String path){
        if (path == null){
            return false;
        }
        File file = new File(path);
        if(file.isFile()){
            return false;
        }
        if(file.isDirectory()) {
            return true;
        }
        if(!file.mkdir()){
            return false;
        }
        BaseHelper.chown(Env.NginxUser, Env.YangmzGroup, file.getAbsolutePath());
        return true;
    }
    public static String MD5(String s) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(s.getBytes("utf-8"));
            return toHex(bytes);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String toHex(byte[] bytes) {

        final char[] HEX_DIGITS = "0123456789ABCDEF".toCharArray();
        StringBuilder ret = new StringBuilder(bytes.length * 2);
        for (int i=0; i<bytes.length; i++) {
            ret.append(HEX_DIGITS[(bytes[i] >> 4) & 0x0f]);
            ret.append(HEX_DIGITS[bytes[i] & 0x0f]);
        }
        return ret.toString();
    }
    public static String getFileSuffix(String filePath){
        String[] suffixArray = {"jpg", "png", "jpeg", "bmp"};
        for ( String suffix: suffixArray ) {
            if ( filePath.endsWith(suffix)){
                return suffix;
            }
        }
        return null;
    }
    public static Integer getWidth(String filePath){
        return getImageProperty(filePath,"width");
    }
    public static Integer getHeight(String filePath)
    {
        return getImageProperty(filePath,"height");
    }


    //property width | height
    public static Integer getImageProperty(String filePath,String property){
        File file = new File(filePath);
        if ( !property.equals("width") && !property.equals("height") ){
            return null;
        }
        String suffix = BaseHelper.getFileSuffix(filePath);
        if(suffix == null){
            return null;
        }
        ImageInputStream imageInputStream = null;
        int value = -1;
        try {
            Iterator<ImageReader> readerIterator = ImageIO.getImageReadersBySuffix(suffix);
            ImageReader reader = readerIterator.next();
            imageInputStream = ImageIO.createImageInputStream(file);
            reader.setInput(imageInputStream, true);
            if( property.equals("width") ) {
                value = reader.getWidth(0);
            }
            if( property.equals("height") ) {
                value = reader.getHeight(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if( imageInputStream != null) {
                try {
                    imageInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return value;
    }
}
