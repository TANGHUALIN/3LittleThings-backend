package items;

import java.util.List;

public class Diary {
	private Integer did;
	private Integer uid;
	private String diaryDate;
	private List<DiaryEntry> diaryEntry;
	private boolean favoriteState;

	public Diary() {
	}
	public Integer getDid() {
		return did;
	}
	public void setDid(Integer did) {
		this.did = did;
	}
	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public String getDiaryDate() {
		return diaryDate;
	}

	public void setDiaryDate(String diaryDate) {
		this.diaryDate = diaryDate;
	}

	public List<DiaryEntry> getDiaryEntry() {
		return diaryEntry;
	}

	public void setDiaryEntry(List<DiaryEntry> diaryEntry) {
		this.diaryEntry = diaryEntry;
	}

	public boolean isFavoriteState() {
		return favoriteState;
	}

	public void setFavoriteState(boolean favoriteState) {
		this.favoriteState = favoriteState;
	}
}
