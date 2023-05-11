package com.example.commoncallapi.config;

import com.example.commoncallapi.dto.CallApiModel;
import com.example.commoncallapi.dto.SandMoBileDto;
import com.example.commoncallapi.dto.SandPartnerDto;
import com.example.commoncallapi.enumerate.ExternalApis;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class CallApiWithRetryAdapterImpl implements CallApiWithRetryAdapter {

    @Autowired
    @Qualifier("createRestTemplate")
    private RestTemplate restTemplate;

    private String responseMessage = "";

    @Override
    public String getResponseMessage() {
        return responseMessage;
    }

    @Override
    public Object callApiWithNoRetry(CallApiModel request) {
        switch (request.getApiType()) {
            case SAN_PARTNER:
                var resp1 = callAPI(request, SandPartnerDto.class);
                return resp1;
            case REQUEST_RECEIVE_CALLBACK:
                var resp2 = callAPI(request, SandMoBileDto.class);
                return resp2;
            case OTHER:
                var resp3 = callAPI(request, String.class);
                return resp3;
        }
        return null;
    }

    @Override
    public <T> T  callApiWithNoRetry(CallApiModel request, Class<T> responseClassType) {
        var coreResponse = callAPI(request, responseClassType);
        if (coreResponse != null) {
            Gson gson = new Gson();
            JsonObject jsonObject = gson.toJsonTree(coreResponse).getAsJsonObject();
            T response = gson.fromJson(jsonObject, responseClassType);
            return response;
        }
        return null;
    }


    @Override
    public <T> T callAPI(CallApiModel requestApi, Class<T> responseClassType) {
        var baseUrl = "";
        var request = requestApi.getBody();
        String toJson = new Gson().toJson(request);
        if (ExternalApis.SAN_PARTNER.equals(requestApi.getApiType())) {
            baseUrl = "base1";
        } else {
            baseUrl = "base2";
        }
        try {
            HttpMethod httpMethod = requestApi.getMethod();
            String url = requestApi.getUrl();


            Object coreResponse = null;
            switch (httpMethod) {
                case POST:
                    coreResponse = restTemplate.postForObject(baseUrl + url, request, Object.class);
                    break;

                case PATCH:
                    coreResponse = restTemplate.patchForObject(baseUrl + url, request, responseClassType);
                    break;

                case GET:
                    coreResponse = restTemplate.getForObject(baseUrl + url, responseClassType);
                    break;
            }

            log.info("[CallApi] Response url: {}, result: {}", baseUrl + url, coreResponse);
            return (T) coreResponse;
        } catch (Exception ex) {
            log.error("[CallApi] Error exception request api: {}, message: {}", requestApi, ExceptionUtils.getStackTrace(ex));
            return null;
        }
    }
}
