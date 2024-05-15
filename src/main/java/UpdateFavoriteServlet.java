import jdbc.DAO.DiaryDAOImpl;
import json.JsonResponseUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UpdateFavoriteServlet extends HttpServlet {
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer did = Integer.parseInt(req.getParameter("did"));
        boolean favoriteState = Boolean.parseBoolean(req.getParameter("favoriteState"));
        DiaryDAOImpl diaryDAOImpl = new DiaryDAOImpl();
        if (diaryDAOImpl.updateDiaryFavoriteState(did, favoriteState)) {
            JsonResponseUtils.writeJsonResponseWithMessage(resp, 200, "update succeed");
        } else {
            JsonResponseUtils.writeJsonResponseWithErrorMessage(resp, 500, "internal error");
        }


    }
}