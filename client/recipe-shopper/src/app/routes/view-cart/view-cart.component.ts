import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Store } from '@ngrx/store';
import { Subscription } from 'rxjs';
import { CartActions } from 'src/app/flux/cart/cart.action';
import { cartSelector } from 'src/app/flux/cart/cart.selector';
import { State } from 'src/app/flux/reducers';
import { CartItem } from 'src/app/model/cart-item.model';
import { CartDataService } from 'src/app/services/cart-data.service';
import { ToastNotificationService } from 'src/app/services/toast-notification.service';

/**
 * @description A component associated with a router path for viewing items in the cart
 */
@Component({
  templateUrl: './view-cart.component.html',
  styleUrls: ['./view-cart.component.css'],
})
export class ViewCartComponent implements OnInit, OnDestroy {
  constructor(
    private store: Store<State>,
    private svc: CartDataService,
    private toastSvc: ToastNotificationService
  ) {}

  cartItems!: CartItem[];
  cartTotal!: number;

  errMsg!: string;

  sub$!: Subscription;

  ngOnInit(): void {
    this.sub$ = this.store.select(cartSelector).subscribe((cartState) => {
      this.cartItems = cartState.cartItems;
      this.cartTotal = cartState.total;
    });

    // this.sub2$ = this.store.select(selectCartTotal).
  }

  ngOnDestroy(): void {
    this.sub$.unsubscribe();
  }

  checkOut() {
    this.svc
      .checkOut(this.cartItems, this.cartTotal)
      .then(() => {
        this.emptyCart();
        this.toastSvc.show(
          'Email Summary',
          'A summary of your cart has been sent to your registered e-mail.'
        );
      })
      .catch((err: Error) => (this.errMsg = err.message));
  }

  emptyCart() {
    this.store.dispatch(CartActions.emptyCart());
  }

  removeItem(index: number) {
    console.debug(index);
    this.store.dispatch(CartActions.removeFromCart({ index }));
  }
}
