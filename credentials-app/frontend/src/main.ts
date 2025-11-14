import 'zone.js';
import { bootstrapApplication } from '@angular/platform-browser';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { provideRouter } from '@angular/router';
import { AppComponent } from './app/app.component';
import { routes } from './app/app.routes';
import {  authInterceptor } from './interceptor/auth.interceptor';
import { OAuthModule } from 'angular-oauth2-oidc';
import { importProvidersFrom, Injector } from '@angular/core';

bootstrapApplication(AppComponent, {
  providers: [
     provideRouter(routes),
    provideHttpClient(withInterceptors([authInterceptor])),
    importProvidersFrom(OAuthModule.forRoot()),
  ],
}).catch(err => console.error(err));

