import { Injectable } from '@angular/core';
import Keycloak from 'keycloak-js';
import { environment } from '../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class KeycloakService {
 
  private keycloak: Keycloak;

  constructor() {
    this.keycloak = new Keycloak({
      url: environment.oidc.keycloak_url,
      realm: 'myrealm',
      clientId: 'angular-client',
    });
  }

//   init(): Promise<boolean> {
//     return this.keycloak.init({
//       onLoad: 'login-required',
//       checkLoginIframe: false,
//       enableLogging: true,
//       pkceMethod: 'S256',
//       flow: 'standard',
//     });
//   }
 init(): Promise<boolean> {
    return this.keycloak.init({
      onLoad: 'check-sso', 
      checkLoginIframe: false
    });
  }

  login(): void {
    this.keycloak.login({
      redirectUri: environment.oidc.redirectUri //window.location.origin
    });
  }

  getToken(): string | undefined {
    return this.keycloak.token;
  }

  getIdToken(): string | undefined {
    return this.keycloak.idToken;
  }

  getIdentityClaims(): any | null {
    return this.keycloak.idTokenParsed ?? null;
  }

   isLoggedIn(): boolean {
    return this.keycloak.authenticated === true;
  }

  logout(): void {
    this.keycloak.logout({ redirectUri: window.location.origin });
  }
}
