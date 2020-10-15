package com.maxwell.monitor.lisdata.consumer;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson.JSONObject;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * 监控lis系统中数据库表的变化
 * @author cb
 */
@Slf4j
@Component
public class MaxwellConsumer {

    private static final String INSERT = "insert";
    private static final String UPDATE = "update";
    private static final String DELETE = "delete";

    /**
     * 批量log记录
     */
    @RabbitListener(queues = "${lis.rabbitmq.maxwellQueue}",
            containerFactory = "rabbitListenerContainerFactory")
    public void processLisData(Message message, Channel channel) throws Exception {
        boolean msgSolvedOk = false;
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        boolean flag = true;
        try {
            // 如果开关打开,则进行数据监控,否则将不进行数据监控,直接丢弃数据
            if (flag) {
                byte[] body = message.getBody();
                String strMsg = new String(body, "UTF-8");
                log.info(">>>lis start- receive data: {}",strMsg);
                String table = JSONObject.parseObject(strMsg).getObject("table",String.class);
                String type = JSONObject.parseObject(strMsg).getObject("type",String.class);
                log.info(">>>发生变更的表为{}：",table);
                log.info(">>>该表发生了的变更类型为{}：",type);
                Map<String,String> newData = JSONObject.parseObject(strMsg).getObject("data", Map.class);
                if (INSERT.equals(type)) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("表" + table + "新增了一条记录,其中");
                    for(String key:newData.keySet()){
                        String value = newData.get(key) != null ? String.valueOf(newData.get(key)) : "";
                        if (!StringUtils.isEmpty(value)) {
                            sb.append("字段" + key + "被赋值" + value);
                            sb.append(";");
                        }
                    }
                    log.info(sb.toString());
                } else if (UPDATE.equals(type)) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("表" + table + "修改了一条记录,其中");
                    Map<String,String> oldData = JSONObject.parseObject(strMsg).getObject("old", Map.class);
                    for(String oldKey:oldData.keySet()){
                        for(String newKey:newData.keySet()){
                            if (oldKey.equals(newKey)) {
                                String oldValue = newData.get(oldKey);
                                String newValue = newData.get(newKey);
                                sb.append("字段" + newKey + "的值由" + oldValue + "变为" + newValue);
                                sb.append(";");
                            }
                        }
                    }
                    log.info(sb.toString());
                } else if (DELETE.equals(type)) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("表" + table + "删除了一条记录,其中");
                }
            } else {
                log.info("开关未打开！");
            }
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
