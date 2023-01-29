package common;


import org.aopalliance.intercept.MethodInvocation;

import java.io.Serializable;

public class Invocation implements Serializable {
    private String interfaceName;
    private String methodName;
    private Object[] arguments;
    private Class<?>[] types;

    public Invocation(MethodInvocation m, String interfaceName) {
        this.interfaceName = interfaceName;
        this.methodName = m.getMethod().getName();
        this.arguments = m.getArguments();
        this.types = m.getMethod().getParameterTypes();
    }

    public Invocation(String interfaceName, String methodName, Object[] arguments, Class<?>[] types) {
        this.interfaceName = interfaceName;
        this.methodName = methodName;
        this.arguments = arguments;
        this.types = types;
    }

    public String getMethodName() {
        return methodName;
    }

    public Object[] getArguments() {
        return arguments;
    }

    public Class<?>[] getTypes() {
        return types;
    }

    public String getInterfaceName() {
        return interfaceName;
    }



}
