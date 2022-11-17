import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PlayerCouponRewardComponent } from '../list/player-coupon-reward.component';
import { PlayerCouponRewardDetailComponent } from '../detail/player-coupon-reward-detail.component';
import { PlayerCouponRewardUpdateComponent } from '../update/player-coupon-reward-update.component';
import { PlayerCouponRewardRoutingResolveService } from './player-coupon-reward-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const playerCouponRewardRoute: Routes = [
  {
    path: '',
    component: PlayerCouponRewardComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PlayerCouponRewardDetailComponent,
    resolve: {
      playerCouponReward: PlayerCouponRewardRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PlayerCouponRewardUpdateComponent,
    resolve: {
      playerCouponReward: PlayerCouponRewardRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PlayerCouponRewardUpdateComponent,
    resolve: {
      playerCouponReward: PlayerCouponRewardRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(playerCouponRewardRoute)],
  exports: [RouterModule],
})
export class PlayerCouponRewardRoutingModule {}
