package com.sid.gl.kafka;

import com.sid.gl.constants.KafkaConstant;
import com.sid.gl.dto.SubscriptionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class PaymentProducer {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(PaymentProducer.class);

    private KafkaTemplate<String, SubscriptionEvent> kafkaTemplate;

    public PaymentProducer(KafkaTemplate<String, SubscriptionEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(SubscriptionEvent event){
        LOGGER.info(
                String.format("payment event --- %s",event.toString())
        );
        //create message
        Message<SubscriptionEvent> message =
                MessageBuilder
                        .withPayload(event)
                        .setHeader(KafkaHeaders.TOPIC, KafkaConstant.TOPIC_CONSTANT)
                        .build();
        kafkaTemplate.send(message);

    }
}
