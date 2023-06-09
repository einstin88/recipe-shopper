import { Injectable } from '@angular/core';
import { Actions, OnInitEffects, createEffect, ofType } from '@ngrx/effects';
import { Action, Store } from '@ngrx/store';
import { HydrationActions } from './hydration.action';
import { State } from '../reducers';
import { map } from 'rxjs';

@Injectable()
export class HydrationEffect implements OnInitEffects {
  constructor(private action$: Actions, private store: Store<State>) {}

  ngrxOnInitEffects(): Action {
    return HydrationActions.hydrate();
  }

  // Hydrate initiator
  hydrate$ = createEffect(() => {
    return this.action$.pipe(
      ofType(HydrationActions.hydrate),
      map(() => {
        const storedState = localStorage.getItem('state');
        console.debug('>>> state after hydrate: ', storedState);
        if (storedState) {
          try {
            const state = JSON.parse(storedState) as State;
            console.debug('>> parsed state: ', state);

            return HydrationActions.hydrateSuccess({ state });
          } catch {
            localStorage.removeItem('state');
          }
        }
        console.debug('>>> did hydrate failed??');
        return HydrationActions.hydrateFailure();
      })
    );
  });
}
