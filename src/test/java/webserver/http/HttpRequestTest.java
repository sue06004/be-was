package webserver.http;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class HttpRequestTest {

    private InputStream inputStream;

    @BeforeEach
    void setup() {
        String input = "GET /docs/index.html HTTP/1.1\n" +
                "Host: www.nowhere123.com\n" +
                "Accept: image/gif, image/jpeg, */*\n" +
                "Accept-Language: en-us\n" +
                "Accept-Encoding: gzip, deflate\n" +
                "User-Agent: Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1)\n";
        inputStream = new ByteArrayInputStream(input.getBytes());
    }

    @Test
    @DisplayName("Request method 테스트")
    void getMethod() throws Exception {
        HttpRequest request = HttpRequest.createRequest(inputStream);
        assertThat("GET").isEqualTo(request.getMethod());
    }

    @Test
    @DisplayName("Request url 테스트")
    void getUrl() throws Exception {
        HttpRequest request = HttpRequest.createRequest(inputStream);
        assertThat("/docs/index.html").isEqualTo(request.getUrl());
    }

    @Test
    @DisplayName("Request header 테스트")
    void getHeader() throws Exception {
        Map<String, String> headers = new HashMap<>();
        headers.put("Host", "www.nowhere123.com");
        headers.put("Accept", "image/gif, image/jpeg, */*");
        headers.put("Accept-Language", "en-us");
        headers.put("Accept-Encoding", "gzip, deflate");
        headers.put("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1)");

        HttpRequest request = HttpRequest.createRequest(inputStream);
        Map<String, String> requestHeaders = request.getHeaders();

        SoftAssertions a = new SoftAssertions();
        for (String headerName : requestHeaders.keySet()) {
            a.assertThat(headers.get(headerName)).isEqualTo(requestHeaders.get(headerName));
        }
        a.assertThat(headers.size()).isEqualTo(requestHeaders.size());
    }

}
