import { Action, ActionReducer } from '@ngrx/store';
import { State, initialState } from '../reducers';
import { HydrationActions } from './hydration.action';

// Middleware to check if action type is Hydrate Success
const isHydrateSuccess = (
  action: Action
): action is ReturnType<typeof HydrationActions.hydrateSuccess> => {
  return action.type == HydrationActions.hydrateSuccess.type;
};

// Middleware to check if action type is Hydrate Failure
const isHydrateFailure = (
  action: Action
): action is ReturnType<typeof HydrationActions.hydrateFailure> => {
  return action.type == HydrationActions.hydrateFailure.type;
};

// Update store depending on hydration success/failure
export const HydrationMetaReducer = (
  reducer: ActionReducer<State>
): ActionReducer<State> => {
  return (state, action) => {
    if (isHydrateSuccess(action)) {
      console.debug('>>> Hydrate success ran');
      return action.state;
    } else if (isHydrateFailure(action)) {
      console.debug('>>> Hydrate failure ran');
      return initialState;
    }

    return reducer(state, action);
  };
};
