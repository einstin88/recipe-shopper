import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Store } from '@ngrx/store';
import { AuthActions } from 'src/app/flux/actions/auth.action';
import { State } from 'src/app/flux/reducers';
import { selectCurrentUser } from 'src/app/flux/selectors/auth.selector';

@Component({
  templateUrl: './sign-out.component.html',
  styleUrls: ['./sign-out.component.css'],
})
export class SignOutComponent implements OnInit {
  constructor(private store: Store<State>, private router: Router) {}

  ngOnInit(): void {
    this.store.dispatch(AuthActions.logout());

    if (this.store.select(selectCurrentUser)) this.router.navigate(['/login']);
  }
}
