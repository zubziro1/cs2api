package zub.cs2api.service;

import nl.vv32.rcon.Rcon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import zub.cs2api.config.SettingsProperties;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

@Service
public class RconCall {
    private final Logger log = LoggerFactory.getLogger(RconCall.class);

    private final SettingsProperties settingsProperties;

    public RconCall(SettingsProperties settingsProperties) {
        this.settingsProperties = settingsProperties;
    }

    public String send(String cmd) throws IOException {
        try (var rcon = Rcon.newBuilder()
                .withChannel(SocketChannel.open(new InetSocketAddress(settingsProperties.getRcon().getAddress(), settingsProperties.getRcon().getPort())))
                .withCharset(StandardCharsets.UTF_8)
                .build()) {


            log.debug("rcon call: {}", cmd);
            rcon.tryAuthenticate(settingsProperties.getRcon().getPassword());
            var response = rcon.sendCommand(cmd);

            if (!response.isEmpty()) {
                log.debug("rcon resp: {}", response);
            }

            return response;
        }
    }
}
