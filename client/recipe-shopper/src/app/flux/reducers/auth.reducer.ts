import { createReducer, on } from '@ngrx/store';
import { JWT } from 'src/app/model/jwt.model';
import { AuthActions } from '../actions/auth.action';

export interface AuthState {
  jwt: JWT;
  currentUser: string;
}

const initialState = {
  jwt: {
    token: '',
  },
  currentUser: '',
};

export const AuthReducers = createReducer(
  initialState,
  on(
    AuthActions.loginSuccess,
    AuthActions.registrationSuccess,
    (state, { jwt }) => ({ ...state, jwt })
  ),
  on(AuthActions.loginFailure, AuthActions.registrationFailure, (state) => ({
    ...state,
    jwt: initialState.jwt,
  }))
);
