package config;

import annotation.WubboReference;
import consumer.Invoker;
import exception.AnnotationNotFoundException;
import netty.client.Client;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;

import java.lang.annotation.Annotation;
import java.lang.annotation.AnnotationTypeMismatchException;
import java.sql.Ref;

public class ReferenceBean {
    private Object proxy;
    private Class<?> interfaceClass;
    private String interfaceName;

    private String providerIP;

    private int providerPort;
    public Object getObject() {
        if (proxy == null) {
            createProxy();
        }
        return proxy;
    }
    private void createProxy() {
        Invoker invoker = refer(interfaceClass);
        proxy = getProxy(invoker);
    }
    public ReferenceBean(Class<?> interfaceClass){
        this.interfaceClass = interfaceClass;
        this.interfaceName = interfaceClass.getName();
        readAnnotation();
    }

    private void readAnnotation() {
        WubboReference reference = interfaceClass.getDeclaredAnnotation(WubboReference.class);
        if(reference == null){
            throw new AnnotationNotFoundException("Annotation WubboReference not found on interface: "+interfaceName);
        }
        this.providerIP = reference.providerIP();
        this.providerPort = reference.providerPort();
    }

    private Invoker refer(Class<?> serviceType) {
        URL url = new URL(providerIP,providerPort,serviceType);
        return new Invoker(url, getClients(url));
    }

    private Client getClients(URL url) {
        String host = url.getProviderIP();
        int port = url.getProviderPort();
        return new Client(host,port);
    }

    private Object getProxy(Invoker invoker) {
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.addInterface(interfaceClass);
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor();
        advisor.setAdvice(new MethodInterceptor() {
            @Override
            public Object invoke(MethodInvocation methodInvocation) throws Throwable {
                return invoker.invoke(methodInvocation);
            }
        });
        proxyFactory.addAdvisor(advisor);
        return proxyFactory.getProxy();
    }
}
