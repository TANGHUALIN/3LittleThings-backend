package utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

public class EmailUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final JsonNode emailData;

    static {
        try (InputStream inputStream = EmailUtils.class.getResourceAsStream("/email.json")) {
            emailData = objectMapper.readTree(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error loading email.json", e);
        }
    }

    public static String getSignupEmailSubject(String language) {
        return getEmailProperty("signup_email", "subject", language);
    }

    public static String getSignupEmailBody(String language) {
        return getEmailProperty("signup_email", "body", language);
    }

    public static String getResetEmailSubject(String language) {
        return getEmailProperty("reset_password_email", "subject", language);
    }

    public static String getResetEmailBody(String language) {
        return getEmailProperty("reset_password_email", "body", language);
    }

    private static String getEmailProperty(String emailType, String property, String language) {
        JsonNode emailNode = emailData.path(emailType);
        if (emailNode != null && emailNode.has(property)) {
            JsonNode languageNode = emailNode.path(property).path(language);
            if (languageNode != null && languageNode.isTextual()) {
                return languageNode.textValue();
            }
        }
        return null; // or return a default value
    }
}
