package com.yangmz.base.dao.test;

import com.yangmz.base.dao.ImageDao;
import com.yangmz.base.helper.BaseHelper;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class ImageDaoTest {
    @Test
    public void TestGetFileSuffix(){
        ImageDao imageDao = new ImageDao();
        String suffix = BaseHelper.getFileSuffix("/opt/yangmz/hehe.jpg");
        assertEquals("jpg", suffix);
        suffix = BaseHelper.getFileSuffix("/opt/yangm..z/h..ehe.png");
        assertEquals("png", suffix);
        suffix = BaseHelper.getFileSuffix("/opt/yangm..z/h..ehe.aa");
        assertEquals(null, suffix);
    }
    @Test
    public void TestSplit() {
        String fileName = "xfda.png";
        String[] stringArray = fileName.split("\\.");
        System.out.println(stringArray[stringArray.length-1]);
    }
    @Test
    public void TestFileParent(){
        File file = new File("/home/yangmz/hehe.png");
        File dir = new File(file.getParent());
        System.out.println(dir.getAbsolutePath());
    }
    @Test
    public void TestChangePath(){
        String path = "/home/jhehe/im/hale.jpg";
        String normalPath = BaseHelper.imagePathToNormal(path);
        System.out.println(normalPath);

        System.out.println(BaseHelper.imagePathClear(normalPath));
    }

}
