package app;

import java.net.InetSocketAddress;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import socket_server.MyServerHandler;

public class NettyServer {
	
	private int port;
	
	public NettyServer(int port) {
		this.port = port;
	}
	
	public void run() throws Exception{
		final MyServerHandler serverHandler = new MyServerHandler();//ChannelInboundAdapter를 상속받아서 channelRead,channelReadComplete,error를 재정의한다.
		EventLoopGroup group = new NioEventLoopGroup();
		
		try {
			ServerBootstrap b = new ServerBootstrap();//serverBootstrap 생성
			b.group(group)
				.channel(NioServerSocketChannel.class)//논블로킹타입으로 채널생성
				.localAddress(new InetSocketAddress(port))//설정한 port로 소켓주소 생성
				.childHandler(new ChannelInitializer<SocketChannel>() {
					@Override //채널이 생성되면 동작하는 메소드
					protected void initChannel(SocketChannel ch) throws Exception {
						ch.pipeline().addLast(serverHandler);
					}
				});
			
			ChannelFuture f = b.bind().sync();//서버를 비동기식으로 binding , sync로 바인딩완료까지 대기
			f.channel().closeFuture().sync();//channel의 closefuture를 얻고 현 스레드 블로킹
		}finally {
			group.shutdownGracefully().sync();//모든 리소스 해제
		}
	}
	
	public static void main(String[] args) throws Exception {
		int port;
		if(args.length > 0) {
			port = Integer.parseInt(args[0]);
		}else {
			port = 9090;
		}
		new NettyServer(port).run();
	}

}
