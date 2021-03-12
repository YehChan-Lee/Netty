package socket_server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;

@Sharable
public class MyServerHandler extends ChannelInboundHandlerAdapter {
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception{ //메세지를 읽을때마다 호출하는 메세지
		ByteBuf in = (ByteBuf) msg;
		System.out.println("Server received : " + in.toString(CharsetUtil.UTF_8));
		ctx.writeAndFlush(in);
	}
	
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception{//channelRead를 모두 완료하고 실행하는 메소드
		ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
		   .addListener(ChannelFutureListener.CLOSE);
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception{//예외발생시 실행되는 메소드
		cause.printStackTrace();
		ctx.close();
	}
	
	
}
