import { createActionGroup, emptyProps, props } from '@ngrx/store';
import { CartItem } from 'src/app/model/cart-item.model';

export const CartActions = createActionGroup({
  source: 'Shopping Cart',
  events: {
    'Add to Cart': props<{ cartItem: CartItem }>(),
    'Remove from Cart': props<{ index: number }>(),
    'Update Cart': props<{ cartItems: CartItem[] }>(),
    'Empty Cart': emptyProps(),
  },
});
