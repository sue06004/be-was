package webserver.http;

import java.io.DataOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.nio.file.Files;

public class HttpResponse {

    private static final String templatesDirectoryPath = "src/main/resources/templates";
    private final byte[] body;
    private final DataOutputStream dos;

    private HttpResponse(OutputStream out, String url) throws Exception {
        dos = new DataOutputStream(out);
        body = Files.readAllBytes(new File(templatesDirectoryPath + url).toPath());
    }

    public static HttpResponse createResponse(OutputStream out, String url) throws Exception {
        return new HttpResponse(out, url);
    }

    public void send() throws Exception {
        response200Header();
        responseBody();
    }

    private void response200Header() throws Exception {
        dos.writeBytes("HTTP/1.1 200 OK \r\n");
        dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
        dos.writeBytes("Content-Length: " + body.length + "\r\n");
        dos.writeBytes("\r\n");
    }

    private void responseBody() throws Exception {
        dos.write(body, 0, body.length);
        dos.flush();

    }
}
