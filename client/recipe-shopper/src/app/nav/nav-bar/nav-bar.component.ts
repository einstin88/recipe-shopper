import { Component } from '@angular/core';
import { Store } from '@ngrx/store';
import {
  selectCurrentUser,
  selectJwt,
} from 'src/app/flux/selectors/auth.selector';

@Component({
  selector: 'app-nav-bar',
  templateUrl: './nav-bar.component.html',
  styleUrls: ['./nav-bar.component.css'],
})
export class NavBarComponent {
  constructor(private store: Store) {}

  currentUser$ = this.store.select(selectCurrentUser);
}
