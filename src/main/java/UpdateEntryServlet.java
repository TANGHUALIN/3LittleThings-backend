import jdbc.DAO.DiaryEntryDAOImpl;
import json.JsonRequestUtils;
import json.JsonResponseUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class UpdateEntryServlet extends HttpServlet {
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        boolean allUpdated = true;
        try {
            Map<Integer, String> editedEntries = JsonRequestUtils.getValuesMapFromJsonRequest(req);
            System.out.println(editedEntries);
            for (Map.Entry<Integer, String> entry : editedEntries.entrySet()) {
                Integer eid = entry.getKey();
                String value = entry.getValue();
                System.out.println("eid:" + eid + "value:" + value);
                DiaryEntryDAOImpl diaryEntryDAOImpl = new DiaryEntryDAOImpl();
                if (!diaryEntryDAOImpl.updateDiaryEntry(eid, value)) {
                    allUpdated = false;
                }
            }
            if (allUpdated) {
                JsonResponseUtils.writeJsonResponseWithMessage(resp, 200, "All entries updated successfully");
            } else {
                JsonResponseUtils.writeJsonResponseWithMessage(resp, 500, "Some entries failed to update");
            }

        } catch (Exception e) {
            JsonResponseUtils.writeJsonResponseWithErrorMessage(resp, 500, "internal error");
        }


    }
}
