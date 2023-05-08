import { createFeatureSelector, createSelector } from '@ngrx/store';
import { AuthState } from '../reducers/auth.reducer';

export const authSelector = createFeatureSelector<AuthState>('auth');

export const selectJwt = createSelector(
  authSelector,
  (state) => state.jwt.token
);

export const selectCurrentUser = createSelector(
  authSelector,
  (state) => state.currentUser
);
