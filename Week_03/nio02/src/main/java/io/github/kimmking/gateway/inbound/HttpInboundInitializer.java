package io.github.kimmking.gateway.inbound;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

import java.util.ArrayList;
import java.util.List;

public class HttpInboundInitializer extends ChannelInitializer<SocketChannel> {
	
	private String proxyServer;
	private List<String> endpoints;

	/**
	 * 路由的地址在这里加入
	 */
	public HttpInboundInitializer() {
		List<String> endpoints = new ArrayList<>();
		endpoints.add("http://localhost:8808");
		endpoints.add("http://localhost:8809");
		this.endpoints = endpoints;
	}

	public HttpInboundInitializer(String proxyServer) {
		this.proxyServer = proxyServer;
	}

	@Override
	public void initChannel(SocketChannel ch) {
		ChannelPipeline p = ch.pipeline();
//		if (sslCtx != null) {
//			p.addLast(sslCtx.newHandler(ch.alloc()));
//		}
		p.addLast(new HttpServerCodec());
		//p.addLast(new HttpServerExpectContinueHandler());
		p.addLast(new HttpObjectAggregator(1024 * 1024));
		// 这里传入路由列表
		p.addLast(new HttpInboundHandler(this.proxyServer, this.endpoints));
	}
}
