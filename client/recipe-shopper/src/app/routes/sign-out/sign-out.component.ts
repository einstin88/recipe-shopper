import { Component, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { AuthActions } from 'src/app/flux/auth/auth.action';
import { State } from 'src/app/flux/reducers';
import { Subscription } from 'rxjs';

@Component({
  templateUrl: './sign-out.component.html',
  styleUrls: ['./sign-out.component.css'],
})
export class SignOutComponent implements OnInit {
  constructor(private store: Store<State>) {}

  sub$!: Subscription;

  ngOnInit(): void {
    this.store.dispatch(AuthActions.logout());
  }
}
