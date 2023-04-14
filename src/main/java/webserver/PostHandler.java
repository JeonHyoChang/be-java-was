package webserver;

import db.Database;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ParseQueryUtils;

import java.io.DataOutputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class PostHandler {
    private final String requestBody;
    private static final Logger logger = LoggerFactory.getLogger(PostHandler.class);

    public PostHandler(String requestBody) {
        this.requestBody = requestBody;
    }

    public void doPost(String url, DataOutputStream dos) {
        if (url.startsWith("/user/create")) {
            createPost();

            ResponseHandler.response302Header(dos);
        }
    }

    private void createPost() {
        logger.debug("requestBody: {}", requestBody);

        User user = createUser();
        Database.addUser(user);
        logger.debug("User: {}", user);
    }

    private User createUser() {
        Map<String, String> params = ParseQueryUtils.parseQueryString(requestBody);
        return new User(decoding(params.get("userId")), decoding(params.get("password"))
                , decoding(params.get("name")), decoding(params.get("email")));
    }

    private String decoding(String value) {
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }
}
