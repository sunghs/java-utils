package sunghs.java.utils.file.exception;

public class InvalidResourceContextException extends RuntimeException {

    public InvalidResourceContextException(String message) {
        super("유효하지 않은 UrlMultiResourceContext 입니다. 사유 : " + message);
    }
}
