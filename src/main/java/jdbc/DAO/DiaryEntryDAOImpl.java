package jdbc.DAO;

import items.DiaryEntry;
import jdbc.DBUtils;
import utils.DateUtils;

import java.sql.Date;
import java.util.List;
import java.util.Map;

public class DiaryEntryDAOImpl implements DiaryEntryDAO {
    public boolean deleteDiaryEntry(Integer eid) {
        String sql = "delete from diary_entry where eid=?";
        return DBUtils.update(sql, eid) != -1;
    }

    public Integer insertDiaryEntryReturnPK(Integer did, String entry) {
        String sql = "insert into diary_entry(did,content) values (?,?)";
        return DBUtils.insertAndGetPrimaryKey(sql, did, entry);
    }

    public boolean updateDiaryEntry(Integer eid, String entry) {
        String sql = "update diary_entry set content=? where eid=?";
        return DBUtils.update(sql, entry, eid) != -1;
    }

    public DiaryEntry queryDiaryEntry(Integer eid) {
        String sql = "SELECT did,entry_date,content FROM diary_entry WHERE eid=?";
        List<Map<String, Object>> table = DBUtils.queryMap(sql, eid);
        if (table != null && !table.isEmpty()) {
            Map<String, Object> row = table.get(0);
            DiaryEntry diaryEntry = new DiaryEntry();
            diaryEntry.setEntryContent((String) row.get("content"));
            diaryEntry.setDid((Integer) row.get("did"));
            diaryEntry.setEid(eid);
            diaryEntry.setEntryDate(DateUtils.changeDateToString((Date) row.get("entry_date")));
            return diaryEntry;
        } else {
            return null;
        }
    }
}
