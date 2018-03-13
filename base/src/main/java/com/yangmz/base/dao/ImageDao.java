package com.yangmz.base.dao;

import com.yangmz.base.env.Env;
import com.yangmz.base.helper.BaseHelper;
import net.coobird.thumbnailator.ThumbnailParameter;
import net.coobird.thumbnailator.Thumbnailator;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.filters.ImageFilter;
import net.coobird.thumbnailator.geometry.Position;
import net.coobird.thumbnailator.geometry.Positions;
import net.coobird.thumbnailator.util.ThumbnailatorUtils;
import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class ImageDao {

    String imageCachePath = Env.HomePath + Env.ImageCache;
    private static Logger logger = Logger.getLogger(ImageDao.class);

    public ImageDao (){
    }

    /*
    destPath  /xxx/xxx/aaa.jpg
     */

    public Boolean savePortrait(String destPath,HttpServletRequest request) throws IOException
    {
        TempFile tempFile = cacheFile(request);
        File file = new File(destPath);
        BaseHelper.mkdir(file.getParent());
        String normalPath = BaseHelper.imagePathToNormal(destPath);
        Thumbnails.of(new File(tempFile.getPath()))
                .sourceRegion(Positions.CENTER,tempFile.getLength(),tempFile.getLength())
                .size(Env.PortraitNormalLength,Env.PortraitNormalLength)
                .addFilter(new ThumbnailsImgFilter())
                .imageType(BufferedImage.TYPE_INT_ARGB)
                .outputFormat("jpg")
                .outputQuality(Env.JpgQuality)
                .toFile(normalPath);
        Thumbnails.of(new File(tempFile.getPath()))
                .sourceRegion(Positions.CENTER,tempFile.getLength(),tempFile.getLength())
                .size(Env.PortraitLength,Env.PortraitLength)
                .addFilter(new ThumbnailsImgFilter())
                .imageType(BufferedImage.TYPE_INT_ARGB)
                .outputFormat("jpg")
                .outputQuality(Env.JpgQuality)
                .toFile(destPath);
        BaseHelper.chown(Env.NginxUser, Env.YangmzGroup, normalPath);
        BaseHelper.chown(Env.NginxUser, Env.YangmzGroup, destPath);
        BaseHelper.deleteFile(tempFile.getPath());
        return true;
    }

    /*
    destPath  /xxx/xxx/aaa.jpg
     */
    public Boolean saveArticleImage(String destPath,HttpServletRequest request) throws IOException {
        TempFile tempFile = cacheFile(request);
        File file = new File(destPath);
        BaseHelper.mkdir(file.getParent());

        Thumbnails.of(new File(tempFile.getPath()))
                .sourceRegion(Positions.CENTER,tempFile.getWidth(),tempFile.getHeight())
                .size(Env.ImageWidth,Env.ImageHeight)
                .addFilter(new ThumbnailsImgFilter())
                .imageType(BufferedImage.TYPE_INT_ARGB)
                .outputFormat("jpg")
                .outputQuality(Env.JpgQuality)
                .toFile(destPath);
        BaseHelper.chown(Env.NginxUser, Env.YangmzGroup, destPath);
        BaseHelper.deleteFile(tempFile.getPath());
        return true;
    }


    private TempFile cacheFile (HttpServletRequest request) throws IOException {

        Long time = System.currentTimeMillis();
        String randCode = BaseHelper.generateRandCode(6);
        String cacheFileName = time.toString() + randCode;
        String cacheFilePath = imageCachePath + "/" + cacheFileName;
        //不需要传入后缀
        String suffix = saveImage(cacheFilePath, request);
        if (suffix == null | suffix.length() == 0){
            return null;
        }
        cacheFilePath = cacheFilePath+ "." + suffix;
        logger.info("cache file path is "+ cacheFilePath);
        Integer width = BaseHelper.getWidth(cacheFilePath);
        Integer height = BaseHelper.getHeight(cacheFilePath);
        Integer length = width < height ? width:height;
        if(length < 20){
            return null;
        }
        TempFile tempFile = new TempFile(width,height,length,cacheFilePath);
        return tempFile;
    }

    public Boolean createImageThumbnail(File file,File destFile) {
        if (file == null || !file.isFile()){
            return false;
        }
        String path = file.getAbsolutePath();
        String destPath = destFile.getAbsolutePath();
        String minDestPath = BaseHelper.imagePathToMin(destPath);
        Integer width = BaseHelper.getWidth(path);
        Integer height = BaseHelper.getHeight(path);
        Integer length = width < height?width:height;
        try {
            Thumbnails.of(file)
                    .sourceRegion(Positions.CENTER,length,length)
                    .size(Env.ArticleThumbnailLength,Env.ArticleThumbnailLength)
                    .addFilter(new ThumbnailsImgFilter())
                    .imageType(BufferedImage.TYPE_INT_ARGB)
                    .outputFormat("jpg")
                    .outputQuality(Env.JpgQuality)
                    .toFile(minDestPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     *将透明背景设置为白色
     */
    class ThumbnailsImgFilter implements ImageFilter {
        @Override
        public BufferedImage apply(BufferedImage img) {
            int w = img.getWidth();
            int h = img.getHeight();
            BufferedImage newImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphic = newImage.createGraphics();
            graphic.setColor(Color.white);//white
            graphic.fillRect(0, 0, w, h);
            graphic.drawRenderedImage(img, null);
            graphic.dispose();
            return newImage;
        }
    }

    /*
    从上传的文件中获取文件扩展名
     */
    public String saveImage(String destPath, HttpServletRequest request) throws IOException {
        long  startTime=System.currentTimeMillis();
        String suffix = null;
        //将当前上下文初始化给  CommonsMutipartResolver （多部分解析器）
        CommonsMultipartResolver multipartResolver=new CommonsMultipartResolver(
                request.getSession().getServletContext());
        //检查form中是否有enctype="multipart/form-data"
        if(multipartResolver.isMultipart(request))
        {
            //将request变成多部分request
            MultipartHttpServletRequest multiRequest=(MultipartHttpServletRequest)request;
            //获取multiRequest 中所有的文件名
            Iterator iter = multiRequest.getFileNames();

            if(!iter.hasNext()) {
                return null;
            }
            //一次遍历所有文件
            MultipartFile file=multiRequest.getFile(iter.next().toString());
            if(file == null){
                return null;
            }
            String[] stringArray = file.getOriginalFilename().split("\\.");
            if ( (stringArray == null) || (stringArray.length < 1 ) ){
                return null;
            }
            suffix = stringArray[stringArray.length-1];
            //上传
            file.transferTo(new File(destPath + "." + suffix));

        }
        return suffix;
    }




    private class TempFile {
        private int width;
        private int height;
        private int length;
        private String path;

        public TempFile(int width, int height, int length, String path) {
            this.width = width;
            this.height = height;
            this.length = length;
            this.path = path;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getLength() {
            return length;
        }

        public void setLength(int length) {
            this.length = length;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }
    }
}
