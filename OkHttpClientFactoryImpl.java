package com.ssf.albatross.config;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.commons.httpclient.OkHttpClientFactory;
import org.springframework.stereotype.Component;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class OkHttpClientFactoryImpl implements OkHttpClientFactory {

    @Autowired
    private AppCoreConfig coreConfig;

    @SneakyThrows
    @Override
    public OkHttpClient.Builder createBuilder(boolean disableSslValidation) {

        // Create a trust manager that does not validate certificate chains
        final TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] chain,
                                                   String authType) throws CertificateException {
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] chain,
                                                   String authType) throws CertificateException {
                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }
                }
        };

        // Install the all-trusting trust manager
        final SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
        // Create an ssl socket factory with our all-trusting manager
        final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        ConnectionPool okHttpConnectionPool = new ConnectionPool(50, 30, TimeUnit.SECONDS);
        builder.connectionPool(okHttpConnectionPool);
        builder.connectTimeout(120, TimeUnit.SECONDS);
        builder.writeTimeout(120, TimeUnit.SECONDS);
        builder.readTimeout(120, TimeUnit.SECONDS);
        builder.retryOnConnectionFailure(false);
        builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0])
                .hostnameVerifier((hostname, session) -> true);
        builder.addInterceptor(chain -> {
            final Request original = chain.request();
            final Request.Builder requestBuilder = original.newBuilder();
            var bankSecurityUri = coreConfig.getUriCoreBankSecurity();
            var bankBaseUri = coreConfig.getCoreBaseUri();
            var fullUrl = original.url().toString();

            if (fullUrl.toLowerCase().contains(bankSecurityUri.toLowerCase())) {
                log.info("[OkHttpClientFactoryImpl] Call url core bank");
                log.debug("[OkHttpClientFactoryImpl] AccessKeySecurity: {}", coreConfig.getAccessKeyCoreBankSecurity());

                requestBuilder
                        .header("Authorization", coreConfig.getAccessKeyCoreBankSecurity())
                        .header("Content-Type", "application/json");
            } else if (fullUrl.toLowerCase().contains(bankBaseUri.toLowerCase())) {
                log.info("[OkHttpClientFactoryImpl] Call url core bank");
                log.debug("[OkHttpClientFactoryImpl] AccessKeySecurity: {}", coreConfig.getAccessBaseKey());

                requestBuilder
                        .header("Authorization", coreConfig.getAccessBaseKey())
                        .header("Content-Type", "application/json");
            } else {
                //requestBuilder.header("Authorization", JwtUtil.getBearerToken()); // Hàm JwtUtil.getBearerToken() phát sinh lỗi
                requestBuilder.header("Content-Type", "application/json");
            }

            return chain.proceed(requestBuilder.build());
        });
        return builder;
    }
}
