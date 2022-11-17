import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PlayerCouponRewardComponent } from './list/player-coupon-reward.component';
import { PlayerCouponRewardDetailComponent } from './detail/player-coupon-reward-detail.component';
import { PlayerCouponRewardUpdateComponent } from './update/player-coupon-reward-update.component';
import { PlayerCouponRewardDeleteDialogComponent } from './delete/player-coupon-reward-delete-dialog.component';
import { PlayerCouponRewardRoutingModule } from './route/player-coupon-reward-routing.module';

@NgModule({
  imports: [SharedModule, PlayerCouponRewardRoutingModule],
  declarations: [
    PlayerCouponRewardComponent,
    PlayerCouponRewardDetailComponent,
    PlayerCouponRewardUpdateComponent,
    PlayerCouponRewardDeleteDialogComponent,
  ],
})
export class PlayerCouponRewardModule {}
