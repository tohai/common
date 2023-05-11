package com.example.commoncallapi.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@Getter
@Configuration
public class RestTemplateConfig {

    @Qualifier("createOKCommonsRequestFactory")
    @Autowired
    private ClientHttpRequestFactory okHttpRequestFactory;

//    @Value("${clients.schedule.uri}")
//    private String scheduleBaseUri;
//
//    @Value("${clients.smartbank.uri}")
//    private String scheduleDomain;

    @Value("${clients.profile.uri}")
    private String profileUrl;

    @Value("${clients.media.uri}")
    private String mediaUrl;

    @Bean
    public RestTemplate createRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(okHttpRequestFactory);
        restTemplate.getMessageConverters().removeIf(aConverter -> (aConverter instanceof MappingJackson2XmlHttpMessageConverter)); // fixme

        return restTemplate;
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
