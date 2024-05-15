package items;

public class DiaryEntry {
    private Integer eid;

    private String entryContent;

    private String entryDate;
    private Integer did;

    public DiaryEntry() {

    }

    public DiaryEntry(Integer eid, Integer did, String entryContent, String entryDate) {
        this.eid = eid;
        this.did = did;
        this.entryContent = entryContent;
        this.entryDate = entryDate;
    }

    public Integer getEid() {
        return eid;
    }

    public void setEid(Integer eid) {
        this.eid = eid;
    }

    public String getEntryContent() {
        return entryContent;
    }

    public void setEntryContent(String entryContent) {
        this.entryContent = entryContent;
    }

    public String getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
    }

    public Integer getDid() {
        return did;
    }

    public void setDid(Integer did) {
        this.did = did;
    }
}
