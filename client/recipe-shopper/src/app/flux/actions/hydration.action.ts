import { createActionGroup, emptyProps, props } from '@ngrx/store';
import { State } from '../reducers';

export const HydrationActions = createActionGroup({
  source: 'Hydrate',
  events: {
    Hydrate: emptyProps(),

    'Hydrate Success': props<{ state: State }>(),
    'Hydrate Failure': emptyProps(),
  },
});
