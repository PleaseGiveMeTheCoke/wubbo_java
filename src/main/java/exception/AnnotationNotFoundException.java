package exception;

public class AnnotationNotFoundException extends RuntimeException{
    public AnnotationNotFoundException(String message){
        super(message);
    }
}
