package com.stech.quiz.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.stech.quiz.entity.QuizResult;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    public void sendQuizResultEmail(String to, String name, QuizResult result) {
        try {
            Context context = new Context();
            context.setVariable("name", name);
            context.setVariable("result", result);
            
            String emailContent = templateEngine.process("email/quiz-result", context);
            
            MimeMessageHelper helper = new MimeMessageHelper(mailSender.createMimeMessage(), true);
            helper.setTo(to);
            helper.setSubject("Your Quiz Results");
            helper.setText(emailContent, true);
            
            mailSender.send(helper.getMimeMessage());
        } catch (Exception e) {
            // Log error and handle exception
        }
    }
}
