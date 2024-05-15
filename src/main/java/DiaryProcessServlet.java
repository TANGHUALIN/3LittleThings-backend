import items.DiaryEntry;
import jdbc.DAO.DiaryDAOImpl;
import jdbc.DAO.DiaryEntryDAOImpl;
import json.JsonRequestUtils;
import json.JsonResponseUtils;
import securityUtils.VerificationTokenUtils;
import utils.DateUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DiaryProcessServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        Integer uid = VerificationTokenUtils.getUidFromRequest(req);
        if (uid != -1) {
            try {
                handleDiaryProcess(req, resp, uid);
            } catch (Exception e) {
                e.printStackTrace();
                JsonResponseUtils.writeJsonResponseWithErrorMessage(resp, 500, "Internal Server Error");
            }
        } else {
            JsonResponseUtils.writeJsonResponseWithErrorMessage(resp, 401, "Unauthorized: Invalid Token");
        }
    }


    private void handleDiaryProcess(HttpServletRequest req, HttpServletResponse resp, Integer uid) throws IOException {
        DiaryDAOImpl diaryDAOImpl = new DiaryDAOImpl();
        Integer did = diaryDAOImpl.queryDiaryByDate(uid, DateUtils.getTodayDate());
        DiaryEntryDAOImpl diaryEntryDAOImpl = new DiaryEntryDAOImpl();
        String entryContent = JsonRequestUtils.getValueFromJsonRequest(req, "entryContent");
        Integer eid;
        if (did == -1) {
            did = diaryDAOImpl.insertDiaryReturnPK(uid);
        }
        eid = diaryEntryDAOImpl.insertDiaryEntryReturnPK(did, entryContent);
        DiaryEntry diaryEntry = diaryEntryDAOImpl.queryDiaryEntry(eid);
        JsonResponseUtils.writeJsonResponseWithObject(resp, diaryEntry);
    }

}
