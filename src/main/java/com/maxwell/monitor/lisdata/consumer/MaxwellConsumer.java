package com.maxwell.monitor.lisdata.consumer;

import com.maxwell.monitor.lisdata.dao.LogMapper;
import com.maxwell.monitor.lisdata.dao.SwitchControlMapper;
import com.maxwell.monitor.lisdata.entity.Log;
import com.maxwell.monitor.lisdata.entity.SwitchControl;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;
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
     * maxwell数据处理开关 0-开启 1-关闭
     */
    private static final String MAXWELL_SWITCH_STATE = "0";

    private static final String MAXWELL_SWITCH_TYPE = "maxwell";

    @Autowired
    private LogMapper logMapper;

    @Autowired
    private SwitchControlMapper switchControlMapper;

    /**
     * 批量log记录
     */
    @RabbitListener(queues = "${lis.rabbitmq.maxwellQueue}",
            containerFactory = "rabbitListenerContainerFactory")
    public void processLisData(Message message, Channel channel) throws Exception {
        boolean msgSolvedOk = false;
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        boolean flag = false;
        try {
            // 如果开关打开,则进行数据监控,否则将不进行数据监控,直接丢弃数据
            SwitchControl record = new SwitchControl();
            record.setSwitchType(MAXWELL_SWITCH_TYPE);
            SwitchControl switchControl = switchControlMapper.selectOne(record);
            if (switchControl != null && MAXWELL_SWITCH_STATE.equals(switchControl.getSwitchState())) {
                flag = true;
            }
            if (flag) {
                byte[] body = message.getBody();
                String strMsg = new String(body, "UTF-8");
                log.info(">>>lis start- receive data: {}",strMsg);
                String table = JSONObject.parseObject(strMsg).getObject("table",String.class);
                String type = JSONObject.parseObject(strMsg).getObject("type",String.class);
                log.info(">>>发生变更的表为{}：",table);
                log.info(">>>该表发生了的变更类型为{}：",type);
                Map<String,String> newData = JSONObject.parseObject(strMsg).getObject("data", Map.class);
                StringBuilder sb = new StringBuilder();
                if (INSERT.equals(type)) {
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
                    sb.append("表" + table + "修改了一条记录,其中");
                    Map<String,String> oldData = JSONObject.parseObject(strMsg).getObject("old", Map.class);
                    for(String oldKey:oldData.keySet()){
                        for(String newKey:newData.keySet()){
                            if (oldKey.equals(newKey)) {
                                String oldValue = oldData.get(oldKey);
                                String newValue = newData.get(newKey);
                                sb.append("字段" + newKey + "的值由" + oldValue + "变为" + newValue);
                                sb.append(";");
                            }
                        }
                    }
                    log.info(sb.toString());
                } else if (DELETE.equals(type)) {
                    sb.append("表" + table + "删除了一条记录,其中");
                }
                Log log = new Log();
                log.setLogDesc(sb.toString());
                log.setCreateDate(new Date());
                logMapper.insert(log);
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
