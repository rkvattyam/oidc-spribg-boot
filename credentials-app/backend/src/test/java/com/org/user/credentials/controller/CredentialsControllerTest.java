package com.org.user.credentials.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.org.user.credentials.entity.Credentials;
import com.org.user.credentials.entity.Organization;
import com.org.user.credentials.entity.User;
import com.org.user.credentials.exception.CredentialsAppException;
import com.org.user.credentials.service.CredentialService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CredentialsController.class)
public class CredentialsControllerTest {

    @MockitoBean
    private CredentialService credentialService;
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    JwtDecoder jwtDecoder;

    ObjectMapper mapper = new ObjectMapper();


    @Test
    @DisplayName("save user credentials")
    void saveApiCredentials() throws Exception {
        Jwt fakeJwt = Jwt.withTokenValue("fake-token")
                .header("alg", "none")
                .claim("sub", "test-user")
                .build();

        when(jwtDecoder.decode("valid-token")).thenReturn(fakeJwt);
        when(credentialService.createCredentialsIfNoneExist(anyLong(), anyLong(), anyString())).thenReturn(getCredentials());
        var payload = "{\n" +
                "  \"userId\": 19,\n" +
                "  \"organizationId\": 21,\n" +
                "  \"clientSecret\": \"super-secret-two\"\n" +
                "}";
        mockMvc.perform(post("/api/credentials")
                        .header("Authorization", "Bearer valid-token")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(payload)
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("save user credentials exception")
    void saveApiCredentialsError() throws Exception {
        Jwt fakeJwt = Jwt.withTokenValue("fake-token")
                .header("alg", "none")
                .claim("sub", "test-user")
                .build();

        when(jwtDecoder.decode("valid-token")).thenReturn(fakeJwt);
        when(credentialService.createCredentialsIfNoneExist(anyLong(), anyLong(), anyString()))
                .thenThrow(new CredentialsAppException("User not found"));
        var payload = "{\n" +
                "  \"userId\": 19,\n" +
                "  \"organizationId\": 21,\n" +
                "  \"clientSecret\": \"super-secret-two\"\n" +
                "}";
        mockMvc.perform(post("/api/credentials")
                        .header("Authorization", "Bearer valid-token")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(payload)
                ).andDo(print())
                .andExpect(status().isNotFound());
    }
    private Credentials getCredentials() throws JsonProcessingException {

        final var org = "{\n" +
                "\"id\": 22,\n" +
                "\"name\": \"GlobalSoft GmbH\",\n" +
                "\"vat\": \"VAT654321\",\n" +
                "\"sapId\": \"SAP002\"\n" +
                " }";
        final var user = "{\n" +
                "        \"email\": \"testeee52@example.com\",\n" +
                "            \"firstName\": \"fname2\",\n" +
                "            \"lastName\": \"lnam2e\",\n" +
                "        \"id\": 10\n" +
                "    }";
        final var credentials = new Credentials();
        credentials.setId(1L);
        credentials.setExpiryDate(LocalDateTime.now().plusDays(10));
        credentials.setCreationDate(LocalDateTime.now());
        credentials.setClientId("clientId");
        credentials.setOrganization(mapper.readValue(org, Organization.class));
        credentials.setCreatedBy(mapper.readValue(user, User.class));

        return credentials;
    }


}
