import 'zone.js';
import { bootstrapApplication } from '@angular/platform-browser';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { provideRouter } from '@angular/router';
import { AppComponent } from './app/app.component';
import { routes } from './app/app.routes';
import {  authInterceptor } from './interceptor/auth.interceptor';

import { APP_INITIALIZER, importProvidersFrom } from '@angular/core';
import { KeycloakService } from './services/keycloak.service';

// Keycloak OIDC configuration

function initializeKeycloak(keycloak: KeycloakService) {
  return () => keycloak.init();
}

bootstrapApplication(AppComponent, {
  providers: [
     provideRouter(routes),
    provideHttpClient(withInterceptors([authInterceptor])),
   // importProvidersFrom(OAuthModule.forRoot()),
    // provideAuth({
    //   config: {
    //     authority: environment.oidc.issuer,
    //     redirectUrl: environment.oidc.redirectUri,
    //     postLogoutRedirectUri: window.location.origin,
    //     clientId: environment.oidc.clientId,
    //     responseType: 'code',
    //     scope: environment.oidc.scope,
    //     silentRenew: true,
    //     useRefreshToken: true,
    //     secureRoutes: []
    //   },
    // }),
    KeycloakService,
    {
      provide: APP_INITIALIZER,
      useFactory: initializeKeycloak,
      deps: [KeycloakService],
      multi: true,
    },
  ],
}).catch(err => console.error(err));

