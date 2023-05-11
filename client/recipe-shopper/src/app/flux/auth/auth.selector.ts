import { createFeatureSelector, createSelector } from '@ngrx/store';
import { AuthState } from './auth.reducer';

export const authSelector = createFeatureSelector<AuthState>('auth');

export const selectJwt = createSelector(
  authSelector,
  (authState) => authState.jwt.token
);

export const selectCurrentUser = createSelector(
  authSelector,
  (authState) => authState.currentUser
);
