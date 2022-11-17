import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CouponImageComponent } from '../list/coupon-image.component';
import { CouponImageDetailComponent } from '../detail/coupon-image-detail.component';
import { CouponImageUpdateComponent } from '../update/coupon-image-update.component';
import { CouponImageRoutingResolveService } from './coupon-image-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const couponImageRoute: Routes = [
  {
    path: '',
    component: CouponImageComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CouponImageDetailComponent,
    resolve: {
      couponImage: CouponImageRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CouponImageUpdateComponent,
    resolve: {
      couponImage: CouponImageRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CouponImageUpdateComponent,
    resolve: {
      couponImage: CouponImageRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(couponImageRoute)],
  exports: [RouterModule],
})
export class CouponImageRoutingModule {}
