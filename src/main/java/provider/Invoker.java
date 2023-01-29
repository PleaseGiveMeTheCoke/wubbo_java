package provider;

import common.Invocation;
import config.URL;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Invoker {
    private Wrapper wrapper;
    private Invoker(Wrapper wrapper){
        this.wrapper = wrapper;
    }
    public static Invoker getInvoker(Class<?> type, URL url) {
        Wrapper wrapper = Wrapper.getWrapper(type);
        return new Invoker(wrapper);
    }
    public Object invoke(Invocation invocation){
        String methodName = invocation.getMethodName();
        Object o = Exporter.getService(invocation.getInterfaceName());
        Object[] arguments = invocation.getArguments();
        Class<?>[] parameterTypes = invocation.getTypes();
        try {
            return wrapper.invokeMethod(o,methodName,parameterTypes,arguments);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
