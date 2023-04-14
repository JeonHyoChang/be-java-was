package model;

public class PageStyleInfo {
    private final String contentType;
    private final String pathName;

    public PageStyleInfo(String contentType, String pathName) {
        this.contentType = contentType;
        this.pathName = pathName;
    }

    public String getContentType() {
        return contentType;
    }

    public String getPathName() {
        return pathName;
    }
}
