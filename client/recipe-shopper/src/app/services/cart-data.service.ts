import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Store } from '@ngrx/store';
import { State } from '../flux/reducers';
import { selectCurrentUser } from '../flux/auth/auth.selector';
import { firstValueFrom } from 'rxjs';
import { EP_CART_CHECKOUT } from '../utils/urls';
import { CartItem } from '../model/cart-item.model';

@Injectable({
  providedIn: 'root',
})
export class CartDataService {
  constructor(private http: HttpClient, private store: Store<State>) {
    store
      .select(selectCurrentUser)
      .subscribe((user) => (this.currentUser = user));
  }

  private currentUser!: string;

  checkOut(cartItems: CartItem[], total: number) {
    const payload = {
      username: this.currentUser,
      cartItems,
      total,
    };

    console.log(payload);
    
    return firstValueFrom(
      this.http.post<void>(EP_CART_CHECKOUT, payload)
    ).catch((error: HttpErrorResponse) => {
      console.error(`--- Checkout Error: ${error.message}`);

      throw new Error(`${error.status}: ${error.statusText}`);
    });
  }
}
