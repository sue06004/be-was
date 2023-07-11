package util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RequestHeaderUtilTest {

    @Test
    @DisplayName("RequestHeader에서 Path 분리하기")
    void getRequestPath(){
        String line = "GET /index.html HTTP/1.1";
        assertEquals("/index.html", HttpRequestUtils.getUrl(line));
    }
}