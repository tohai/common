package com.example.commoncallapi.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Component;

@Configuration
public class LocalDateTimeSerializerConfig {
    @Bean
    public ObjectMapper serializingObjectMapper() {
        JavaTimeModule module = new JavaTimeModule();
        return Jackson2ObjectMapperBuilder.json().modules(module)
            .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS).build();
    }

	@Component
	@Configuration
	public static class RequestFactoryConfig {

		@Autowired
		private OkHttpClientFactoryImpl httpClientFactory;

		@Bean
		@Qualifier("createOKCommonsRequestFactory")
		public ClientHttpRequestFactory createOKCommonsRequestFactory() {
			OkHttpClient client = httpClientFactory.createBuilder(false).build();
			return new OkHttp3ClientHttpRequestFactory(client);
		}
	}
}
