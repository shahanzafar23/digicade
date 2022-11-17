import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CouponRewardComponent } from './list/coupon-reward.component';
import { CouponRewardDetailComponent } from './detail/coupon-reward-detail.component';
import { CouponRewardUpdateComponent } from './update/coupon-reward-update.component';
import { CouponRewardDeleteDialogComponent } from './delete/coupon-reward-delete-dialog.component';
import { CouponRewardRoutingModule } from './route/coupon-reward-routing.module';

@NgModule({
  imports: [SharedModule, CouponRewardRoutingModule],
  declarations: [CouponRewardComponent, CouponRewardDetailComponent, CouponRewardUpdateComponent, CouponRewardDeleteDialogComponent],
})
export class CouponRewardModule {}
