package io.github.kimmking.gateway.router;

import java.util.List;
import java.util.Random;

/**
 * 在此实现路由器功能
 *      随机路由实现
 *      TODO RoundRobin
 */
public class MyHttpEndpointRouter implements HttpEndpointRouter {

    @Override
    public String route(List<String> endpoints) {
        int size = endpoints.size();
        Random random = new Random();
        int randomInt = random.nextInt(size);
        return endpoints.get(randomInt);
    }

}
