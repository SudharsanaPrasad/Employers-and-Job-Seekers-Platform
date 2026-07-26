package com.example.careernest.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

// sends SMS via Twilio. When the Twilio credentials are not set the service stays
// disabled and just logs the message, so the app runs without an account; set the
// TWILIO_* env vars to send real messages. An SMS failure never breaks the caller.
@Service
@Slf4j
public class SmsService {

    private final String fromNumber;
    private final boolean enabled;

    public SmsService(@Value("${twilio.account-sid}") String accountSid,
                      @Value("${twilio.auth-token}") String authToken,
                      @Value("${twilio.from-number}") String fromNumber) {
        this.fromNumber = fromNumber;
        this.enabled = !accountSid.isBlank() && !authToken.isBlank() && !fromNumber.isBlank();
        if (enabled) {
            Twilio.init(accountSid, authToken);
            log.info("Twilio SMS enabled (from {})", fromNumber);
        } else {
            log.warn("Twilio SMS disabled - credentials not set; messages will be logged only");
        }
    }

    public void send(String toPhone, String message) {
        if (toPhone == null || toPhone.isBlank()) {
            log.warn("SMS skipped: recipient has no phone number");
            return;
        }
        if (!enabled) {
            log.info("[SMS-DISABLED] to {} -> {}", toPhone, message);
            return;
        }
        try {
            Message.creator(new PhoneNumber(toPhone), new PhoneNumber(fromNumber), message).create();
            log.info("SMS sent to {}", toPhone);
        } catch (Exception e) {
            log.error("SMS to {} failed: {}", toPhone, e.getMessage());
        }
    }
}
