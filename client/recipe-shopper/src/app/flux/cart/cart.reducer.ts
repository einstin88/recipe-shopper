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

    // Get the subtotal to add to the cart's total
    const itemTotal = cartItem.ingredients
      .map((item) => item.total!)
      .reduce((acc, val) => acc + val, 0);

    console.debug(itemTotal);

    return { ...state, cartItems: newCart, total: state.total + itemTotal };
  }),
  on(CartActions.removeFromCart, (state, { index }) => {
    const newCart = [...state.cartItems];

    // Get the subtotal of the removed recipe  
    const totalToRemove = newCart
      .splice(index, 1)[0]
      .ingredients.map((item) => item.total!)
      .reduce((acc, val) => acc + val, 0);

    return { ...state, cartItems: newCart, total: state.total - totalToRemove };
  }),
  on(CartActions.emptyCart, (state) => {
    return { ...state, ...cartInitialState };
  })
);
