package com.orderservice.orderservice.intercept;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;

import java.io.IOException;
import java.util.Objects;

@Configuration
public class RestTemplateInterceptor implements ClientHttpRequestInterceptor {
    @Autowired
    private OAuth2AuthorizedClientManager oAuth2AuthorizedClientManager;
    public RestTemplateInterceptor(OAuth2AuthorizedClientManager oAuth2AuthorizedClientManager){
        this.oAuth2AuthorizedClientManager = oAuth2AuthorizedClientManager;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        request.getHeaders().add("Authorization", "Bearer " +
                Objects.requireNonNull(oAuth2AuthorizedClientManager.
                        authorize(OAuth2AuthorizeRequest.withClientRegistrationId("internal-client")
                                .principal("internal").build())).getAccessToken().getTokenValue());

        return execution.execute(request, body);
    }
}
