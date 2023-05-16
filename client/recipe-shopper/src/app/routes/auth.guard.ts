import { inject } from '@angular/core';
import { CanActivateFn, Params, Router, UrlTree } from '@angular/router';
import { Store } from '@ngrx/store';
import { selectJwt } from '../flux/auth/auth.selector';
import { State } from '../flux/reducers';
import { Observable, map } from 'rxjs';

export const AuthGuard: CanActivateFn = (
  _,
  routerState
): Observable<boolean | UrlTree> => {
  const router = inject(Router);

  return inject(Store<State>)
    .select(selectJwt)
    .pipe(
      map((token) => {
        if (token) return true;

        const queryParams: Params = { redirect: routerState.url };
        return router.createUrlTree(['/login'], { queryParams });
      })
    );
};
