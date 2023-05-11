import { Component, OnDestroy, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { Subscription } from 'rxjs';
import { selectCurrentUser } from 'src/app/flux/auth/auth.selector';

@Component({
  selector: 'app-nav-bar',
  templateUrl: './nav-bar.component.html',
  styleUrls: ['./nav-bar.component.css'],
})
export class NavBarComponent implements OnInit, OnDestroy {
  constructor(private store: Store) {}

  currentUser!: string;
  isLoggedIn: boolean = false;

  sub$!: Subscription;

  ngOnInit(): void {
    this.sub$ = this.store.select(selectCurrentUser).subscribe((user) => {
      if (user) this.isLoggedIn = true;
      else this.isLoggedIn = false;

      this.currentUser = user;
    });
  }
  ngOnDestroy(): void {
    this.sub$.unsubscribe();
  }
}
