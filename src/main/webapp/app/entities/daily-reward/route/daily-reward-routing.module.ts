import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DailyRewardComponent } from '../list/daily-reward.component';
import { DailyRewardDetailComponent } from '../detail/daily-reward-detail.component';
import { DailyRewardUpdateComponent } from '../update/daily-reward-update.component';
import { DailyRewardRoutingResolveService } from './daily-reward-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const dailyRewardRoute: Routes = [
  {
    path: '',
    component: DailyRewardComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DailyRewardDetailComponent,
    resolve: {
      dailyReward: DailyRewardRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DailyRewardUpdateComponent,
    resolve: {
      dailyReward: DailyRewardRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DailyRewardUpdateComponent,
    resolve: {
      dailyReward: DailyRewardRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(dailyRewardRoute)],
  exports: [RouterModule],
})
export class DailyRewardRoutingModule {}
