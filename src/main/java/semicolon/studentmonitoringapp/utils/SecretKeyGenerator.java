package semicolon.studentmonitoringapp.utils;

import lombok.extern.slf4j.Slf4j;

import java.security.SecureRandom;
import java.util.Base64;

@Slf4j
public class SecretKeyGenerator {

    public static void main(String[] args) throws Exception {
        byte[] secretKey = new byte[32];
        new SecureRandom().nextBytes(secretKey);
        String baseKey =  Base64.getEncoder().encodeToString(secretKey);
        log.info("baseKey: {}", baseKey);
    }
}
