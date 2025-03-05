package com.github.caac.demo;

import java.util.Map;

import org.apache.commons.collections4.MultiValuedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TwilioWhatsAppController {

    private static final Logger logger = LoggerFactory.getLogger(TwilioWhatsAppController.class);

    private final TwilioWhatsAppService twilioWhatsAppService;

    private CustomerAgenticService customerAgenticService;

    public TwilioWhatsAppController(TwilioWhatsAppService twilioWhatsAppService,
            CustomerAgenticService customerAgenticService) {
        this.twilioWhatsAppService = twilioWhatsAppService;
        this.customerAgenticService = customerAgenticService;
    }

    @PostMapping("/send")
    public String sendWhatsAppMessage(@RequestParam("to") String to,
            @RequestParam("message") String message) {
        return twilioWhatsAppService.sendWhatsAppMessage(to, message);
    }

    @PostMapping("/receive")
    public ResponseEntity<String> receiveWhatsAppMessage(@RequestBody MultiValuedMap<String, String> payload) {
        String from = payload.get("From").stream().findFirst().get();
        String body = payload.get("Body").stream().findFirst().get();

        logger.info("Received message: {} from {}", body, from);

        return ResponseEntity.ok("Message received");
    }

    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, value = "/receive-wa")
    public ResponseEntity<String> receiveMessage(@RequestParam Map<String, String> payload) {
        String from = payload.get("From");
        String body = payload.get("Body");

        logger.info("Received message: {} from {}", body, from);

        String response = customerAgenticService.chatWithAgentsWithoutProtection(body);

        String responseMessage = "<Response><Message>" + response + "</Message></Response>";

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_XML)
                .body(responseMessage);
    }
}
