package webserver.http;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {

    private String method;
    private String url;
    private String version;
    private String path;

    private Map<String, String> queryMap = new HashMap<>();

    private final Map<String, String> headers = new HashMap<>();

    private HttpRequest(InputStream in) throws Exception {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        String requestLine = bufferedReader.readLine();

        parseRequestLine(requestLine);
        setQueryMap();

        parseHeader(bufferedReader);
    }

    private void parseRequestLine(String requestLine) {
        String[] requestLineToken = requestLine.split(" ");

        method = requestLineToken[0];
        url = requestLineToken[1];
        version = requestLineToken[2];
    }

    private void parseHeader(BufferedReader bufferedReader) throws Exception {
        String header = bufferedReader.readLine();
        while (header != null && !header.equals("")) {
            String[] headerToken = header.split(":");
            headers.put(headerToken[0], headerToken[1]);
            header = bufferedReader.readLine();
        }
    }

    private void setQueryMap() {
        if (!url.contains("?")) {
            path = url;
            return;
        }
        String queryLine = url.split("\\?")[1];
        path = url.split("\\?")[0];
        String[] queryList = queryLine.split("&");
        for (String query : queryList) {
            queryMap.put(query.split("=")[0], query.split("=")[1]);
        }
    }

    public static HttpRequest createRequest(InputStream in) throws Exception {
        return new HttpRequest(in);
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
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

    public Map<String, String> getQueryMap() {
        return queryMap;
    }
}
