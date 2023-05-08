import { isDevMode } from '@angular/core';
import {
  ActionReducerMap,
  MetaReducer,
} from '@ngrx/store';
import { AuthReducers, AuthState } from './auth.reducer';

export interface State {
  auth: AuthState;
}

export const reducers: ActionReducerMap<State> = {
  auth: AuthReducers,
};

export const metaReducers: MetaReducer<State>[] = isDevMode() ? [] : [];
