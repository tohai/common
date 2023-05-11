package com.example.commoncallapi.config;

import com.example.commoncallapi.dto.CallApiModel;
import com.example.commoncallapi.exception.CallApiException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;

public interface CallApiWithRetryAdapter {

    public String getResponseMessage();

    public Object callApiWithNoRetry(CallApiModel request);

    public <T> T  callApiWithNoRetry(CallApiModel request, Class<T> responseClassType);

    public <T> T callAPI(CallApiModel requestApi, Class<T> responseClassType);
}
