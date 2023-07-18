package webserver.view;

import java.io.DataOutputStream;

public class View {

    private final DataOutputStream dos;
    private final byte[] body;

    public View(DataOutputStream dos, byte[] body) {
        this.dos = dos;
        this.body = body;
    }

    public void render() throws Exception{
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
