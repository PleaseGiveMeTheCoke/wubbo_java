package constant;

public class Constants {
    public static final int DEFAULT_IO_THREADS = Math.min(Runtime.getRuntime().availableProcessors() + 1, 32);
    public static final int REQUEST_TYPE = 0;
    public static final int RESPONSE_TYPE = 1;
}
