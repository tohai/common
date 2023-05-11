package com.example.commoncallapi.dto;

import com.example.commoncallapi.enumerate.ExternalApis;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpMethod;

import java.util.Hashtable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CallApiModel {
    private String url;
    private HttpMethod method;
    private Hashtable<String, String> header;
    private Object body;
    private ExternalApis apiType;
}
