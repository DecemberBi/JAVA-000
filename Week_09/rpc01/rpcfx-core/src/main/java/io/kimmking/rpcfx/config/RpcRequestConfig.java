package io.kimmking.rpcfx.config;

import okhttp3.OkHttpClient;

public class RpcRequestConfig {

    private static OkHttpClient client;

    public static synchronized OkHttpClient getClient() {
        if (client == null) {
            return new OkHttpClient();
        }
        return client;
    }

}
