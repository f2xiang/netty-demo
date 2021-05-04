package cn.itcast.message;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @description:
 * @author: fengx
 * @create: 2021-05-03 20:27
 */
@Data
public class RpcResponseMessage extends Message {

    private Object res;
    private Exception exception;


    @Override
    public int getMessageType() {
        return RpcResponseMessage;
    }
}
