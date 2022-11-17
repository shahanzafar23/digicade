import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CouponImageComponent } from './list/coupon-image.component';
import { CouponImageDetailComponent } from './detail/coupon-image-detail.component';
import { CouponImageUpdateComponent } from './update/coupon-image-update.component';
import { CouponImageDeleteDialogComponent } from './delete/coupon-image-delete-dialog.component';
import { CouponImageRoutingModule } from './route/coupon-image-routing.module';

@NgModule({
  imports: [SharedModule, CouponImageRoutingModule],
  declarations: [CouponImageComponent, CouponImageDetailComponent, CouponImageUpdateComponent, CouponImageDeleteDialogComponent],
})
export class CouponImageModule {}
