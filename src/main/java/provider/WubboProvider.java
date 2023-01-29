package provider;

import annotation.WubboReference;
import annotation.WubboService;
import config.URL;
import exception.AnnotationNotFoundException;
import netty.server.Server;
import util.ReflectUtils;

import java.util.Set;

public class WubboProvider {
    String scanPackage;
    int port;

    public WubboProvider(String scanPackage, int port) {
        this.scanPackage = scanPackage;
        this.port = port;
    }
    public void start() throws InstantiationException, IllegalAccessException {
        Set<Class<?>> classes = ReflectUtils.getClasses(scanPackage);
        for (Class<?> c : classes) {
            if(c.isAnnotationPresent(WubboService.class)){
                WubboService ws = c.getDeclaredAnnotation(WubboService.class);
                Class<?>[] interfaces = c.getInterfaces();
                Class<?> type = interfaces[ws.interfaceIndex()];
                URL url = new URL(type);
                Exporter.export(url,c.newInstance());
            }
        }
        Server.openServer(port);
    }

}
