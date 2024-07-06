package com.arms.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients({"com.arms.util.external_communicate","com.arms.client.dwr"})
public class OpenFeignConfig {
}
