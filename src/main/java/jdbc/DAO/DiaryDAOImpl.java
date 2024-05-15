package jdbc.DAO;

import items.Diary;
import items.DiaryEntry;
import jdbc.DBUtils;
import utils.DateUtils;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiaryDAOImpl implements DiaryDAO {
    /**
     * ユーザによって、ユーザのすべての日記を問い合わせ
     *
     * @param uid 　ユーザID
     * @return 日記リスト
     */
    @Override
    public List<Diary> queryAllDiary(Integer uid, int pageSize, int offset) {
        return queryDiaryList("uid=? order by diary_date desc LIMIT ? OFFSET ?", uid, pageSize, offset);
    }


    @Override
    public Integer insertDiaryReturnPK(Integer uid) {
        String sql = "insert into diary(uid) values (?)";
        return DBUtils.insertAndGetPrimaryKey(sql, uid);
    }



    @Override
    public boolean deleteDiary(Integer did) {
        String sql = "delete from diary where did=?";
        return DBUtils.update(sql, did) != -1;
    }



    @Override
    public boolean updateDiaryFavoriteState(Integer did, boolean favoriteState) {
        return updateDiaryColumn(did, "favorite_state = ?", favoriteState);
    }


    @Override
    public Integer queryDiaryByDate(Integer uid, String date) {
        String sql = "SELECT did FROM diary WHERE uid=? AND DATE(diary_date) = ?";
        List<Map<String, Object>> table = DBUtils.queryMap(sql, uid, date);
        if (table != null && !table.isEmpty()) {
            return (Integer) table.get(0).get("did");
        } else {
            return -1;
        }
    }

    @Override
    public int queryDiaryCount(int uid) {
        String sql = "SELECT COUNT(*) AS total_count FROM (SELECT diary.did, diary.uid, diary.diary_date, diary_entry.content, diary_entry.eid, diary_entry.entry_date, diary.favorite_state FROM diary LEFT JOIN diary_entry ON diary.did = diary_entry.did WHERE uid = ?) AS result";
        List<List<Object>> list = DBUtils.queryList(sql, uid);
        if (!list.isEmpty()) {
            return Integer.parseInt(list.get(0).get(0).toString());
        } else {
            return 0;
        }
    }
    private boolean updateDiaryColumn(Integer did, String setClause, Object value) {
        String sql = "update diary set " + setClause + " where did = ?";
        return DBUtils.update(sql, value, did) != -1;
    }

    private List<Diary> queryDiaryList(String setClause, Object... value) {
        String sql = "SELECT diary.did,diary.uid, diary.diary_date, diary_entry.content, diary_entry.eid,diary_entry.entry_date,diary.favorite_state FROM diary LEFT JOIN diary_entry ON diary.did = diary_entry.did WHERE " + setClause;
        List<Map<String, Object>> table = DBUtils.queryMap(sql, value);
        Map<Integer, Diary> diaryMap = new HashMap<>();
        if (table != null && !table.isEmpty()) {
            for (Map<String, Object> row : table) {
                Integer did = (Integer) row.get("did");
                Diary diary = diaryMap.get(did);
                if (diary == null) {
                    diary = new Diary();
                    diary.setUid((Integer) row.get("uid"));
                    diary.setDid(did);
                    diary.setDiaryDate(DateUtils.changeDateToString((Date) row.get("diary_date")));
                    diary.setFavoriteState((boolean) row.get("favorite_state"));
                    diary.setDiaryEntry(new ArrayList<>());
                    diaryMap.put(did, diary);
                }
                if (row.get("content") != null) {
                    DiaryEntry diaryEntry = new DiaryEntry();
                    diaryEntry.setEntryContent((String) row.get("content"));
                    diaryEntry.setEid((Integer) row.get("eid"));
                    diaryEntry.setDid((Integer) row.get("did"));
                    diaryEntry.setEntryDate(DateUtils.changeDateToString((Date) row.get("entry_date")));
                    diary.getDiaryEntry().add(diaryEntry);
                }
            }
            return new ArrayList<>(diaryMap.values());
        } else {
            return null;
        }

    }
}
