package demo.provider.scan;

import annotation.WubboService;
import demo.interfacex.DemoService;

@WubboService
public class DemoServiceImpl implements DemoService {
    @Override
    public String getMessage() {
        return "hello,world";
    }
}
