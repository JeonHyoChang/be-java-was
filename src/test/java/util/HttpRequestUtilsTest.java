package util;

import model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class HttpRequestUtilsTest {
    @Test
    @DisplayName("getStartLine 메서드 테스트: HTTP 요청에서 첫 줄(Start Line)이 정확하게 추출되는지 검증")
    void getStartLine() {
        // given
        String request = "GET /index.html HTTP/1.1\n" +
                "Host: localhost:8080\n" +
                "Connection: keep-alive\n" +
                "Cache-Control: max-age=0\n" +
                "sec-ch-ua: \"Google Chrome\";v=\"111\", \"Not(A:Brand\";v=\"8\", \"Chromium\";v=\"111\"\n" +
                "sec-ch-ua-mobile: ?0\n" +
                "sec-ch-ua-platform: \"macOS\"\n" +
                "Upgrade-Insecure-Requests: 1\n" +
                "User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/111.0.0.0 Safari/537.36\n" +
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7\n" +
                "Sec-Fetch-Site: none\n" +
                "Sec-Fetch-Mode: navigate\n" +
                "Sec-Fetch-User: ?1\n" +
                "Sec-Fetch-Dest: document\n" +
                "Accept-Encoding: gzip, deflate, br\n" +
                "Accept-Language: ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7\n" +
                "Cookie: Idea-64d61216=7dd6c7cd-f4bb-484a-bbcf-757fe48c2684; JSESSIONID=2FFB53D49DA882FAF771D9E6B5D86BC7";
        BufferedReader br = new BufferedReader(new StringReader(request));

        String expectedStartLine = "GET /index.html HTTP/1.1";

        // when
        String param = HttpRequestUtils.getStartLine(br);

        // then
        assertThat(param).isEqualTo(expectedStartLine);
    }

    @Test
    @DisplayName("getMethod 메서드 테스트: HTTP 요청 라인에서 HTTP 메소드가 정확하게 추출되는지 검증")
    void getMethod() {
        // given
        String line = "GET /index.html HTTP/1.1";
        String expectedMethod = "GET";

        // when
        String param = HttpRequestUtils.getMethod(line);

        // then
        assertThat(param).isEqualTo(expectedMethod);
    }

    @Test
    @DisplayName("getUrl 메서드 테스트: HTTP 요청 라인에서 URL이 정확하게 추출되는지 검증")
    void getUrl() {
        // given
        String line = "GET /index.html HTTP/1.1";
        String expectedUrl = "/index.html";

        // when
        String param = HttpRequestUtils.getUrl(line);

        // then
        assertThat(param).isEqualTo(expectedUrl);
    }

    @Test
    @DisplayName("")
    void joinWithGET() {
        // given
        String joinUrl = "/user/create?userId=test&password=1234&name=테스터&email=test01%40naver.com";

        User expectedUser = new User("test", "1234", "테스터", "test01@naver.com");

        // when
        User userParam = HttpRequestUtils.joinWithGET(joinUrl);

        // then
        assertThat(userParam).usingRecursiveComparison().isEqualTo(expectedUser);
    }

    @Test
    @DisplayName("")
    void decoding() {
        // given
        String encoding01 = "%40";
        String encoding02 = "%EB%A7%8C%EC%A3%BC";

        String expectedDecoding01 = "@";
        String expectedDecoding02 = "만주";

        // when
        String decodingParam01 = HttpRequestUtils.decoding(encoding01);
        String decodingParam02 = HttpRequestUtils.decoding(encoding02);
        System.out.println(decodingParam02);

        // then
        assertThat(decodingParam01).isEqualTo(expectedDecoding01);
        assertThat(decodingParam02).isEqualTo(expectedDecoding02);
    }

    @Test
    @DisplayName("getRequestHeaders() 메소드에서 BufferedReader를 이용하여 요청 헤더 정보 추출하기")
    public void getRequestHeaders() throws Exception {
        // given
        String requestHeaders = "Connection: keep-alive\n" +
                "Content-Length: 81";
        BufferedReader br = new BufferedReader(new StringReader(requestHeaders));

        Map<String, String> expectedHeaders = new HashMap<>();
        expectedHeaders.put("Connection", "keep-alive");
        expectedHeaders.put("Content-Length", "81");

        // when
        Map<String, String> headersParam = HttpRequestUtils.getRequestHeaders(br);

        // then
        assertThat(headersParam).isEqualTo(expectedHeaders);
    }

    @Test
    @DisplayName("getRequestBody 메소드가 지정된 길이까지 요청 바디를 잘 반환하는지 테스트")
    public void getRequestBody() throws Exception {
        // given
        String requestBody = "userId=test&password=1234&name=%EC%95%84%ED%97%B9%ED%97%B9&email=1111%40gmail.com 이부분은 더미 데이터 부분 입니다. 지정된 숫자까지 읽을까요?";
        BufferedReader br = new BufferedReader(new StringReader(requestBody));
        int contentLength = 81;

        String expectedRequestBody = "userId=test&password=1234&name=%EC%95%84%ED%97%B9%ED%97%B9&email=1111%40gmail.com";

        // when
        String requestBodyParam = HttpRequestUtils.getRequestBody(br, contentLength);

        // then
        assertThat(requestBodyParam).isEqualTo(expectedRequestBody);
    }

    @Test
    @DisplayName("이부분채워주셈")
    public void joinWithPOST() throws Exception {
        // given
        String joinRequestBody = "userId=test&password=1234&name=테스터&email=test01%40naver.com";

        User expectedUser = new User("test", "1234", "테스터", "test01@naver.com");

        // when
        User userParam = HttpRequestUtils.joinWithPOST(joinRequestBody);

        // then
        assertThat(userParam).usingRecursiveComparison().isEqualTo(expectedUser);
    }
}