package com.yangmz.base.service;

import com.yangmz.base.property.EmailProperty;
import org.springframework.beans.factory.annotation.Autowired;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.security.Security;
import java.util.Properties;

public class EmailService {

    private EmailProperty emailProperty;

    public EmailService(EmailProperty emailProperty){
        this.emailProperty = emailProperty;
    }

    final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

    public String sendEmail(String destAddress, String title, String text)
    {
        // 配置发送邮件的环境属性
        final Properties props = new Properties();
        // 表示SMTP发送邮件，需要进行身份验证
        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());

        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", emailProperty.getHost());
        props.put("mail.smtp.port", emailProperty.getPort());
        props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
        props.setProperty("mail.smtp.socketFactory.port", emailProperty.getPort());
        props.setProperty("mail.smtp.socketFactory.fallback", "false");

        // 如果使用ssl，则去掉使用25端口的配置，进行如下配置,
        // props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        // props.put("mail.smtp.socketFactory.port", "465");
        // props.put("mail.smtp.port", "465");
        // 发件人的账号
        props.put("mail.user", emailProperty.getUserName());
        // 访问SMTP服务时需要提供的密码
        props.put("mail.password", emailProperty.getPassword());

        // 构建授权信息，用于进行SMTP进行身份验证
        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                // 用户名、密码
                String userName = props.getProperty("mail.user");
                String password = props.getProperty("mail.password");
                return new PasswordAuthentication(userName, password);
            }
        };
        // 使用环境属性和授权信息，创建邮件会话
        Session mailSession = Session.getInstance(props, authenticator);
//        mailSession.setDebug(true);
        // 创建邮件消息
        MimeMessage message = new MimeMessage(mailSession);
        try {
            // 设置发件人
            String nickName = javax.mail.internet.MimeUtility.encodeText(emailProperty.getNickName());
            InternetAddress from = new InternetAddress(nickName +
                    " <" + emailProperty.getUserName() + ">");

            message.setFrom(from);
            Address[] a = new Address[1];
            a[0] = new InternetAddress(destAddress);
            message.setReplyTo(a);
            // 设置收件人
            InternetAddress to = new InternetAddress(destAddress);
            message.setRecipient(MimeMessage.RecipientType.TO, to);
            // 设置邮件标题
            message.setSubject(title);
            // 设置邮件的内容体
            message.setContent(text, "text/html;charset=UTF-8");
            // 发送邮件
            Transport.send(message);
            return "send email success!!";
        }
        catch (MessagingException | UnsupportedEncodingException e) {
            return e.getMessage();
        }
    }
}
