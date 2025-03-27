package com.github.caac.demo;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Service
public class TwilioWhatsAppService {

    private static final Logger logger = LoggerFactory.getLogger(TwilioWhatsAppService.class);

    @Value("${twilio.whatsapp.number}")
    private String fromWhatsAppNumber;

    private final CustomerAgenticService customerAgenticService;

    public TwilioWhatsAppService(CustomerAgenticService customerAgenticService) {
        this.customerAgenticService = customerAgenticService;
    }

    public String sendWhatsAppMessage(String to, String messageBody) {
                Message message = Message.creator(
                new PhoneNumber("whatsapp:" + to),
                new PhoneNumber(fromWhatsAppNumber),
                messageBody)
                .create();

        return message.getSid();
    }

    @Async
    public void sendWhatsAppMessageAsync(Map<String, String> payload) {
        logger.info("Method processLongRunningTask Async... " + payload);

        String from = payload.get("From");
        String body = payload.get("Body");

        String to = payload.get("From");

        String recommendation = customerAgenticService.chatWithAgents(from, body);

        String responseMessage = """
                %s
                """.formatted(recommendation);

        if (responseMessage.length() > 1600) {
            responseMessage = responseMessage.substring(0, 1600);
        }

        Message message = Message.creator(
                new PhoneNumber(to),
                new PhoneNumber(fromWhatsAppNumber),
                responseMessage).create();

        logger.info("Sent to WhatsApp Sid: " + message.getSid());
    }
}
