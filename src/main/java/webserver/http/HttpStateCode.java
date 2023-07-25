package webserver.http;

public class HttpStateCode {

    public static final String OK = "200 OK "; // todo 상태코드랑 메시지 enum으로 분류하는게 어떤지
    public static final String REDIRECT = "302 Found ";
    public static final String NOT_FOUND = "404 Not Found ";

    private HttpStateCode() {

    } 

}
