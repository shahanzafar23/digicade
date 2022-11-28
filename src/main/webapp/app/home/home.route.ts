import { Route } from '@angular/router';

import { HomeComponent } from './home.component';
import { AppComponent } from '../app/app.component';

export const HOME_ROUTE: Route = {
  path: '',
  component: AppComponent,
  data: {
    pageTitle: 'Welcome, Java Hipster!',
  },
};
