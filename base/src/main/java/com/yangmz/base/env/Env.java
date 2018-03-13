package com.yangmz.base.env;

public class Env {
    static public String NginxUser = "nginx";
    static public String YangmzUser = "yangmz";
    static public String YangmzGroup = "yangmz";
    static public String HomePath = "/home/yangmz";
    static public String ImageCache = "/image/cache";

    static public String ServiceAddress = "http://127.0.0.1";
    static public String ServicePort = "8001";
    static public String CheckTokenService =  "/sso/checkToken";
    static public String GetUser =  "/user";
    static public String GetUserList =  "/user/list?userIdList=";

    static public String SaltWord = "www.sheep.com";
    static public Double JpgQuality = 0.90;
    static public Integer ArticleTitileLength = 6;
    static public Integer ArticlePerDir = 50;
    static public Integer CommentPerPage = 15;
    static public Integer ArticlePerPage = 10;
    static public Integer PortraitLength = 100;
    static public Integer PortraitNormalLength = 200;
    static public Integer ImageWidth = 800;
    static public Integer ImageHeight = 10000;
    public static Integer ArticleThumbnailLength = 400;
}
