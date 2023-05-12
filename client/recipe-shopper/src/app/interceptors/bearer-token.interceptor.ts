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

@Injectable()
export class BearerTokenInterceptor implements HttpInterceptor {
  constructor(private store: Store) {
    store.select(selectJwt).subscribe((token) => {
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
