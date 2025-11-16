interface Window {
  env: {
    KEYCLOAK_ISSUER?: string;
    CLIENT_ID?: string;
    [key: string]: any; // optional for extra runtime vars
  };
}
