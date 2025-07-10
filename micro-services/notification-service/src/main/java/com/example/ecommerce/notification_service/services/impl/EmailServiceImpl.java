package com.example.ecommerce.notification_service.services.impl;

import com.example.ecommerce.notification_service.models.ProductDTO;
import com.example.ecommerce.notification_service.services.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
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

import static com.example.ecommerce.notification_service.models.EmailTemplates.ORDER_CONFIRMATION;
import static com.example.ecommerce.notification_service.models.EmailTemplates.PAYMENT_CONFIRMATION;

/**
 * Implementation of the EmailService interface for sending emails.
 * This service uses Spring's JavaMailSender and Thymeleaf for email templating.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender; // JavaMailSender instance for sending emails
    private final SpringTemplateEngine templateEngine; // Thymeleaf template engine for processing email templates

    /**
     * Sends an email using the specified template and variables.
     *
     * @param destinationEmail The recipient's email address.
     * @param mimeMessage      The MimeMessage object for the email.
     * @param messageHelper    Helper for configuring the email message.
     * @param templateName     The name of the Thymeleaf template to use.
     * @param variables        A map of variables to populate the template.
     * @param templateEngine   The Thymeleaf template engine.
     * @param mailSender       The JavaMailSender instance.
     */
    private static void sendEmail(
            String destinationEmail,
            MimeMessage mimeMessage,
            MimeMessageHelper messageHelper,
            String templateName,
            Map<String, Object> variables,
            SpringTemplateEngine templateEngine,
            JavaMailSender mailSender
    ) {

        var context = new Context();
        context.setVariables(variables);

        try {
            var htmlTemplate = templateEngine.process(templateName, context);
            messageHelper.setTo(destinationEmail);
            messageHelper.setText(htmlTemplate, true);
            mailSender.send(mimeMessage);
            log.info("INFO - Sending mail to email: {} with template {}", destinationEmail, templateName);
        } catch (MessagingException _) {
            log.warn("WARNING - Error while sending email to {} :", destinationEmail);
        }

    }

    /**
     * Sends a payment success email asynchronously.
     *
     * @param destinationEmail The recipient's email address.
     * @param customerName     The name of the customer.
     * @param amount           The payment amount.
     * @param orderReference   The order reference number.
     * @throws MessagingException If an error occurs while sending the email.
     */
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

        sendEmail(
                destinationEmail,
                mimeMessage,
                messageHelper,
                templateName,
                variables,
                templateEngine,
                mailSender
        );

    }

    /**
     * Sends an order confirmation email asynchronously.
     *
     * @param destinationEmail The recipient's email address.
     * @param customerName     The name of the customer.
     * @param amount           The total order amount.
     * @param orderReference   The order reference number.
     * @param productDTOs      A list of products included in the order.
     * @throws MessagingException If an error occurs while sending the email.
     */
    @Async
    @Override
    public void sendOrderConfirmationEmail(
            String destinationEmail,
            String customerName,
            BigDecimal amount,
            String orderReference,
            List<ProductDTO> productDTOs
    ) throws MessagingException {
        var mimeMessage = mailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(
                mimeMessage,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name()
        );

        final var templateName = ORDER_CONFIRMATION.getTemplate();
        messageHelper.setSubject(ORDER_CONFIRMATION.getSubject());
        messageHelper.setFrom("moodxmail@gmail.com");

        Map<String, Object> variables = new HashMap<>();
        variables.put("customerName", customerName);
        variables.put("amount", amount);
        variables.put("orderReference", orderReference);
        variables.put("productDTOs", productDTOs);

        sendEmail(
                destinationEmail,
                mimeMessage,
                messageHelper,
                templateName,
                variables,
                templateEngine,
                mailSender
        );

    }

}
