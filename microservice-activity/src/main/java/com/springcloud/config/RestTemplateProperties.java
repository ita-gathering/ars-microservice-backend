package com.springcloud.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("rest")
@Data
public class RestTemplateProperties {
  private String userUrl;
}
