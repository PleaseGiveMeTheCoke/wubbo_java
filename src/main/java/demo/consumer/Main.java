package demo.consumer;

import config.ReferenceBean;
import demo.interfacex.DemoService;

public class Main {
    public static void main(String[] args) {
        ReferenceBean referenceBean = new ReferenceBean(DemoService.class);
        DemoService service = (DemoService)referenceBean.getObject();
        System.out.println(service.getMessage());
    }
}
