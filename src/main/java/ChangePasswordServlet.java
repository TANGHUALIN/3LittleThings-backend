import jdbc.DAO.UserDAOImpl;
import json.JsonRequestUtils;
import json.JsonResponseUtils;
import securityUtils.PasswordUtils;
import securityUtils.VerificationTokenUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class ChangePasswordServlet extends HttpServlet {
    @Override

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        System.out.println("change password servlet");
        Integer uid = VerificationTokenUtils.getUidFromRequest(req);
        System.out.println("uid in token" + uid);
        UserDAOImpl userDAOImpl = new UserDAOImpl();
        String[] valueName = {"email", "password"};
        Map<String, String> valuesMap = JsonRequestUtils.getValuesMapFromJsonRequest(req, valueName);
        String email = valuesMap.get("email");
        Integer uidInDB = userDAOImpl.queryUidByEmail(email);
        System.out.println("uidIndb" + uidInDB);

        if (uid != -1 && uidInDB != -1) {
            try {
                if (uid.equals(uidInDB)) {
                    String password = valuesMap.get("password");
                    String hashedPassword = PasswordUtils.hashPassword(password);
                    if (userDAOImpl.updateUserPassword(hashedPassword, email)) {
                        JsonResponseUtils.writeJsonResponseWithMessage(res, 202, "change password succeed");
                    } else {
                        JsonResponseUtils.writeJsonResponseWithErrorMessage(res, 500, "internal error");
                    }
                } else {
                    JsonResponseUtils.writeJsonResponseWithErrorMessage(res, 404, "email is wrong");
                }
            } catch (Exception e) {
                e.printStackTrace();
                JsonResponseUtils.writeJsonResponseWithErrorMessage(res, 500, "Internal Server Error");
            }
        } else {
            JsonResponseUtils.writeJsonResponseWithErrorMessage(res, 401, "Unauthorized: Invalid Token");
        }

    }
}
