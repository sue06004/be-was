package webserver.http;

public class HttpResponse {

    private String stateCode;
    private String location;
    private String contentType;
    private String cookie = null;
    private byte[] body = new byte[0];

    private HttpResponse() {

    }

    public static HttpResponse createResponse() {
        return new HttpResponse();
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public byte[] getBody() {
        return body;
    }

    public String getResponseHead() {
        StringBuilder stringBuilder = new StringBuilder();

        if (stateCode.equals(HttpStateCode.REDIRECT)) {
            stringBuilder.append("HTTP/1.1 ").append(stateCode).append("\r\n");
            stringBuilder.append("Location: ").append(location).append("\r\n");
            stringBuilder.append("Content-Type: ").append(contentType).append("\r\n");
            stringBuilder.append("Content-Length: ").append(body.length).append("\r\n");
            if(cookie != null){
                stringBuilder.append("Set-Cookie: ").append(cookie).append("\r\n");
            }
            stringBuilder.append("\r\n");
        } else {
            stringBuilder.append("HTTP/1.1 ").append(stateCode).append("\r\n");
            stringBuilder.append("Content-Type: ").append(contentType).append("\r\n");
            stringBuilder.append("Content-Length: ").append(body.length).append("\r\n");
            stringBuilder.append("\r\n");
        }
        return stringBuilder.toString();
    }

    public void setContentType(String url) {
        String extension = getExtension(url);
        MIME mime = MIME.getMimeByExtension(extension);
        contentType = mime.getMime();
    }

    private String getExtension(String path) {
        String[] pathToken = path.split("\\.");
        int length = pathToken.length;
        if (length == 1) {
            return null;
        }
        return pathToken[length - 1];
    }

    public void setCookie(String cookie){
        this.cookie = cookie;
    }
}
