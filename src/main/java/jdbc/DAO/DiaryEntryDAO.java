package jdbc.DAO;

import items.DiaryEntry;

public interface DiaryEntryDAO {
    boolean deleteDiaryEntry(Integer eid);

    Integer insertDiaryEntryReturnPK(Integer did, String entry);

    boolean updateDiaryEntry(Integer eid, String entry);

    DiaryEntry queryDiaryEntry(Integer eid);


}
