import { Injectable } from '@angular/core';
import { Actions, concatLatestFrom, createEffect, ofType } from '@ngrx/effects';
import { AuthActions } from './auth.action';
import { catchError, exhaustMap, map, of } from 'rxjs';
import { AuthDataService } from 'src/app/services/auth-data.service';
import { State } from '../reducers';
import { Store } from '@ngrx/store';
import { selectCurrentUser } from './auth.selector';
import { CartActions } from '../cart/cart.action';
import { Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';

@Injectable()
export class AuthEffects {
  constructor(
    private action$: Actions,
    private store: Store<State>,
    private authSvc: AuthDataService,
    private router: Router
  ) {}

  /**
   * Filter and handle the logout action trigger
   */
  logout$ = createEffect(() => {
    return this.action$.pipe(
      ofType(AuthActions.logout),
      // Add current user to the stream. https://ngrx.io/guide/effects#incorporating-state
      concatLatestFrom(() => this.store.select(selectCurrentUser)),
      // Effects are built on top of Observables. https://ngrx.io/guide/effects#handling-errors
      // Exhaustmap 'flat maps' the combination of return values to an Observable only if the previous Observable has completed
      exhaustMap(([, user]) => {
        return this.authSvc.logoutUser(user).pipe(
          map(() => AuthActions.logoutSuccess()),
          catchError((err: HttpErrorResponse) => {
            // Hacky way to bypass expired token for now
            console.log('logout ran here')
            if (err.status == 500 || err.status == 403)
              return of(AuthActions.logoutSuccess());

            return of(AuthActions.logoutFailure());
          })
        );
      })
    );
  });

  /**
   *   Handle side effects upon logout success (aka clean up actions)
   *
   * - Clear storage of current user's artifacts
   */
  logoutSuccess$ = createEffect(() => {
    return this.action$.pipe(
      ofType(AuthActions.logoutSuccess),
      map(() => {
        this.router.navigate(['/']);
        return CartActions.emptyCart();
      })
    );
  });
}
