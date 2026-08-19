package com.sahithya.fooddeliverybackend.service;

import com.sahithya.fooddeliverybackend.exception.InvalidPaymentSignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.HexFormat;

@Service
public class PaymentSignatureService {

    private static final long MAX_TIMESTAMP_AGE_SECONDS = 300;

    private final String webhookSecret;

    public PaymentSignatureService(
            @Value("${payment.webhook-secret}")
            String webhookSecret
    ) {
        this.webhookSecret = webhookSecret;
    }

    public void verifySignature(
            String payload,
            String timestamp,
            String providedSignature
    ) {

        validateTimestamp(timestamp);

        String signedPayload =
                timestamp + "." + payload;

        String expectedSignature =
                generateSignature(signedPayload);

        boolean matches =
                MessageDigest.isEqual(
                        expectedSignature.getBytes(StandardCharsets.UTF_8),
                        providedSignature.getBytes(StandardCharsets.UTF_8)
                );

        if (!matches) {
            throw new InvalidPaymentSignatureException();
        }
    }

    private void validateTimestamp(String timestamp) {

        long requestTimestamp;

        try {
            requestTimestamp = Long.parseLong(timestamp);
        } catch (NumberFormatException exception) {
            throw new InvalidPaymentSignatureException();
        }

        long currentTimestamp =
                Instant.now().getEpochSecond();

        long age =
                Math.abs(currentTimestamp - requestTimestamp);

        if (age > MAX_TIMESTAMP_AGE_SECONDS) {
            throw new InvalidPaymentSignatureException();
        }
    }

    private String generateSignature(
            String payload
    ) {
        try {

            Mac mac =
                    Mac.getInstance("HmacSHA256");

            SecretKeySpec key =
                    new SecretKeySpec(
                            webhookSecret.getBytes(
                                    StandardCharsets.UTF_8
                            ),
                            "HmacSHA256"
                    );

            mac.init(key);

            byte[] result =
                    mac.doFinal(
                            payload.getBytes(
                                    StandardCharsets.UTF_8
                            )
                    );

            return HexFormat.of()
                    .formatHex(result);

        } catch (Exception exception) {
            throw new IllegalStateException(
                    "Unable to generate payment signature",
                    exception
            );
        }
    }
}