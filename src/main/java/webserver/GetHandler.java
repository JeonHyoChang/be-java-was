package webserver;

import model.PageStyleInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.StylesheetUtils;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class GetHandler {
    private static final Logger logger = LoggerFactory.getLogger(GetHandler.class);

    public static void doGet(String url, DataOutputStream dos) throws IOException {
        PageStyleInfo stylesheet = new PageStyleInfo(StylesheetUtils.getContentType(url),
                StylesheetUtils.getPathName(url));

        logger.debug("stylesheet: {}, {}", stylesheet.getContentType(), stylesheet.getPathName());

        byte[] body = Files.readAllBytes(new File(stylesheet.getPathName()).toPath());
        ResponseHandler.response200Header(dos, body.length, stylesheet.getContentType());
        ResponseHandler.responseBody(dos, body);
    }
}
