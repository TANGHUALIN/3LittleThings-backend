package gmailService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import org.apache.commons.codec.binary.Base64;

import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Properties;


/* class to demonstrate use of Gmail list labels API */
public class EmailService {
    /**
     * Application name.
     */
    private static final String APPLICATION_NAME = "3LittleThings";
    /**
     * Global instance of the JSON factory.
     */
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    /**
     * Directory to store authorization tokens for this application.
     */
    private static final String TOKENS_DIRECTORY_PATH = "YOUR PATH TO TOKENS";

    private static String CLIENT_ID;

    private static String CLIENT_SECRET;

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */

    private static final String CREDENTIALS_FILE_PATH = "/google_cred.json";
    private static final NetHttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static final String SCOPE = "https://www.googleapis.com/auth/gmail.send";
    private static GoogleClientSecrets clientSecrets;

    static {
        ObjectMapper objectMapper = new ObjectMapper();

        try (InputStream in = EmailService.class.getResourceAsStream(CREDENTIALS_FILE_PATH)) {

            JsonNode jsonNode = objectMapper.readTree(in);

            JsonNode webNode = jsonNode.path("web");
            CLIENT_ID = webNode.path("client_id").asText();
            CLIENT_SECRET = webNode.path("client_secret").asText();
            InputStream in1 = EmailService.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
            clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in1));
            in1.close();
            if (clientSecrets == null) {
                throw new NullPointerException("Failed to load client secrets.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * Creates an authorized Credential object.
     *
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT)
            throws IOException {
        // Load client secrets.


        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, Collections.singletonList(SCOPE))
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
        //returns an authorized Credential object.
        return credential;
    }


    public static void sendVerificationEmail(String toEmail,String subject, String body) throws Exception {

        try (InputStreamReader clientSecretsReader = new InputStreamReader(EmailService.class.getResourceAsStream("/credentials.json"));
             ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {
            Credential credential = getCredentials(HTTP_TRANSPORT);
            Gmail service = new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                    .setApplicationName(APPLICATION_NAME)
                    .build();


            // Encode as MIME message
            Properties props = new Properties();
            Session session = Session.getDefaultInstance(props, null);
            MimeMessage email = new MimeMessage(session);
            email.setFrom(new InternetAddress("3littlethingsdiary@gmail.com"));
            email.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(toEmail));
            email.setSubject(subject);
            email.setText(body);


            // Encode and wrap the MIME message into a Gmail message
            email.writeTo(buffer);
            byte[] rawMessageBytes = buffer.toByteArray();
            String encodedEmail = Base64.encodeBase64URLSafeString(rawMessageBytes);

            // Send the email
            Message message = new Message();
            message.setRaw(encodedEmail);

            try {
                // Create and send the message
                service.users().messages().send("me", message).execute();
                // Log success or any additional information
                System.out.println("Verification email sent successfully.");
            } catch (Exception e) {
                // Log and handle any exceptions that may occur during email sending
                System.err.println("Error sending verification email: " + e.getMessage());
                throw e; // Re-throw the exception if needed
            }
        }
    }






}