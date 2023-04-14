package util;

import model.ContentType;

public class StylesheetUtils {
    public static String getContentType(String url) {
        return ContentType.of(url).getContentType();
    }

    public static String getPathName(String url) {
        return ContentType.of(url).getPathName() + url;
    }
}
