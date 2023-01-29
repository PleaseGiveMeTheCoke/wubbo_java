package consumer;

import common.Invocation;
import config.URL;
import netty.client.Client;
import org.aopalliance.intercept.MethodInvocation;
public class Invoker {
    URL url;

    Client client;

    public Invoker(URL url, Client client){
        this.client = client;
        this.url = url;
    }
    public Object invoke(MethodInvocation methodInvocation){
        return client.request(new Invocation(methodInvocation,url.getInterfaceName())).get(3000);
    }
}
