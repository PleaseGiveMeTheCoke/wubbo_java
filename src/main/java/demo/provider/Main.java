package demo.provider;


import org.springframework.beans.factory.Aware;
import provider.WubboProvider;

import java.io.Serializable;

public class Main {
    public static void main(String[] args) throws InstantiationException, IllegalAccessException {
        WubboProvider provider = new WubboProvider("demo.provider.scan",8181);
        provider.start();
    }
}
