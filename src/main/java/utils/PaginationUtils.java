package utils;

public class PaginationUtils {
    public static int getTotalPage(int diaryCount, int pageSize) {
        return (int) Math.ceil((double) diaryCount / pageSize);
    }
}
