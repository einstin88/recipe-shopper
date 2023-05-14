import {
  HttpClient,
  HttpErrorResponse,
  HttpHeaders,
  HttpParams,
} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { EP_REGISTER_USER, EP_SIGN_IN_USER, EP_SIGN_OUT } from '../utils/urls';
import { User } from '../model/user.model';
import { AuthPayLoad } from '../model/authentication-payload.model';
import { JWT } from '../model/jwt.model';
import { firstValueFrom } from 'rxjs';
import { Store } from '@ngrx/store';
import { AuthActions } from '../flux/auth/auth.action';
import { selectCurrentUser } from '../flux/auth/auth.selector';

@Injectable({
  providedIn: 'root',
})
export class AuthDataService {
  constructor(private http: HttpClient, private store: Store) {}

  registerUser(newUser: User) {
    return firstValueFrom(this.http.post<JWT>(EP_REGISTER_USER, newUser))
      .then((jwt) => {
        console.info('>>> Token: ', jwt);
        this.store.dispatch(AuthActions.registrationSuccess({ jwt }));
      })
      .catch((error: HttpErrorResponse) => {
        console.error('--- Registration error: ', error.message);

        this.store.dispatch(AuthActions.registrationFailure());
        throw new Error(`${error.status}: Registration failed!`);
      });
  }

  loginUser(credentials: AuthPayLoad) {
    const payload = new HttpParams().appendAll({ ...credentials });

    const headers = new HttpHeaders().set(
      'Content-Type',
      'application/x-www-form-urlencoded'
    );

    return firstValueFrom(
      this.http.post<JWT>(EP_SIGN_IN_USER, payload.toString(), { headers })
    )
      .then((jwt) => {
        console.debug('>>> Token: ', jwt);
        this.store.dispatch(AuthActions.loginSuccess({ jwt }));
      })
      .catch((error: HttpErrorResponse) => {
        console.error('--- Login error: ', error.message);

        this.store.dispatch(AuthActions.loginFailure());
        throw new Error(`${error.status}: Bad response from server`);
      });
  }

  logoutUser(currentUser: string) {
    const url = `${EP_SIGN_OUT}/${currentUser}`;

    return this.http.get<void>(url);
  }
}
