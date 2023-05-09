import { createReducer, on } from '@ngrx/store';
import { JWT } from 'src/app/model/jwt.model';
import { AuthActions } from '../actions/auth.action';
import jwtDecode from 'jwt-decode';
import { JwtPayload } from 'src/app/model/jwt-payload.model';

export interface AuthState {
  jwt: JWT;
  currentUser: string;
}

export const authInitialState = {
  jwt: {
    token: '',
  },
  currentUser: '',
};

export const AuthReducers = createReducer(
  authInitialState,
  on(
    AuthActions.loginSuccess,
    AuthActions.registrationSuccess,
    (state, { jwt }) => {
      const decodedToken: JwtPayload = jwtDecode(jwt.token);
      console.debug('>>> Decoded sub: ', decodedToken.sub);

      return { ...state, jwt, currentUser: decodedToken.sub };
    }
  ),
  on(
    AuthActions.loginFailure,
    AuthActions.registrationFailure,
    AuthActions.logout,
    (state) => ({
      ...state,
      ...authInitialState,
    })
  )
);
