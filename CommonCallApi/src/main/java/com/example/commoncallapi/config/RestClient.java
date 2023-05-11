package com.example.commoncallapi.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@Component
public class RestClient {

    private final RestTemplate client;

    public RestClient(@Qualifier("createRestTemplate") RestTemplate client, ObjectMapper mapper) {
        this.client = client;
        this.mapper = mapper;
    }

    private final ObjectMapper mapper;



    public <T> T postApi(String url, Object request, Class<T> classType) {
        try {
            var response = client.postForObject(url, request, classType);
            return response;
        } catch (Exception ex) {
            throw ex;
        }
    }

    public <T> T getApi(String url, Class<T> classType) {
        try {
            return client.getForObject(url, classType);
        } catch (Exception ex) {
            throw ex;
        }
    }

    public <T> T getApi(String url, Object request, Class<T> classType) {
        try {
            final Map<String, Object> map = mapper.convertValue(request, Map.class);
            var response = client.getForObject(url, classType, map);
            return response;
        } catch (Exception ex) {
            throw ex;
        }
    }

    public void putApi(String url, Object request, Object queryString) {
        try {
            final Map<String, Object> map = mapper.convertValue(queryString, Map.class);
            if(map == null){
                client.put(url, request);
                return;
            }

            client.put(url, request, map);
        } catch (Exception ex) {
            throw ex;
        }
    }

    public void deleteApi(String url, Object request) {
        try {
            final Map<String, Object> map = mapper.convertValue(request, Map.class);
            client.delete(url, map);
        } catch (Exception ex) {
            throw ex;
        }
    }
}
