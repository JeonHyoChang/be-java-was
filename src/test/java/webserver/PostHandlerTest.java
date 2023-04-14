package webserver;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class PostHandlerTest {
    private PostHandler postHandler;

    @BeforeEach
    void setup() {
        String requestBody = "userId=john123&password=password123&name=John+Doe&email=john%40example.com";
        postHandler = new PostHandler(requestBody);
    }

    @Test
    @DisplayName("유저 생성을 위해 user/create URL로 POST 요청을 보내면 인덱스 페이지로 리다이렉트 되는지 확인하는 테스트")
    public void testDoPost() throws IOException {
        // given
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

        String expectedOutput = "HTTP/1.1 302 Found \r\nLocation: /index.html\r\n\r\n";

        // when
        postHandler.doPost("/user/create", dataOutputStream);

        // then
        assertThat(outputStream.toString()).isEqualTo(expectedOutput);
    }
}