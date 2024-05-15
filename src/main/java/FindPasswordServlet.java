import gmailService.EmailService;
import jdbc.DAO.UserDAOImpl;
import json.JsonRequestUtils;
import json.JsonResponseUtils;
import securityUtils.KeyUtils;
import securityUtils.VerificationTokenUtils;
import utils.EmailUtils;
import utils.LanguageUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Key;

public class FindPasswordServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        UserDAOImpl userDAOImp = new UserDAOImpl();
        String toEmail = JsonRequestUtils.getValueFromJsonRequest(req, "email");
        if (!userDAOImp.queryUserEmailExist(toEmail)) {
            JsonResponseUtils.writeJsonResponseWithErrorMessage(res, 404, "user doesnt exist");
        } else {
            try {

                final Key secretKey = KeyUtils.getKeyForJwtToken();


                String jwtToken = VerificationTokenUtils.generateJwtToken(toEmail, 360000, secretKey);


                String lang = LanguageUtils.getUserPreferredLanguage(req);
                System.out.println("language:" + lang);
                String subject = EmailUtils.getResetEmailSubject(lang);

                String body = EmailUtils.getResetEmailBody(lang);
                String link = VerificationTokenUtils.createVerificationLink("loading2", jwtToken);

                String bodyWithLink = body.replace("[Link]", link);
                EmailService.sendVerificationEmail(toEmail, subject, bodyWithLink);


                userDAOImp.insertTempUser(jwtToken, toEmail, "findPassword");
                JsonResponseUtils.writeJsonResponseWithMessage(res, 202, "email send successful. Please check your email to verify your account.");
                System.out.println(" Please check your email to verify your account.");
            } catch (Exception e) {
                JsonResponseUtils.writeJsonResponseWithErrorMessage(res, 400, "invalid mail address.");
                System.out.println("invalid email");
            }
        }
    }
}


