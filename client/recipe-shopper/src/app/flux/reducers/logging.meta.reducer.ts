import { ActionReducer } from "@ngrx/store";
import { State } from ".";

export const loggingMetaReducer = (
    reducer: ActionReducer<State>
  ): ActionReducer<State> => {
    return (state, action) => {
      console.log('>>> Action: ', action);
      console.log('>>> State: ', state);
  
      return reducer(state, action);
    };
  };
  