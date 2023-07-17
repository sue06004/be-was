package webserver.http;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {

    private final String method;
    private final String url;
    private final String version;

    private final Map<String, String> headers = new HashMap<>();

    private HttpRequest(InputStream in) throws Exception {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        String requestLine = bufferedReader.readLine();
        String[] requestLineToken = requestLine.split(" ");
        method = requestLineToken[0];
        url = requestLineToken[1];
        version = requestLineToken[2];

        String header = bufferedReader.readLine();
        while (header != null && !header.equals("")) { //테스트 할 때 null이 들어감 why?
            String[] headerToken = header.split(":");
            headers.put(headerToken[0], headerToken[1]);
            header = bufferedReader.readLine();
        }
    }

    public static HttpRequest createRequest(InputStream in) throws Exception {
        return new HttpRequest(in);
    }

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public String getVersion() {
        return version;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }
}
