package webserver;

import model.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            HttpRequestUtils reqUtils = new HttpRequestUtils();

            // 첫 라인 리턴
            String startLine = reqUtils.getStartLine(br);
            logger.debug("startLine: {}", startLine);

            // RequestInfo
            HttpRequest reqInfo = new HttpRequest(reqUtils.getMethod(startLine), // method 설정
                    reqUtils.getUrl(startLine), // path 설정
                    reqUtils.getRequestHeaders(br)); // header 정보 => Map<String, String>
            DataOutputStream dos = new DataOutputStream(out);

            // GETHandler
            if (reqInfo.equalsMethod("GET")) {
                GetHandler.doGet(reqInfo.getUrl(), dos);
            }

            // POSTHandler
            if (reqInfo.equalsMethod("POST")) {
                int contentLength = Integer.parseInt(reqInfo.getHeader("Content-Length"));
                PostHandler postHandler = new PostHandler(reqUtils.getRequestBody(br, contentLength));

                postHandler.doPost(reqInfo.getUrl(), dos);
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
