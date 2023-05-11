import { inject } from '@angular/core';
import { CanMatchFn } from '@angular/router';
import { Store } from '@ngrx/store';
import { Observable, map } from 'rxjs';
import { State } from '../flux/reducers';
import { selectJwt } from '../flux/auth/auth.selector';

export const AnonymousGuard: CanMatchFn = (_, __): Observable<boolean> => {
  return inject(Store<State>)
    .select(selectJwt)
    .pipe(
      map((token) => {
        if (!token) return true;

        return false;
      })
    );
};
