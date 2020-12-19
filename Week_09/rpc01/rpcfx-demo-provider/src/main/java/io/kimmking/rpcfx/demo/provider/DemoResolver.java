package io.kimmking.rpcfx.demo.provider;

import io.kimmking.rpcfx.api.RpcfxResolver;
import org.reflections.Reflections;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DemoResolver implements RpcfxResolver, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public Object resolve(String serviceClass) {
        try {
            Class<?> interfaceClass = Class.forName(serviceClass);
            Reflections reflections = new Reflections("io.kimmking.rpcfx.demo.provider");
            Set<Class<?>> implClass = reflections.getSubTypesOf((Class<Object>) interfaceClass);
            return implClass.iterator().next().newInstance();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return null;
//        return this.applicationContext.getBean(serviceClass);
    }
}
