package webserver.view;

import webserver.http.HttpResponse;

import java.io.DataOutputStream;

public class View {

    public static void render(DataOutputStream dos, HttpResponse response) throws Exception{
        byte[] body = response.getBody();
        responseHeader(dos, body,response);
        responseBody(dos, body);
    }

    private static void responseHeader(DataOutputStream dos, byte[] body, HttpResponse response) throws Exception {
        dos.writeBytes(response.getResponseHead(body.length));
    }

    private static void responseBody(DataOutputStream dos, byte[] body) throws Exception {
        dos.write(body, 0, body.length);
        dos.flush();
    }
}
