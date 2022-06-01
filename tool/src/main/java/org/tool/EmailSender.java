package org.tool;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailSender {
    /* 邮箱账户名 */
    private String from = "lha602@163.com";
    /* 邮箱登录密码 */
    private String password = "";
    /* 邮件服务器地址(这里是网易邮箱服务器) */
    private String host = "smtp.163.com";
    private Properties properties;
    private Session session;

    public EmailSender() {
        this.properties = System.getProperties();
        this.properties.put("mail.smtp.host", host);
        this.properties.put("mail.smtp.auth", "true");
        Authenticator auth = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        };
        this.session = Session.getDefaultInstance(properties,auth);
    }

    /**
     * 发送邮件
     * @param to 收件人邮箱地址
     * @param title 邮件标题
     * @param message 邮件内容
     */
    public void send(String to, String title, String message) {
        try {
            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress(this.from));
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            mimeMessage.setSubject(title);
            mimeMessage.setText(message);
            Transport.send(mimeMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 测试
     * @param args
     */
    public static void main(String[] args) {
        EmailSender sender = new EmailSender();
        sender.send("1026792451@qq.com", "验证码", "123456789");
    }
}
