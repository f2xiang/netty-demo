package cn.itcast.tomcat.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

/**
 * @description: 封装DefaultFullHttpResponse
 * @author: fengx
 * @create: 2021-05-29 17:00
 */
public class Response<T> {

    private T data;
    private HttpHeaders headers;
    private DefaultFullHttpResponse response;


    public Response(T data) {
        this.data = data;
        //给客户端回传数据
        ByteBuf byteBuf = Unpooled.copiedBuffer(data.toString(), CharsetUtil.UTF_8);
        //使用DefaultFullHttpResponse作为response
        response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1, HttpResponseStatus.OK, byteBuf);

        //设置头信息,以及编码，防止乱码
        headers = response.headers();
        headers.add(HttpHeaders.Names.CONTENT_TYPE, "application/json;charset=utf8");
        headers.set(HttpHeaders.Names.CONTENT_LENGTH, byteBuf.readableBytes());

    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public HttpHeaders getHeaders() {
        return headers;
    }

    public void setHeaders(HttpHeaders headers) {
        this.headers = headers;
    }

    public DefaultFullHttpResponse getResponse() {
        return response;
    }

    public void setResponse(DefaultFullHttpResponse response) {
        this.response = response;
    }
}
