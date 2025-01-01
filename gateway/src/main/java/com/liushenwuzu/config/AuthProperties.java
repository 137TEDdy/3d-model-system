package com.liushenwuzu.config;

import java.util.List;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "em.auth")
public class AuthProperties {
    private List<String> includePaths;
    private List<String> excludePaths;
}
