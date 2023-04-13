package webserver;

import db.Database;
import db.SessionDB;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.HttpResponseUtils;
import util.StylesheetUtils;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;
import java.util.UUID;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));

            // 모든 리퀘스트 출력 & 첫 라인 리턴
            String startLine = HttpRequestUtils.getStartLine(br);
            if (startLine == null) {
                return;
            }

            String method = HttpRequestUtils.getMethod(startLine); // method 설정
            String url = HttpRequestUtils.getUrl(startLine); // path 설정
            Map<String, String> headers = HttpRequestUtils.getRequestHeaders(br);

            // GET: stylesheet
            String contentType = StylesheetUtils.getContentType(url);
            String pathName = StylesheetUtils.getPathName(url);

            if (method.equals("GET")) {
                // GET: join
                if (url.startsWith("/user/create?")) {
                    User user = HttpRequestUtils.joinWithGET(url);
                    Database.addUser(user);
                    logger.debug("User: {}", user);

                    url = "/index.html";
                }
            }

            // POST: join
            DataOutputStream dos = new DataOutputStream(out);
            if (method.equals("POST")) {
                if (url.startsWith("/user/login")) {
                    int contentLength = Integer.parseInt(headers.get("Content-Length"));
                    String requestBody = HttpRequestUtils.getRequestBody(br, contentLength);
                    logger.debug("requestBody-login: {}", requestBody);

                    // id & password 분리
                    String[] loginData = requestBody.split("&");
                    String loginId = loginData[0].split("=")[1];
                    String loginPassword = loginData[1].split("=")[1];

                    User loginUser = Database.findUserById(loginId);
                    if (loginUser != null && loginUser.getPassword().equals(loginPassword)) {
                        // 로그인 성공
                        UUID sessionId = SessionDB.addSession(loginUser);
                        logger.debug("sessionId: {}", sessionId);
                        System.out.println(pathName);
                        HttpResponseUtils.response302WithCookie(dos, sessionId);
                    } else {
                        // 로그인 실패
                        HttpResponseUtils.response302Failed(dos);
                    }
                }
                if (url.startsWith("/user/create")) {
                    int contentLength = Integer.parseInt(headers.get("Content-Length"));
                    String requestBody = HttpRequestUtils.getRequestBody(br, contentLength);
                    logger.debug("requestBody: {}", requestBody);

                    User user = HttpRequestUtils.joinWithPOST(requestBody);
                    Database.addUser(user);
                    logger.debug("User: {}", user);

                    HttpResponseUtils.response302Header(dos);
                }
            } else {
                byte[] body = Files.readAllBytes(new File(pathName + url).toPath());
                HttpResponseUtils.response200Header(dos, body.length, contentType);
                HttpResponseUtils.responseBody(dos, body);
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
