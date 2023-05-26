import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
} from '@angular/common/http';
import { Observable } from 'rxjs';
import { Store } from '@ngrx/store';
import { selectJwt } from '../flux/auth/auth.selector';
import { decodeJwt } from 'jose';
import { AuthActions } from '../flux/auth/auth.action';
import { ToastNotificationService } from '../services/toast-notification.service';

@Injectable()
export class BearerTokenInterceptor implements HttpInterceptor {
  constructor(
    private store: Store,
    private toastSvc: ToastNotificationService
  ) {
    store.select(selectJwt).subscribe((token) => {
      if (token) {
        const decodedToken = decodeJwt(token);

        const expiry = decodedToken.exp!;
        console.debug('Time difference: ', expiry * 1000 - Date.now());

        if (expiry * 1000 - Date.now() < 0) {
          store.dispatch(AuthActions.logoutSuccess());
          toastSvc.show('Session Expired', 'Please sign in again.');
          return;
        }

        if (expiry * 1000 - Date.now() <= 600000)
          toastSvc.show(
            'Session expiring soon',
            'Less than 10 mins remaining in your current session. After that you will be automatically logged out.'
          );
      }

      this.jwt = token;
    });
  }

  jwt!: string;

  intercept(
    request: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    if (this.jwt) {
      const setHeaders = { Authorization: `Bearer ${this.jwt}` };
      const authRequest = request.clone({ setHeaders });

      return next.handle(authRequest);
    }

    return next.handle(request);
  }
}
