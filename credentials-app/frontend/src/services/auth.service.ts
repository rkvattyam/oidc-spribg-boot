import { Injectable } from '@angular/core';
import { OAuthService, AuthConfig, NullValidationHandler } from 'angular-oauth2-oidc';
import { environment } from '../environments/environment';

const authConfig: AuthConfig = {
  issuer: environment.oidc.issuer,
  redirectUri: environment.oidc.redirectUri,
  clientId: environment.oidc.clientId,
  responseType: 'code',
  scope: environment.oidc.scope,
  showDebugInformation: true,
  strictDiscoveryDocumentValidation: false,
  disablePKCE: false
};

@Injectable({ providedIn: 'root' })
export class AuthService {
  constructor(private oauthService: OAuthService) {
    //this.configure();
    this.oauthService.configure(authConfig);
    this.oauthService.loadDiscoveryDocumentAndTryLogin().then(() => {
  if (!oauthService.hasValidAccessToken()) {
    oauthService.initCodeFlow(); // triggers login
  }});
  }


  async loginCallback(): Promise<boolean> {
    const code = new URL(window.location.href).searchParams.get('code');
    if (!code) {
      console.error('No authorization code in URL');
      return false;
    }

    try {
    
      await this.oauthService.loadDiscoveryDocument();
      await this.oauthService.tryLoginCodeFlow();

      // Check token existence
      const accessToken = this.oauthService.getAccessToken();
      return !!accessToken;
    } catch (err) {
      console.error('Login callback failed', err);
      return false;
    }
  }

  async loginTest(): Promise<void> {
    await this.oauthService.loadDiscoveryDocumentAndTryLogin();
  }

  async finalizeLogin(): Promise<boolean> {
  await this.oauthService.loadDiscoveryDocument();
  await this.oauthService.tryLoginCodeFlow();

  if (!this.accessToken) {
    await new Promise(res => setTimeout(res, 500));
    await this.oauthService.tryLoginCodeFlow();
  }

  return !!this.accessToken;
}


  logout() { this.oauthService.logOut({ postLogoutRedirectUri: window.location.origin }); }

  get accessToken() { return this.oauthService.getAccessToken(); }
  get idToken() { return this.oauthService.getIdToken(); }
  get identityClaims() { return this.oauthService.getIdentityClaims(); }
  get isLoggedIn() { return this.oauthService.hasValidAccessToken(); }
 
  //get loadDiscoveryDocumentAndTryLogin() { return this.oauthService.loadDiscoveryDocumentAndTryLogin() }
}
