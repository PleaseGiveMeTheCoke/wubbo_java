package provider;

import config.URL;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class Exporter {
    private static final ConcurrentHashMap<String,Invoker> exporterMap = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String,Object> serviceMap = new ConcurrentHashMap<>();
    public static void export(URL url, Object impl){
        exporterMap.put(url.getInterfaceName(),Invoker.getInvoker(url.getInterfaceClass(),url));
        serviceMap.put(url.getInterfaceName(),impl);
    }
    public static Invoker getInvoker(String interfaceName){
        return exporterMap.get(interfaceName);
    }

    public static Object getService(String interfaceName){
        return serviceMap.get(interfaceName);
    }
}
