import { isDevMode } from '@angular/core';
import { ActionReducerMap, MetaReducer } from '@ngrx/store';
import { AuthReducers, AuthState, authInitialState } from '../auth/auth.reducer';
import { HydrationMetaReducer } from '../store-persist-hydrate/hydrate.meta.reducer';
import { loggingMetaReducer } from './logging.meta.reducer';

export interface State {
  auth: AuthState;
}

export const initialState = {
  auth: authInitialState,
};

export const reducers: ActionReducerMap<State> = {
  auth: AuthReducers,
};

export const metaReducers: MetaReducer<State>[] = isDevMode()
  ? [HydrationMetaReducer, loggingMetaReducer]
  : [HydrationMetaReducer];
