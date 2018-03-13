package com.yangmz.sso.test;

import com.yangmz.base.property.EmailProperty;
import com.yangmz.base.helper.BaseHelper;
import org.junit.Test;

public class SsoTest {

    EmailProperty emailProperty = new EmailProperty("/conf/email.properties");
    @Test
    public void testAdd() {
    }

    @Test
    public void testDivide() {
        System.out.println(emailProperty.getNickName());
    }

    @Test
    public void testRand() {
        System.out.println(BaseHelper.generateRandCode(20));
    }
}
