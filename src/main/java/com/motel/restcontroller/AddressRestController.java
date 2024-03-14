package com.motel.restcontroller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AddressRestController {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
