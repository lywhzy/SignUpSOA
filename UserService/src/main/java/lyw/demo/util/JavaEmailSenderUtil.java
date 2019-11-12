package lyw.demo.util;

import com.sun.mail.util.MailSSLSocketFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.Properties;

public class JavaEmailSenderUtil {

    public static void sendEmail(String toEmailAddress, String emailTitle, String emailContent) throws GeneralSecurityException, MessagingException {
        Properties props = new Properties();

        // 开启debug模式
        props.setProperty("mail.debug", "true");
        // 发送服务器需要身份验证
        props.setProperty("mail.smtp.auth", "true");
        // 设置邮件服务器主机名
        props.setProperty("mail.host", "smtp.qq.com");
        // 发送邮件协议名称
        props.setProperty("mail.transport.protocol", "smtp");

        // SSL认证，注意腾讯邮箱是基于SSL加密的，所有需要开启才可以使用
        MailSSLSocketFactory sf = new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.ssl.socketFactory", "smtp");

        // 使用JavaMail发送邮件的五个步骤
        // 1.创建session会话
        Session session = Session.getInstance(props);

        // 4.创建邮件，发送的消息，基于观察者模式进行设计的
        Message msg = new MimeMessage(session);
        // 设置邮件标题
        msg.setSubject(emailTitle);
        // 使用StringBuilder，因为StringBuilder加载速度会比String快，而且线程安全性也不错
        StringBuilder builder = new StringBuilder();
        builder.append("\n" + emailContent);
        builder.append("\n时间 " + new Date());
        ((MimeMessage) msg).setText(builder.toString());
        // 邮件发送人
        msg.setFrom(new InternetAddress("1987152280@qq.com"));

        // 2.通过session得到transport对象
        Transport transport = session.getTransport();
        // 3.使用邮箱用户名和密码链接上邮件服务器，发送邮件时，发送人需要提交用户名和密码（授权码）给stmp服务器，用户名和密码都通过验证后
        // 才能正常发送邮件给收件人，QQ邮箱需要使用SSL，端口号465或587
        transport.connect("smtp.qq.com", 465, "1987152280", "relrzxahhyyacbie");
        // 5.发送消息
        transport.sendMessage(msg, new Address[] { new InternetAddress(toEmailAddress)});
        transport.close();
    }

    // 向toEmailAddress发送内容为4位验证码的邮件，并返回验证码
    public static int sendCodeEmail(String toEmailAddress, String content) throws GeneralSecurityException, MessagingException {
        int randNum = RandomUtil.getRandNum();// 4位验证码
        String emailContent = content + "" + randNum;
        sendEmail(toEmailAddress, "验证码", emailContent);
        return randNum;
    }
}
