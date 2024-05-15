import jdbc.DAO.DiaryDAOImpl;
import json.JsonResponseUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DeleteDiaryServlet extends HttpServlet {
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        Integer did = Integer.parseInt(req.getParameter("did"));
        DiaryDAOImpl diaryDAOimpl = new DiaryDAOImpl();
        if (diaryDAOimpl.deleteDiary(did)) {
            JsonResponseUtils.writeJsonResponseWithMessage(res, 200, "delete succeed");
        }
    }
}

