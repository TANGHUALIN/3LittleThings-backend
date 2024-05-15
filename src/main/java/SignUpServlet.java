import gmailService.EmailService;
import jdbc.DAO.UserDAOImpl;
import json.JsonRequestUtils;
import json.JsonResponseUtils;
import securityUtils.KeyUtils;
import securityUtils.PasswordUtils;
import securityUtils.VerificationTokenUtils;
import utils.EmailUtils;
import utils.LanguageUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Key;
import java.util.Map;


public class SignUpServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String[] valueName = {"email", "password"};
        Map<String, String> valuesMap = JsonRequestUtils.getValuesMapFromJsonRequest(req, valueName);
        String toEmail = valuesMap.get("email");
        String password = valuesMap.get("password");
        String hashedPassword = PasswordUtils.hashPassword(password);
        UserDAOImpl userDAOImp = new UserDAOImpl();

        if (userDAOImp.queryUserEmailExist(toEmail)) {
            JsonResponseUtils.writeJsonResponseWithErrorMessage(res, 409, "user already exists");
        } else {
            final Key secretKey = KeyUtils.getKeyForJwtToken();
            String jwtToken = VerificationTokenUtils.generateJwtToken(toEmail, 360000, secretKey);

            try {
                if (userDAOImp.queryTempUserEmailExist(toEmail)) {
                    JsonResponseUtils.writeJsonResponseWithErrorMessage(res, 429, "Try to use the same email to sign up within 5 minutes.Please check your mail or sign up after 5min.");
                    System.out.println("sign up twice within 5min");
                } else {
                    String lang = LanguageUtils.getUserPreferredLanguage(req);
                    System.out.println("language:" + lang);
                    String subject = EmailUtils.getSignupEmailSubject(lang);

                    String body = EmailUtils.getSignupEmailBody(lang);
                    String link = VerificationTokenUtils.createVerificationLink("loading", jwtToken);

                    String bodyWithLink = body.replace("[Link]", link);
                    EmailService.sendVerificationEmail(toEmail, subject, bodyWithLink);

                    System.out.println("subject:" + subject);
                    System.out.println("body:" + bodyWithLink);
                    userDAOImp.insertTempUser(jwtToken, toEmail, hashedPassword);
                    JsonResponseUtils.writeJsonResponseWithMessage(res, 202, "Registration successful. Please check your email to verify your account.");
                    System.out.println("Registration successful. Please check your email to verify your account.");
                }
            } catch (Exception e) {
                JsonResponseUtils.writeJsonResponseWithErrorMessage(res, 400, "invalid mail address.");
                System.out.println("invalid email");
            }
        }
    }

}