package canhxuan.quanlybanhang.service.email;

import canhxuan.quanlybanhang.dto.EmailRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailConsumer {

    @Autowired
    private JavaMailSender mailSender;

    @KafkaListener(topics = "email-topic", groupId = "email-group")
    public void consume(String emailJson) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            EmailRequest request = mapper.readValue(emailJson, EmailRequest.class);
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(request.getTo());
            message.setSubject(request.getSubject());
            message.setText(request.getText());
            mailSender.send(message);
            System.out.println("Email sent to " + request.getTo());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
