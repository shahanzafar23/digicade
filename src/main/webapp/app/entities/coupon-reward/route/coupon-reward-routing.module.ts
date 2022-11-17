import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CouponRewardComponent } from '../list/coupon-reward.component';
import { CouponRewardDetailComponent } from '../detail/coupon-reward-detail.component';
import { CouponRewardUpdateComponent } from '../update/coupon-reward-update.component';
import { CouponRewardRoutingResolveService } from './coupon-reward-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const couponRewardRoute: Routes = [
  {
    path: '',
    component: CouponRewardComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CouponRewardDetailComponent,
    resolve: {
      couponReward: CouponRewardRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CouponRewardUpdateComponent,
    resolve: {
      couponReward: CouponRewardRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CouponRewardUpdateComponent,
    resolve: {
      couponReward: CouponRewardRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(couponRewardRoute)],
  exports: [RouterModule],
})
export class CouponRewardRoutingModule {}
