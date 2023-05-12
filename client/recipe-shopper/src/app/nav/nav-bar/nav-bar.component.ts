import { Component, OnDestroy, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { Subscription } from 'rxjs';
import { selectCurrentUser } from 'src/app/flux/auth/auth.selector';
import { selectCartItems } from 'src/app/flux/cart/cart.selector';

@Component({
  selector: 'app-nav-bar',
  templateUrl: './nav-bar.component.html',
  styleUrls: ['./nav-bar.component.css'],
})
export class NavBarComponent implements OnInit, OnDestroy {
  constructor(private store: Store) {}

  cartCount!: number;
  currentUser!: string;
  isLoggedIn: boolean = false;

  sub$!: Subscription;
  sub1$!: Subscription;

  ngOnInit(): void {
    this.sub$ = this.store.select(selectCurrentUser).subscribe((user) => {
      if (user) this.isLoggedIn = true;
      else this.isLoggedIn = false;

      this.currentUser = user;
    });

    this.sub1$ = this.store
      .select(selectCartItems)
      .subscribe((items) => (this.cartCount = items.length));
  }
  ngOnDestroy(): void {
    this.sub$.unsubscribe();
    this.sub1$.unsubscribe();
  }
}
