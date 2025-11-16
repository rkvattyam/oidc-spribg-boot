package com.org.user.credentials.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.org.user.credentials.dto.UserOrganizationsResponse;
import com.org.user.credentials.service.OrganizationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(OrganizationController.class)
public class OrganizationControllerTest {

    @MockitoBean
    private OrganizationService organizationService;
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    JwtDecoder jwtDecoder;

    ObjectMapper mapper = new ObjectMapper();

    @Test
    @DisplayName("get new user organization")
    void getUserOrganizations() throws Exception {
        Jwt fakeJwt = Jwt.withTokenValue("fake-token")
                .header("alg", "none")
                .claim("sub", "test-user")
                .build();

        when(jwtDecoder.decode("valid-token")).thenReturn(fakeJwt);
        when(organizationService.getOrganizationsByUsername(anyString())).thenReturn(getNewUserOrganisationResponse());
        mockMvc.perform(get("/api/organizations/user/{email}", "testUser@xyz.com")
                        .header("Authorization", "Bearer valid-token")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("get existing user Organization")
    void getExistingUserOrganizations() throws Exception {
        Jwt fakeJwt = Jwt.withTokenValue("fake-token")
                .header("alg", "none")
                .claim("sub", "test-user")
                .build();

        when(jwtDecoder.decode("valid-token")).thenReturn(fakeJwt);
        when(organizationService.getOrganizationsByUsername(anyString())).thenReturn(getExistingUser());
        mockMvc.perform(get("/api/organizations/user/{email}", "johnDoe@xyz.com")
                        .header("Authorization", "Bearer valid-token")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                ).andDo(print())
                .andExpect(status().isOk());
    }

    private UserOrganizationsResponse getNewUserOrganisationResponse () throws JsonProcessingException {
        final var newUser = "{\"userId\":null,\"organizations\":[{\"id\":21,\"name\":\"TechNova Ltd\",\"vat\":\"VAT123456\",\"sapId\":\"SAP001\"},{\"id\":22,\"name\":\"GlobalSoft GmbH\",\"vat\":\"VAT654321\",\"sapId\":\"SAP002\"}]}\n";
        return mapper.readValue(newUser, UserOrganizationsResponse.class);
    }

    private UserOrganizationsResponse getExistingUser() throws JsonProcessingException {
        final var existingUser = "{\"userId\":20,\"organizations\":[{\"id\":21,\"name\":\"TechNova Ltd\",\"vat\":\"VAT123456\",\"sapId\":\"SAP001\"}]}";
        return mapper.readValue(existingUser, UserOrganizationsResponse.class);
    }
}
