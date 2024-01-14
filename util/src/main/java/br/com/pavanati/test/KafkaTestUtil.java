package br.com.pavanati.test;

import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

public class KafkaTestUtil {

    public static Message<String> buildKaftaMessageWithStatus(String topic, String message, String status) {
        return MessageBuilder.withPayload(message)
                .setHeader(KafkaHeaders.TOPIC, topic)
                .setHeader("status", status)
                .build();
    }

    public static Message<String> buildKaftaMessageWithId(String topic, String message, String id) {
        return MessageBuilder.withPayload(message)
                .setHeader(KafkaHeaders.TOPIC, topic)
                .setHeader("orderPayloadId", id)
                .setHeader("integrationPlatform", "occ")
                .build();
    }

}
