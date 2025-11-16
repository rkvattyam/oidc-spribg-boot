package com.org.user.credentials.integration;

import com.org.user.credentials.CredentialsAppApplication;
import com.org.user.credentials.dto.CredentialResponseDto;
import com.org.user.credentials.dto.UserOrganizationsResponse;
import com.org.user.credentials.dto.UserResponse;
import com.org.user.credentials.service.OidcTokenService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = CredentialsAppApplication.class)
@ActiveProfiles("integration")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ApiIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private OidcTokenService oidcTokenService;

    private HttpHeaders headers;

    private long userId;
    private long orgId;
    private String clientId;
    private String email;

    @Value ("${integration.userName}")
    private String userName;

    @BeforeEach
    void setup() {
        final var token = oidcTokenService.getToken(userName, "password");

        headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    @Test
    @DisplayName("get a new user with an organisation")
    @Order(1)
    void testGetUserEntity() {

        //NewUser when logged in will only  get organizations

        HttpEntity<Void> getRequest = new HttpEntity<>(headers);
        ResponseEntity<UserOrganizationsResponse> getResponse = restTemplate.exchange("/api/organizations/user/" + userName, HttpMethod.GET, getRequest, UserOrganizationsResponse.class);

        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertNull(getResponse.getBody().userId());
        assertEquals(3, getResponse.getBody().organizations().size());
    }

    @Test
    @DisplayName("retrieve user details")
    @Order(2)
    void testGetUserDetails() {
        // First, create an user
        String requestBody = """
                {
                    "email":"user10@example.com","firstName":"first10","lastName":"last10","organizations":[{"id":21,"name":"TechNova Ltd"}, {"id":22,"name":"GlobalSoft GmbH"}]
                }
                """;
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
        ResponseEntity<UserResponse> createResponse = restTemplate.postForEntity("/api/users", request, UserResponse.class);
        assertNotNull(createResponse);
        assertEquals(HttpStatus.OK, createResponse.getStatusCode());
        email = createResponse.getBody().email();

        // Now GET
        HttpEntity<Void> getRequest = new HttpEntity<>(headers);
        ResponseEntity<UserOrganizationsResponse> getResponse = restTemplate.exchange("/api/organizations/user/" + email, HttpMethod.GET, getRequest, UserOrganizationsResponse.class);

        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertNotNull(getResponse.getBody().userId());
        userId = getResponse.getBody().userId();
        orgId = getResponse.getBody().organizations().get(0).id();

    }


    @Test
    @DisplayName("retrieve no credentials for the user")
    @Order(3)
    void testGetUserWithNoCredentials() {

        HttpEntity<Void> getRequest = new HttpEntity<>(headers);
        final var url = "/api/credentials/user?email=" + email;
        ResponseEntity<List<CredentialResponseDto>> getResponse = restTemplate.exchange(url, HttpMethod.GET, getRequest, new ParameterizedTypeReference<>() {
        });

        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertNotNull(getResponse.getBody());
    }

    //Since get user
    @Test
    @DisplayName("save api credential details")
    @Order(4)
    void testSaveCredentials() {
        String requestBody = """
                  {
                    "userId": %d,
                    "organizationId": %d,
                    "clientSecret": "EditedSecret"
                  }
                """.formatted(userId, orgId);
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
        ResponseEntity<CredentialResponseDto> response = restTemplate.postForEntity("/api/credentials", request, CredentialResponseDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().id());
        assertEquals(orgId, response.getBody().organizationId());
        clientId = response.getBody().clientId();

    }

    @Test
    @DisplayName("retrieve credentials for the user")
    @Order(5)
    void testGetUserCredentials() {

        HttpEntity<Void> getRequest = new HttpEntity<>(headers);
        final var url = "/api/credentials/user?email=" + email;
        ResponseEntity<List<CredentialResponseDto>> getResponse = restTemplate.exchange(url, HttpMethod.GET, getRequest, new ParameterizedTypeReference<>() {
        });

        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertNotNull(getResponse.getBody());
    }

    @Test
    @DisplayName("update Client Secret")
    @Order(6)
    void testUpdateEntity() {

        // Update entity
        String updateBody = """
                {    "newSecret": "super-secret-update" }
                """;
        HttpEntity<String> updateRequest = new HttpEntity<>(updateBody, headers);
        final var url = "/api/credentials/" + clientId + "/secret";
        ResponseEntity<String> updateResponse = restTemplate.exchange(url, HttpMethod.PUT, updateRequest, String.class);

        assertEquals(HttpStatus.OK, updateResponse.getStatusCode());
        assertEquals(clientId, updateResponse.getBody());
    }

    @Test
    @DisplayName("delete api credential")
    @Order(7)
    void deleteApiCredential() {
        HttpEntity<Void> deleteRequest = new HttpEntity<>(headers);
        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
                "/api/credentials/" + clientId, HttpMethod.DELETE, deleteRequest, Void.class);
        assertTrue(
                deleteResponse.getStatusCode() == HttpStatus.NO_CONTENT ||
                        deleteResponse.getStatusCode() == HttpStatus.OK
        );
    }

    @Test
    @DisplayName("test with no invalid/expired bearer token")
    void testUnauthorizedAccess() {
        HttpHeaders noAuthHeaders = new HttpHeaders();
        noAuthHeaders.setContentType(MediaType.APPLICATION_JSON);
        var email = "testUser@xyz.com"; //needs to be removed
        HttpEntity<String> request = new HttpEntity<>(noAuthHeaders);
        ResponseEntity<Map> response = restTemplate.exchange("/api/organizations/user" + email, HttpMethod.GET, request, Map.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
}

