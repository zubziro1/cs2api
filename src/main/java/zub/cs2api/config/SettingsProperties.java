package zub.cs2api.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ConfigurationProperties(prefix = "settings")
@SuppressWarnings("unused")
public class SettingsProperties {
    private String baseUrlCallback;
    private Long waitForCallbackTimeOut;
    private Rcon rcon;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Rcon {
        private String password;
        private String address;
        private Integer port;
    }
}