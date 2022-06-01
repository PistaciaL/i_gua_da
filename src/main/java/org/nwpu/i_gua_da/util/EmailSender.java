package org.nwpu.i_gua_da.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Component
public class EmailSender {

    @Value("${spring.mail.from}")
    private String from;

    @Autowired
    private JavaMailSender javaMailSender;

    /**
     * 发送邮件
     * @param to 收件人邮箱地址
     * @param code 验证码内容
     * @param operationName 操作名字(比如"注册账号", "修改密码"等)
     */
    public void send(String to, String code, String operationName) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom(from);
        helper.setTo(to);
        helper.setSubject("\"爱瓜大\"平台验证码");
        helper.setText(messageContentDecorate(code, operationName), true);
        javaMailSender.send(mimeMessage);
//        try {
//            MimeMessage mimeMessage = new MimeMessage(session);
//            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
//            helper.setFrom(new InternetAddress(this.from));
//            helper.setTo(new InternetAddress(to));
//            helper.setSubject(title);
//            String msg = messageContentDecorate(code, operationName);
//            helper.setText(msg, true);
//            Transport.send(mimeMessage);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    /**
     * 对邮件内容进行修饰(样式美化)
     * @param code 验证码内容
     * @param operationName 操作名称
     * @return 美化后的内容
     */
    private String messageContentDecorate(String code, String operationName) {

        String msg = "<head>\n" +
                "    <base target=\"_blank\" />\n" +
                "    <style type=\"text/css\">\n" +
                "         ::-webkit-scrollbar {\n" +
                "            display: none;\n" +
                "        }\n" +
                "    </style>\n" +
                "    <style id=\"cloudAttachStyle\" type=\"text/css\">\n" +
                "        #divNeteaseBigAttach,\n" +
                "        #divNeteaseBigAttach_bak {\n" +
                "            display: none;\n" +
                "        }\n" +
                "    </style>\n" +
                "    <style id=\"blockquoteStyle\" type=\"text/css\">\n" +
                "        blockquote {\n" +
                "            display: none;\n" +
                "        }\n" +
                "    </style>\n" +
                "    <style type=\"text/css\">\n" +
                "        body {\n" +
                "            font-size: 14px;\n" +
                "            font-family: arial, verdana, sans-serif;\n" +
                "            line-height: 1.666;\n" +
                "            padding: 0;\n" +
                "            margin: 0;\n" +
                "            overflow: auto;\n" +
                "            white-space: normal;\n" +
                "            word-wrap: break-wor d;\n" +
                "            min-height: 100px\n" +
                "        }\n" +
                "        \n" +
                "        td,\n" +
                "        input,\n" +
                "        button,\n" +
                "        select,\n" +
                "        body {\n" +
                "            font-family: Helvetica, 'Microsoft Yahei', verdana\n" +
                "        }\n" +
                "        \n" +
                "        pre {\n" +
                "            white-space: pre-wrap;\n" +
                "            white-space: -moz-pre-wrap;\n" +
                "            white-space: -pre-wrap;\n" +
                "            white-space: -o-pre-wrap;\n" +
                "            word-wrap: break-word;\n" +
                "            width: 95%\n" +
                "        }\n" +
                "        \n" +
                "        th,\n" +
                "        td {\n" +
                "            font-family: arial, verdana, sans-serif;\n" +
                "            line-height: 1.666\n" +
                "        }\n" +
                "        \n" +
                "        img {\n" +
                "            border: 0\n" +
                "        }\n" +
                "        \n" +
                "        header,\n" +
                "        footer,\n" +
                "        section,\n" +
                "        aside,\n" +
                "        article,\n" +
                "        nav,\n" +
                "        hgroup,\n" +
                "        figure,\n" +
                "        figcaption {\n" +
                "            display: block\n" +
                "        }\n" +
                "        \n" +
                "        blockquote {\n" +
                "            margin-right: 0px\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "\n" +
                "<body tabindex=\"0\" role=\"listitem\">\n" +
                "    <table width=\"700\" border=\"0\" align=\"center\" cellspacing=\"0\" style=\"width:700px;\">\n" +
                "        <tbody>\n" +
                "            <tr>\n" +
                "                <td>\n" +
                "                    <div style=\"width:700px;margin:0 auto;border-bottom:1px solid #ccc;margin-bottom:30px;\">\n" +
                "                        <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"700\" height=\"39\" style=\"font:12px Tahoma, Arial, 宋体;\">\n" +
                "                            <tbody>\n" +
                "                                <tr>\n" +
                "                                    <td width=\"210\"></td>\n" +
                "                                </tr>\n" +
                "                            </tbody>\n" +
                "                        </table>\n" +
                "                    </div>\n" +
                "                    <div style=\"width:680px;padding:0 10px;margin:0 auto;\">\n" +
                "                        <div style=\"line-height:1.5;font-size:14px;margin-bottom:25px;color:#4d4d4d;\">\n" +
                "                            <strong style=\"display:block;margin-bottom:15px;\">尊敬的⽤户：<span style=\"color:#f60;font-size: 16px;\"></span>您好！</strong>\n" +
                "                            <strong style=\"display:block;margin-bottom:15px;\">\n" +
                "    您正在进⾏<span style=\"color: red\">"+ operationName +"</span>操作，请在验证码输⼊框中输⼊：<span style=\"color:#f60;font-size: 24px\">"+ code +"</sp\n" +
                "    an>，以完成操作。\n" +
                "    </strong>\n" +
                "                        </div>\n" +
                "                        <div style=\"margin-bottom:30px;\">\n" +
                "                            <small style=\"display:block;margin-bottom:20px;font-size:12px;\">\n" +
                "    <p style=\"color:#747474;\">\n" +
                "    注意：此操作可能会修改您的密码、登录邮箱或绑定⼿机。如⾮本⼈操作，请及时登录并修改密码以保证帐户安全\n" +
                "    <br>（⼯作⼈员不会向你索取此验证码，请勿泄漏！)\n" +
                "    </p>\n" +
                "    </small>\n" +
                "                        </div>\n" +
                "                    </div>\n" +
                "                    <div style=\"width:700px;margin:0 auto;\">\n" +
                "                        <div style=\"padding:10px 10px 0;border-top:1px solid #ccc;color:#747474;margin-bottom:20px;line-height:1.3em;font-size:12px;\">\n" +
                "                            <p>此为系统邮件，请勿回复<br> 请保管好您的邮箱，避免账号被他⼈盗⽤\n" +
                "                            </p>\n" +
                "                            <p>2022爱瓜大团队</p>\n" +
                "                        </div>\n" +
                "                    </div>\n" +
                "                </td>\n" +
                "            </tr>\n" +
                "        </tbody>\n" +
                "    </table>\n" +
                "</body>";
        return msg;
    }
}