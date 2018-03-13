package com.yangmz.app.test;

import com.yangmz.app.base.TextItem;
import com.yangmz.app.dao.ArticleDao;
import com.yangmz.app.model.Article;
import com.yangmz.base.client.MybatisClient;
import org.junit.Test;
import org.mockito.Mock;

import java.util.List;

public class AppTest {
    @Mock
    private MybatisClient mybatisClient;  // 模拟对象

    @Test
    public void testGetItemFromText() {
        String text = "imageId_yangmz={97}text_yangmz={12teximageId_yangmz={t_yangmz=3}";
        List<TextItem> textItems = Article.getItemFromText(text);
        int i = 0;
        if (textItems == null){
            return;
        }
        for (TextItem textItem: textItems) {
            System.out.println("Item "+ String.valueOf(i) + ": ");
            System.out.println(textItem.toString());
            i++;
        }
    }

    @Test
    public void testString2Long() {
        Long id = null;
        try {
            id = Long.valueOf("adsf");
        }catch (NumberFormatException e){
            System.out.println("String error");
        }

        System.out.println(id);
    }
}
