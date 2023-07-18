package webserver.http;

public class HttpResponse {

    private String stateCode;
    private String location;

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

    public String getResponseHead(int bodyLength) {
        StringBuilder stringBuilder = new StringBuilder();

        if (stateCode.equals("302 Found ")) {
            stringBuilder.append("HTTP/1.1 ").append(stateCode).append("\r\n");
            stringBuilder.append("Location: ").append(location).append("\r\n");
            stringBuilder.append("Content-Type: text/html;charset=utf-8\r\n");
            stringBuilder.append("Content-Length: ").append(bodyLength).append("\r\n");
            stringBuilder.append("\r\n");
        } else {
            stringBuilder.append("HTTP/1.1 ").append(stateCode).append("\r\n");
            stringBuilder.append("Content-Type: text/html;charset=utf-8\r\n");
            stringBuilder.append("Content-Length: ").append(bodyLength).append("\r\n");
            stringBuilder.append("\r\n");
        }
        return stringBuilder.toString();
    }
}
