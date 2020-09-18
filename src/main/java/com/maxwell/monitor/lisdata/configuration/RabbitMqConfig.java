package com.maxwell.monitor.lisdata.configuration;


import lombok.Data;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


/**
 * @author cb
 */
@Data
@ConfigurationProperties(ignoreUnknownFields = false, prefix = "lis.rabbitmq")
@Component
public class RabbitMqConfig {


    @Value("${spring.rabbitmq.host}")
    private String host;

    @Value("${spring.rabbitmq.port}")
    private int port;

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;


    private String maxwellQueue;
    private String maxwellExchange;

    @Bean
    public Queue maxwellQueue() {
        return new Queue(maxwellQueue);
    }
    @Bean
    public FanoutExchange maxwellExchange() {
        return new FanoutExchange(maxwellExchange,false,false);
    }
    @Bean
    public Binding bindingMaxwellExchange(Queue maxwellQueue, FanoutExchange maxwellExchange) {
        return BindingBuilder.bind(maxwellQueue).to(maxwellExchange);
    }


    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        return factory;
    }
}
