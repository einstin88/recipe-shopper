import { inject } from '@angular/core';
import { Actions, createEffect } from '@ngrx/effects';
import { distinctUntilChanged, switchMap, tap } from 'rxjs';
import { Store } from '@ngrx/store';
import { State } from '../reducers';

/**
 * This effect detects changes in the store and persist the data in local storage
 */
export const persistStoreEffect = createEffect(
  (actions$ = inject(Actions), store = inject(Store<State>)) => {
    return actions$.pipe(
      switchMap(() => store),
      distinctUntilChanged(),
      tap((state) => localStorage.setItem('state', JSON.stringify(state)))
    );
  },
  { functional: true, dispatch: false } // Functional effect declaration; stop effect from triggering further actions
);
