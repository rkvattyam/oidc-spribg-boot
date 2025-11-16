export const environment = {
  production: false,
  oidc: {
    issuer: 'http://localhost:8081/realms/myrealm',
    clientId: 'angular-client',
    redirectUri: window.location.origin + '/callback',
    scope: 'openid profile email',
    post_logout_redirect_uri: window.location.origin,
    keycloak_url: 'http://localhost:8081'
  },
  apiBase: '/envoy/proxy:9090',
  apiBaseUrl : 'http://localhost:8080/api'
};
