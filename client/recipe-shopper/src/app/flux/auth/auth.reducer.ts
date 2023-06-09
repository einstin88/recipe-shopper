import { createReducer, on } from '@ngrx/store';
import { JWT } from 'src/app/model/jwt.model';
import { AuthActions } from './auth.action';
import { decodeJwt } from 'jose';

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
      const currentUser = decodeJwt(jwt.token).sub!;
      console.debug('>>> Decoded subject: ', currentUser);

      return { ...state, jwt, currentUser };
    }
  ),
  on(
    AuthActions.loginFailure,
    AuthActions.registrationFailure,
    AuthActions.logoutSuccess,
    (state) => ({
      ...state,
      ...authInitialState,
    })
  )
);
