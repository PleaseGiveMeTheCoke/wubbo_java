package demo.interfacex;
import annotation.WubboReference;

@WubboReference(providerIP = "localhost",providerPort = 8181)
public interface DemoService {
    String getMessage();
}
