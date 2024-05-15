package jdbc.DAO;

import items.Diary;

import java.util.List;

public interface DiaryDAO {

	List<Diary> queryAllDiary(Integer uid, int pageSize, int offset);


	int queryDiaryCount(int uid);


    Integer insertDiaryReturnPK(Integer uid);


    public Integer queryDiaryByDate(Integer uid, String date);

	boolean deleteDiary(Integer did);

	boolean updateDiaryFavoriteState(Integer did, boolean favoriteState);


}
