import { Injector, inject } from '@angular/core';
import { OAuthService } from 'angular-oauth2-oidc';
import { HttpInterceptorFn, HttpRequest, HttpHandlerFn } from '@angular/common/http';
import { environment } from '../environments/environment';

// We will store the injector globally to avoid eager resolution
let injectorRef: Injector;

export const provideAuthInterceptorInjector = (injector: Injector) => {
  injectorRef = injector;
};

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const auth = inject(OAuthService);

  const token = auth.getIdToken();

  // Only attach the token for backend API calls (not OIDC endpoints)
  if (token && req.url.startsWith(environment.apiBaseUrl)) {
    req = req.clone({
      setHeaders: { Authorization: `Bearer ${token}` },
    });
  }

  return next(req);
};
