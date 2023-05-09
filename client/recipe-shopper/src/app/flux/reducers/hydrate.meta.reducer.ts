import { Action, ActionReducer } from '@ngrx/store';
import { State } from '.';
import { HydrationActions } from '../actions/hydration.action';

const isHydrateSuccess = (
  action: Action
): action is ReturnType<typeof HydrationActions.hydrateSuccess> => {
  return action.type == HydrationActions.hydrateSuccess.type;
};

export const HydrationMetaReducer = (
  reducer: ActionReducer<State>
): ActionReducer<State> => {
  return (state, action) => {
    if (isHydrateSuccess(action)) {
      console.debug('>>> Hydrate success ran');
      return action.state;
    }
    return reducer(state, action);
  };
};
