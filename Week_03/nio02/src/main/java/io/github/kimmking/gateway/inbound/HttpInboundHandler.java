package io.github.kimmking.gateway.inbound;

import io.github.kimmking.gateway.filter.HttpRequestFilter;
import io.github.kimmking.gateway.filter.MyHttpRequestFilter;
import io.github.kimmking.gateway.outbound.okhttp.OkhttpOutboundHandler;
import io.github.kimmking.gateway.router.HttpEndpointRouter;
import io.github.kimmking.gateway.router.MyHttpEndpointRouter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class HttpInboundHandler extends ChannelInboundHandlerAdapter {

    private static Logger logger = LoggerFactory.getLogger(HttpInboundHandler.class);
    private final String proxyServer;
    private OkhttpOutboundHandler handler;
    private HttpRequestFilter httpRequestFilter;
    private HttpEndpointRouter httpEndpointRouter;
    
    public HttpInboundHandler(String proxyServer, List<String> endpoints) {
        this.proxyServer = proxyServer;
        httpEndpointRouter = new MyHttpEndpointRouter();
        handler = new OkhttpOutboundHandler(this.proxyServer, httpEndpointRouter, endpoints);
        httpRequestFilter = new MyHttpRequestFilter();
    }
    
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            logger.info("接到请求，msg={}", msg);
            FullHttpRequest fullRequest = (FullHttpRequest) msg;
            httpRequestFilter.filter(fullRequest, ctx);
            handler.handle(fullRequest, ctx);
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

}
