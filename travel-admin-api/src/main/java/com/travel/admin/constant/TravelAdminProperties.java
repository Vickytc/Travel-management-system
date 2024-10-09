package com.travel.admin.constant;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Custom configuration constants
 */
@Data
@Component
@ConfigurationProperties(prefix = "travel-admin-api")
public class TravelAdminProperties {
    private Upload upload;
    private Login login;
    private Recaptcha recaptcha;

    @Data
    public static class Upload {
        private String path;
    }

    @Data
    public static class Login {
        private int errorCount;
        private int lockTime;
    }

    @Data
    public static class Recaptcha {
        private String siteKey;
        private String secretKey;
    }
}
