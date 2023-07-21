package webserver.http;

import java.util.Arrays;

public enum MIME {
    HTML("text/html;charset=utf-8"),
    CSS("text/css"),
    JS("text/javascript"),
    ICO("image/vnd.microsoft.icon"),
    PNG("image/png"),
    JPG("image/jpeg"),
    WOFF("application/x-font-woff"),
    WOFF2("font/woff2"),
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
