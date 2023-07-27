package webserver.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.StringTokenizer;

public class HttpRequest {

    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private String method;
    private String url;
    private String version;
    private String path;

    private Parameter parameter = new Parameter();
    private final RequestHeader headers = new RequestHeader();

    private HttpRequest(InputStream in) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        String requestLine = bufferedReader.readLine();

        parseRequestLine(requestLine);
        if (method.equals("GET") && url.contains("?")) {
            parseQueryParam();
        } else {
            path = url;
        }
        parseHeader(bufferedReader);
        if (method.equals("POST")) {
            parseBody(bufferedReader);
        }
    }

    private void parseRequestLine(String requestLine) {
        String[] requestLineToken = requestLine.split(" ");

        method = requestLineToken[0];
        url = requestLineToken[1];
        version = requestLineToken[2];
    }

    private void parseHeader(BufferedReader bufferedReader) throws IOException {
        String header = bufferedReader.readLine();

        while (header != null && !header.equals("")) {
            String headerKey = header.substring(0, header.indexOf(":"));
            String headerValue = header.substring(header.indexOf(":") + 1).trim();

            headers.put(headerKey, headerValue);
            header = bufferedReader.readLine();
        }
    }

    private void parseQueryParam() { // method가 get일 때 parameter 파싱
        String queryLine = url.split("\\?")[1];
        String[] queryList = queryLine.split("&");

        for (String query : queryList) {
            parameter.put(query.split("=")[0], query.split("=")[1]);
        }

        path = url.split("\\?")[0];
    }

    private void parseBody(BufferedReader bufferedReader) throws IOException { // method가 post일 때 body 파싱

        String body = getBodyString(bufferedReader);
        String[] bodyToken = body.split("&");

        for (String query : bodyToken) {
            String[] queryToken = query.split("=");
            parameter.put(queryToken[0], URLDecoder.decode(queryToken[1],"UTF-8"));
        }
    }

    private String getBodyString(BufferedReader bufferedReader) throws IOException {
        int bodyLength = Integer.parseInt(headers.get("Content-Length"));
        char[] bodyBuffer = new char[bodyLength];
        bufferedReader.read(bodyBuffer, 0, bodyLength);
        String body = String.valueOf(bodyBuffer);
        return body;
    }


    public String getSessionId() {
        String cookies = headers.get("Cookie");
        String sessionId = null;
        if (cookies != null) {
            sessionId = getSessionFromCookie(cookies);
        }
        return sessionId;
    }

    private String getSessionFromCookie(String cookies) {
        StringTokenizer cookieToken = new StringTokenizer(cookies, "=");
        while (cookieToken.hasMoreTokens()) {
            String cookie = cookieToken.nextToken();
            if (cookie.equals("sid")) {
                return cookieToken.nextToken();
            }
        }
        return null;
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

    public RequestHeader getHeaders() {
        return headers;
    }

    public Parameter getParam() {
        return parameter;
    }
}
