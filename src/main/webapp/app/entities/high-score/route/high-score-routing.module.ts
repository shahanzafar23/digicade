import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { HighScoreComponent } from '../list/high-score.component';
import { HighScoreDetailComponent } from '../detail/high-score-detail.component';
import { HighScoreUpdateComponent } from '../update/high-score-update.component';
import { HighScoreRoutingResolveService } from './high-score-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const highScoreRoute: Routes = [
  {
    path: '',
    component: HighScoreComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: HighScoreDetailComponent,
    resolve: {
      highScore: HighScoreRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: HighScoreUpdateComponent,
    resolve: {
      highScore: HighScoreRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: HighScoreUpdateComponent,
    resolve: {
      highScore: HighScoreRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(highScoreRoute)],
  exports: [RouterModule],
})
export class HighScoreRoutingModule {}
