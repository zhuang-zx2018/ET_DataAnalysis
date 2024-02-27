package com.gagc.dataanalysis.service.impl;

import com.gagc.dataanalysis.service.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.net.InetAddress;

/**
 * 邮件发送
 *
 */
@Slf4j
@Service
public class MailServiceImpl implements MailService {


    @Value("${spring.mail.username}")
    private String from;

    @Value("${spring.mail.tousername}")
    private String to;
    @Autowired
    private JavaMailSender mailSender;
    private String COMMA = ",";

    @Override
    public void sendEmail(String title,String content) {
        MimeMessage message = mailSender.createMimeMessage();
        String[] tos = to.split(COMMA);
        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(tos);
//            helper.setCc(ccs);
            helper.setSubject(title);
            if (StringUtils.isEmpty(content)) {
                StringBuilder htmlBuilder = new StringBuilder();
                htmlBuilder.append("<html><head></head>");
                htmlBuilder.append("<body><p>您好：</p></body>");
                htmlBuilder.append("<body><p>本邮件为系统自动发送。当前发送邮件服务器IP:"+InetAddress.getLocalHost()+"</p></body>");
                htmlBuilder.append("<body><p>祝好！</p></body>");
                htmlBuilder.append("</html>");
                content = htmlBuilder.toString();
            }
            helper.setText(content, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            log.warn("发送附件到邮件失败：{}", e);
        } catch (IOException e) {
            log.warn("发送附件到邮件失败io：{}", e);
        }
    }
}
