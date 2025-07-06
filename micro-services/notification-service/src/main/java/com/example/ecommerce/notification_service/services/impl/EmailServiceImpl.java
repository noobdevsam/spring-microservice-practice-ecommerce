package com.example.ecommerce.notification_service.services.impl;

import com.example.ecommerce.notification_service.models.ProductDTO;
import com.example.ecommerce.notification_service.services.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.ecommerce.notification_service.models.EmailTemplates.PAYMENT_CONFIRMATION;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Async
    @Override
    public void sendPaymentSuccessEmail(
            String destinationEmail,
            String customerName,
            BigDecimal amount,
            String orderReference
    ) throws MessagingException {

        var mimeMessage = mailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(
                mimeMessage,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name()
        );

        final var templateName = PAYMENT_CONFIRMATION.getTemplate();
        messageHelper.setSubject(PAYMENT_CONFIRMATION.getSubject());
        messageHelper.setFrom("moodxmail@gmail.com");

        Map<String, Object> variables = new HashMap<>();
        variables.put("customerName", customerName);
        variables.put("amount", amount);
        variables.put("orderReference", orderReference);

        var context = new Context();
        context.setVariables(variables);

        try {
            var htmlTemplate = templateEngine.process(templateName, context);
            messageHelper.setTo(destinationEmail);
            messageHelper.setText(htmlTemplate, true);
            log.info("INFO - Sending mail to email: {} with template {}", destinationEmail, templateName);
        } catch (MessagingException _) {
            log.warn("WARNING - Error while sending email to {} :", destinationEmail);
        }

    }

    @Async
    @Override
    public void sendOrderConfirmationEmail(
            String destinationEmail,
            String customerName,
            BigDecimal amount,
            String orderReference,
            List<ProductDTO> productDTOs
    ) throws MessagingException {

    }

}
