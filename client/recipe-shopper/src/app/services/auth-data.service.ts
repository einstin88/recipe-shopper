import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { EP_REGISTER_USER, EP_SIGN_IN_USER } from '../utils/urls';
import { User } from '../model/user.model';
import { AuthPayLoad } from '../model/authentication-payload.model';
import { JWT } from '../model/jwt.model';
import { firstValueFrom, tap } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AuthDataService {
  constructor(private http: HttpClient) {}

  jwt!: string;

  registerUser(newUser: User) {
    firstValueFrom(
      this.http.post<JWT>(EP_REGISTER_USER, newUser).pipe(
        tap((token) => {
          this.jwt = token.token;
          console.debug('>>> Token: ', this.jwt);
        })
      )
    ).catch((error) => console.error('--- Login error: ', error));
  }

  loginUser(credentials: AuthPayLoad) {
    const payload = new HttpParams().appendAll({ ...credentials });

    const headers = new HttpHeaders().set(
      'Content-Type',
      'application/x-www-form-urlencoded'
    );

    firstValueFrom(
      this.http
        .post<JWT>(EP_SIGN_IN_USER, payload.toString(), { headers })
        .pipe(
          tap((token) => {
            this.jwt = token.token;
            console.debug('>>> Token: ', this.jwt);
          })
        )
    ).catch((error) => console.error('--- Login error: ', error));
  }
}
