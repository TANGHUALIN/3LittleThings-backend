import items.Diary;
import jdbc.DAO.DiaryDAOImpl;
import json.JsonResponseUtils;
import securityUtils.VerificationTokenUtils;
import utils.PaginationUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


public class DiaryDisplayServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        final int PAGE_SIZE = 100;
        int page = Integer.parseInt(req.getParameter("page"));
        DiaryDAOImpl diaryDAOImpl = new DiaryDAOImpl();
        Integer uid = VerificationTokenUtils.getUidFromRequest(req);
        System.out.println("uid from token:" + uid);
        if (uid != -1) {
            try {
                System.out.println("uid:" + uid);
                int count = diaryDAOImpl.queryDiaryCount(uid);
                int pageNumber = PaginationUtils.getTotalPage(count, PAGE_SIZE);
                int offset = (page - 1) * PAGE_SIZE;
                boolean hasNextPage = page < pageNumber;
                List<Diary> diaryList = diaryDAOImpl.queryAllDiary(uid, PAGE_SIZE, offset);
                JsonResponseUtils.writeJsonResponseWithPageInfo(resp, page, pageNumber, hasNextPage, diaryList);
            } catch (Exception e) {
                e.printStackTrace();
                JsonResponseUtils.writeJsonResponseWithErrorMessage(resp, 500, "Internal Server Error");
            }
        } else {
            JsonResponseUtils.writeJsonResponseWithErrorMessage(resp, 401, "Unauthorized: Invalid Token");
        }

    }
}
