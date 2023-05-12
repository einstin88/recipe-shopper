import { isDevMode } from '@angular/core';
import { ActionReducerMap, MetaReducer } from '@ngrx/store';
import {
  AuthReducers,
  AuthState,
  authInitialState,
} from '../auth/auth.reducer';
import { HydrationMetaReducer } from '../store-persist-hydrate/hydrate.meta.reducer';
import { loggingMetaReducer } from './logging.meta.reducer';
import {
  CartReducers,
  CartState,
  cartInitialState,
} from '../cart/cart.reducer';

export interface State {
  auth: AuthState;
  cart: CartState;
}

export const initialState: State = {
  auth: authInitialState,
  cart: cartInitialState,
};

export const reducers: ActionReducerMap<State> = {
  auth: AuthReducers,
  cart: CartReducers,
};

export const metaReducers: MetaReducer<State>[] = isDevMode()
  ? [HydrationMetaReducer, loggingMetaReducer]
  : [HydrationMetaReducer];
