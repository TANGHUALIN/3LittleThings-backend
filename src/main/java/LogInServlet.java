import items.User;
import jdbc.DAO.UserDAOImpl;
import json.JsonRequestUtils;
import json.JsonResponseUtils;
import securityUtils.KeyUtils;
import securityUtils.PasswordUtils;
import securityUtils.VerificationTokenUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class LogInServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String tokenWithinLink = req.getParameter("token");
        System.out.println("token from react " + tokenWithinLink);
        System.out.println("temptoken" + tokenWithinLink);
        System.out.println(tokenWithinLink);
        UserDAOImpl userDAOImpl = new UserDAOImpl();
        User tempUser = userDAOImpl.queryTempUserByToken(tokenWithinLink);
        if (tempUser == null) {
            System.out.println("tempUser is null");
            res.sendRedirect("https://3littlethings.top");
        } else {
            String hashedPassword = tempUser.getHashedPassword();
            System.out.println("password:" + hashedPassword);
            String email = tempUser.getEmail();
            System.out.println("findPassword is true:" + hashedPassword.equals("findPassword"));
            if (hashedPassword.equals("findPassword") || userDAOImpl.insertUser(email, hashedPassword)) {
                Integer uid = userDAOImpl.queryUidByEmail(email);
                sendResponseWithToken(res, uid, "verify succeed");
            } else {
                res.sendRedirect("https://3littlethings.top");
            }

        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String[] valueName = {"email", "password"};
        Map<String, String> valuesMap = JsonRequestUtils.getValuesMapFromJsonRequest(req, valueName);
        String email = valuesMap.get("email");
        System.out.println(email);
        String password = valuesMap.get("password");
        System.out.println(password);
        UserDAOImpl userDAOImpl = new UserDAOImpl();
        User user = userDAOImpl.queryUserByEmail(email);
        String hashedPassword;
        Integer uid;
        if (user == null) {
            JsonResponseUtils.writeJsonResponseWithErrorMessage(res, 404, "havent signed up");
        } else {
            hashedPassword = user.getHashedPassword();
            if (PasswordUtils.verifyPassword(password, hashedPassword)) {
                uid = user.getUid();
                sendResponseWithToken(res, uid, "Login succeed");
            } else {
                JsonResponseUtils.writeJsonResponseWithErrorMessage(res, 400, "password is wrong");
            }
        }

    }

    private void sendResponseWithToken(HttpServletResponse res, Integer uid, String message) throws IOException {
        System.out.println("uid" + uid);
        String token = VerificationTokenUtils.generateJwtToken(uid.toString(), 5184000, KeyUtils.getKeyForJwtToken());
        res.setHeader("Access-Control-Expose-Headers", "Authorization");
        res.setHeader("Authorization", "Bearer " + token);
        System.out.println("header:" + res.getHeader("Authorization"));
        JsonResponseUtils.writeJsonResponseWithMessage(res, 200, message);
    }
}
