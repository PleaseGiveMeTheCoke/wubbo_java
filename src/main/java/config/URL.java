package config;

import java.util.HashMap;
import java.util.Map;

public class URL {
    Map<String,String> params;

    private String providerIP;
    private int providerPort;
    private String interfaceName;

    private Class<?> interfaceClass;
    public URL(String providerIP, int providerPort, Class<?> interfaceClass){
        params = new HashMap<>();
        this.interfaceClass = interfaceClass;
        this.interfaceName = interfaceClass.getName();
        this.providerIP = providerIP;
        this.providerPort = providerPort;
    }

    public URL(Class<?> interfaceClass){
        params = new HashMap<>();
        this.interfaceClass = interfaceClass;
        this.interfaceName = interfaceClass.getName();
    }

    public void addParameters(String key, String value){
        params.put(key,value);
    }

    public String getProviderIP() {
        return providerIP;
    }

    public void setProviderIP(String providerIP) {
        this.providerIP = providerIP;
    }

    public int getProviderPort() {
        return providerPort;
    }

    public void setProviderPort(int providerPort) {
        this.providerPort = providerPort;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public Class<?> getInterfaceClass() {
        return interfaceClass;
    }

    public void setInterfaceClass(Class<?> interfaceClass) {
        this.interfaceClass = interfaceClass;
    }
}
