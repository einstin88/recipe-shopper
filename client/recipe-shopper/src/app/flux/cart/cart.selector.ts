import { createFeatureSelector, createSelector } from '@ngrx/store';
import { CartState } from './cart.reducer';

export const cartSelector = createFeatureSelector<CartState>('cart');

export const selectCartItems = createSelector(
  cartSelector,
  (cartState) => cartState.cartItems
);

export const selectCartTotal = createSelector(
  cartSelector,
  (cartState) => cartState.total
);
