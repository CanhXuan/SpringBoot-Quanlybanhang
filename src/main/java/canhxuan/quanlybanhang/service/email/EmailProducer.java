package canhxuan.quanlybanhang.service.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class EmailProducer {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendEmailEvent(String emailJson) {
        kafkaTemplate.send("email-topic", emailJson);
        System.out.println("📨 Sent email payload to Kafka: " + emailJson);
    }
}
