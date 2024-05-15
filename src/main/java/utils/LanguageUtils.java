package utils;

import javax.servlet.http.HttpServletRequest;

public class LanguageUtils {
    public static String getUserPreferredLanguage(HttpServletRequest request) {
        String acceptLanguage = request.getHeader("Accept-Language");
        System.out.println("acceptLang" + acceptLanguage);
        if (acceptLanguage != null && !acceptLanguage.isEmpty()) {
            String preferredLanguage = acceptLanguage.substring(0, 2);
            ;
            System.out.println("language:" + preferredLanguage);
            return preferredLanguage;
        }
        // 默认返回英语
        return "en";
    }
}
