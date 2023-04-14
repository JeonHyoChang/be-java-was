package webserver;

import model.PageStyleInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class GetHandlerTest {

    @Test
    @DisplayName("GETHandler - doGet - 정상적인 URL 입력시 200 OK 응답 반환")
    public void testDoGetWithValidURL() throws IOException {
        // given
        String url = "/index.html";
        PageStyleInfo stylesheet = new PageStyleInfo("text/html", "src/main/resources/templates" + url);
        byte[] body = Files.readAllBytes(new File(stylesheet.getPathName()).toPath());
        String expectedOutput = "HTTP/1.1 200 OK \r\nContent-Type: text/html;charset=utf-8\r\nContent-Length: " + body.length + "\r\n\r\n";

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

        // when
        GetHandler.doGet(url, dataOutputStream);

        // then
        assertThat(outputStream.toString()).startsWith(expectedOutput);
    }
}
