package cn.itcast.message;


/**
 * @description:
 * @author: fengx
 * @create: 2021-05-03 17:35
 */
public class PingMessage extends Message {
    @Override
    public int getMessageType() {
        return PingMessage;
    }
}
