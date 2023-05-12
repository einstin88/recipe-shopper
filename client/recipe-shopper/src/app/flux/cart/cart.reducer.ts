import { createReducer, on } from '@ngrx/store';
import { CartItem } from 'src/app/model/cart-item.model';
import { CartActions } from './cart.action';

export interface CartState {
  cartItems: CartItem[];
  total: number;
}

export const cartInitialState: CartState = {
  cartItems: [],
  total: 0,
};

export const CartReducers = createReducer(
  cartInitialState,
  on(CartActions.addToCart, (state, { cartItem }) => {
    const newCart = [...state.cartItems, cartItem];

    const itemTotal = cartItem.ingredients
      .map((item) => item.total!)
      .reduce((acc, val) => acc + val, 0);

    console.log(itemTotal);

    return { ...state, cartItems: newCart, total: state.total + itemTotal };
  }),
  on(CartActions.removeFromCart, (state, { index }) => {
    const newCart = [...state.cartItems];

    const newTotal = newCart
      .splice(index, 1)[0]
      .ingredients.map((item) => item.total!)
      .reduce((acc, val) => acc + val, 0);
    return { ...state, cartItems: newCart, total: state.total - newTotal };
  }),
  on(CartActions.emptyCart, (state) => {
    return { ...state, ...cartInitialState };
  })
);
