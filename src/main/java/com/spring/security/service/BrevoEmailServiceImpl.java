package com.spring.security.service;

import com.spring.security.exceptions.EmailServiceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service class for sending emails using the Brevo API.
 * Brevo (formerly Sendinblue) is an email marketing service provider.
 * This service uses RestTemplate to make HTTP requests to the Brevo API.
 * You can configure the API key and sender email in your application properties.
 */
@Service
@Profile("prod")
@Primary
public class BrevoEmailServiceImpl implements EmailService {

    private final RestTemplate restTemplate;

    // API key for authenticating with the Brevo API, injected from application properties.
    @Value("${brevo-api-key}")
    private String apiKey;

    // Sender email address, injected from application properties.
    @Value("${brevo-sender-email}")
    private String senderEmail;

    /**
     * Constructor to initialize the BrevoEmailService with a RestTemplate bean.
     *
     * @param restTemplate RestTemplate bean for making HTTP requests.
     */
    public BrevoEmailServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Sends an HTML email with the specified subject and HTML content to the given recipient.
     *
     * @param toEmail     the recipient's email address
     * @param subject     the subject of the email
     * @param htmlContent the HTML content of the email
     */
    @Override
    public void sendHtmlEmail(String toEmail, String subject, String htmlContent) throws EmailServiceException {

        try {
            // Set up HTTP headers with content type and authorization.
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            // Create the request body with sender, recipient, subject, and content.
            Map<String, Object> body = new HashMap<>();
            body.put("sender", Map.of( "email", senderEmail));
            body.put("to", List.of(Map.of("email", toEmail)));
            body.put("subject", subject);
            body.put("htmlContent", htmlContent);

            // Wrap the body and headers into an HttpEntity.
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

            // Define the Brevo API endpoint for sending emails.
            String apiUrl = "https://api.brevo.com/v3/smtp/email";

            // Send the POST request and capture the response.
            ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, request, String.class);

            // Print the response body to the console.
            System.out.println("Response: " + response.getBody());
        } catch (Exception e) {
            throw new EmailServiceException("Failed to send email",e);
        }

    }
}
