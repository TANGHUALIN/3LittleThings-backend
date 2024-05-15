import jdbc.DAO.DiaryDAOImpl;
import json.JsonResponseUtils;
import securityUtils.VerificationTokenUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DiaryCountServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        Integer uid = VerificationTokenUtils.getUidFromRequest(req);
        if (uid != -1) {
            try {
                DiaryDAOImpl diaryDAOImpl = new DiaryDAOImpl();
                System.out.println("uid:" + uid);
                int count = diaryDAOImpl.queryDiaryCount(uid);
                JsonResponseUtils.writeJsonResponseWithObject(resp, count);
            } catch (Exception e) {
                e.printStackTrace();
                JsonResponseUtils.writeJsonResponseWithErrorMessage(resp, 500, "Internal Server Error");
            }
        } else {
            JsonResponseUtils.writeJsonResponseWithErrorMessage(resp, 401, "Unauthorized: Invalid Token");
        }
    }
}
