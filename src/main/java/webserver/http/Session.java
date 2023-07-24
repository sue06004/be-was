package webserver.http;

import java.util.UUID;

public class Session {

    private Session(){

    }

    public static String createSessionId(){
        return UUID.randomUUID().toString();
    }
}
