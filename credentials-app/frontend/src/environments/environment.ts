export const environment = {
  production: false,
  oidc: {
    issuer: 'http://localhost:9000',           // your mock OIDC provider
    clientId: 'angular-client',
    redirectUri: window.location.origin + '/callback',
    scope: 'openid profile email api',
    post_logout_redirect_uri: window.location.origin
  },
  apiBase: '/api/proxy', // proxied to Envoy / gateway
  apiBaseUrl : 'http://localhost:8080/api'
};
