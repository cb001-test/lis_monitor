package com.maxwell.monitor.lisdata.consumer;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson.JSONObject;

/**
 * 监控lis系统中数据库表的变化
 * @author cb
 */
@Slf4j
@Component
public class MaxwellConsumer {

    /**
     * 批量log记录
     */
    @RabbitListener(queues = "${lis.rabbitmq.maxwellQueue}",
            containerFactory = "rabbitListenerContainerFactory")
    public void processLisData(Message message, Channel channel) throws Exception {
        boolean msgSolvedOk = false;
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            byte[] body = message.getBody();
            String strMsg = new String(body, "UTF-8");
            log.info(">>>lis start- receive data: {}",strMsg);
            String table = JSONObject.parseObject(strMsg).getObject("table",String.class);
            String type = JSONObject.parseObject(strMsg).getObject("type",String.class);
            log.info(">>>发生变更的表为：",table);
            log.info(">>>该表发生了的变更类型为：",type);
            msgSolvedOk = true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            msgSolvedOk = false;
        } finally {
            if (msgSolvedOk) {
                channel.basicAck(deliveryTag,false);
            } else {
                channel.basicReject(deliveryTag, false);
            }
        }
    }
}
