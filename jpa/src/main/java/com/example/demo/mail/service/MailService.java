package com.example.demo.mail.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender emailSender;

    public void sendEmail(String toEmail,
                          String title,
                          String text) {
        MimeMessagePreparator emailForm = createEmailForm(toEmail, title, text);
        try {
            emailSender.send(emailForm);
        } catch (RuntimeException e) {
            log.debug("MailService.sendEmail exception occur toEmail: {}, " +
                    "title: {}, text: {}", toEmail, title, text);
            throw new RuntimeException(e);
        }
    }

    // 발신할 이메일 데이터 세팅
    private MimeMessagePreparator createEmailForm(String toEmail,
                                              String title,
                                              String text) {

        return mimeMessage -> {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");

            String content = String.format(
                    "<div style=\"display: flex; flex-direction: column; align-items: center;\">\n" +
                    "    <h1>1nterface 회원가입</h1>\n" +
                    "    <br/>\n" +
                    "    <br/>\n" +
                    "    <p>회원 가입을 계속하시려면 아래의 인증코드를 입력하세요.</p>\n" +
                    "    <br/>\n" +
                    "    <p>인증번호</p>\n" +
                    "    <p style=\"border: 1px solid black; padding: 5px;\">%s</p>\n" +
                    "    <br/>\n" +
                    "    <br/>\n" +
                    "    <p>(본인이 계정을 생성하지 않은 경우 이 이메일은 무시해도 됩니다.)</p>\n" +
                    "</div>\n", text);

            helper.setTo(toEmail);
            helper.setSubject(title);

            helper.setText(content, true); //html 타입이므로, 두번째 파라미터에 true 설정
        };
    }
}
