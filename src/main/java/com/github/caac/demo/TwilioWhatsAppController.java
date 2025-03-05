package com.github.caac.demo;

import org.apache.commons.collections4.MultiValuedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public class TwilioWhatsAppController {

    private static final Logger logger = LoggerFactory.getLogger(TwilioWhatsAppController.class);

    private final TwilioWhatsAppService twilioWhatsAppService;

    public TwilioWhatsAppController(TwilioWhatsAppService twilioWhatsAppService) {
        this.twilioWhatsAppService = twilioWhatsAppService;
    }

    @PostMapping("/send")
    public String sendWhatsAppMessage(@RequestParam("to") String to,
            @RequestParam("message") String message) {
        return twilioWhatsAppService.sendWhatsAppMessage(to, message);
    }

    @PostMapping("/receive")
    public ResponseEntity<String> receiveWhatsAppMessage(@RequestBody MultiValuedMap<String, String> body) {
        String from = body.get("From").stream().findFirst().get();
        String messageBody = body.get("Body").stream().findFirst().get();

        logger.info("Received message: {} from {}", messageBody, from);

        return ResponseEntity.ok("Message received");
    }
}
