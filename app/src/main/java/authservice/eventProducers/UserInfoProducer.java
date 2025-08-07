package authservice.eventProducers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import authservice.model.UserInfoDto;

@Service
public class UserInfoProducer {
    private final KafkaTemplate<String, UserInfoDto> kafkaTemplate;

    @Value("${spring.kafka.topic-json.name}")
    private String TOPIC_NAME;

    // Spring initialiazes the kafkatemplate using app. properties.
    @Autowired
    UserInfoProducer(KafkaTemplate<String, UserInfoDto> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEventToKafka(UserInfoEvent userInfoEvent) {
        Message<UserInfoEvent> message = MessageBuilder.withPayload(userInfoEvent)
                .setHeader(KafkaHeaders.TOPIC, TOPIC_NAME)
                .build();
        kafkaTemplate.send(message);
    }

}
