import { createActionGroup, emptyProps, props } from '@ngrx/store';
import { JWT } from 'src/app/model/jwt.model';

export const AuthActions = createActionGroup({
  source: 'Auth Server',
  events: {
    'Login Success': props<{ jwt: JWT }>(),
    'Login Failure': emptyProps(),

    'Registration Success': props<{ jwt: JWT }>(),
    'Registration Failure': emptyProps(),

    Logout: emptyProps(),
    'Logout Success': emptyProps(),
    'Logout Failure': emptyProps(),

    'Token Expired': emptyProps()
  },
});
