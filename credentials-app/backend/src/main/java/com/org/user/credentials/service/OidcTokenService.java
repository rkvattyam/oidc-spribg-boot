package com.org.user.credentials.service;

import com.org.user.credentials.exception.OidcTokenException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@Profile("integration")
public class OidcTokenService {

    private final RestTemplate restTemplate;
    private final String tokenUrl;
    private final String clientId;
    private final String clientSecret;

    public OidcTokenService(
            @Value("${keycloak.token-uri}") String tokenUrl,
            @Value("${keycloak.client-id}") String clientId,
            @Value("${keycloak.client-secret}") String clientSecret
    ) {
        this.restTemplate = new RestTemplate();
        this.tokenUrl = tokenUrl;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    public String getToken(String username, String password) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "password");
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("username", username);
        params.add("password", password);
        params.add("scope", "openid profile email");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        try {
            ResponseEntity<Map> response =
                    restTemplate.postForEntity(tokenUrl, request, Map.class);
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new OidcTokenException(
                        "Failed to retrieve token from OIDC provider: ",
                        response.getStatusCode().value()
                );
            }

            Map<String, Object> body = response.getBody();

            if (!body.containsKey("access_token")) {
                throw new OidcTokenException("No access_token in OIDC response", HttpStatus.UNAUTHORIZED.value());
            }

            return body.get("access_token").toString();
        } catch (HttpClientErrorException e) {
           throw new OidcTokenException("error while connecting to ouath client end point" + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }


    }
}

