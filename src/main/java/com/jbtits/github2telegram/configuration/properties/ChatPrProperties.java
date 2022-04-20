package com.jbtits.github2telegram.configuration.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "chat.pr")
public class ChatPrProperties {

    private String prefix;
    private Set<String> keyWords;
}
