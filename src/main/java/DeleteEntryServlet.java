import jdbc.DAO.DiaryEntryDAOImpl;
import json.JsonRequestUtils;
import json.JsonResponseUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class DeleteEntryServlet extends HttpServlet {
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        boolean allDeleted = true;
        try {
            List<Integer> eidList = JsonRequestUtils.getListFromJsonRequest(req);
            for (Integer eid : eidList) {
                DiaryEntryDAOImpl diaryEntryDAOImpl = new DiaryEntryDAOImpl();
                if (!diaryEntryDAOImpl.deleteDiaryEntry(eid)) {
                    allDeleted = false;
                }
            }
            if (allDeleted) {
                JsonResponseUtils.writeJsonResponseWithMessage(res, 200, "All entries deleted successfully");
            } else {
                JsonResponseUtils.writeJsonResponseWithMessage(res, 500, "Some entries failed to delete");
            }

        } catch (Exception e) {
            JsonResponseUtils.writeJsonResponseWithErrorMessage(res, 500, "internal error");
        }

    }
}

