package webserver.http;

import java.util.Arrays;

public enum MIME {
    HTML("text/html;charset=utf-8"),
    CSS("text/css"),
    JS("text/javascript"),
    ICO("image/x-icon"),
    PNG("image/png"),
    JPG("image/jpeg"),
    OTHER("text/plain");

    private final String mime;

    MIME(String mime){
        this.mime = mime;
    }

    public String getMime() {
        return mime;
    }

    public static MIME getMimeByExtension(String extension){
        if (extension == null){
            return HTML;
        }
        return Arrays.stream(MIME.values())
                .filter(mime -> extension.toUpperCase().equals(mime.toString()))
                .findFirst()
                .orElse(OTHER);
    }
}
