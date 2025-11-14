// index.js
import { Provider } from 'oidc-provider';

const ISSUER = 'http://localhost:9000';

const clients = [
  {
    client_id: 'angular-client',
    post_logout_redirect_uris: ['http://localhost:4200'],  // For logout redirect
    redirect_uris: ['http://localhost:4200/callback'],
    grant_types: ['authorization_code'],
    response_types: ['code'],
    token_endpoint_auth_method: 'none',
    require_pushed_authorization_requests: false,
    require_pkce: 'S256'
  },
];

const configuration = {
  clients,
  scopes: ['openid', 'profile', 'email', 'api'],
  features: {
    devInteractions: { enabled: true },
    revocation: { enabled: true },
    introspection: { enabled: true },
    rpInitiatedLogout: { enabled: true }
  },
  formats: {
    AccessToken: 'jwt',
  },
  claims: {
    openid: ['sub'],
     profile: ['name', 'family_name', 'given_name'],
     email: ['email', 'email_verified']
  },
  ttl: {
    AccessToken: 3600,  // 1 hour
    IdToken: 3600,
    RefreshToken: 86400, // 1 day
  }
};

const provider = new Provider(ISSUER, configuration);

provider.listen(9000, () => {
  console.log(`✅ Mock OIDC provider listening on ${ISSUER}`);
  console.log(`🔍 Discovery: ${ISSUER}/.well-known/openid-configuration`);
});
