import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICouponReward } from '../coupon-reward.model';
import { CouponRewardService } from '../service/coupon-reward.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './coupon-reward-delete-dialog.component.html',
})
export class CouponRewardDeleteDialogComponent {
  couponReward?: ICouponReward;

  constructor(protected couponRewardService: CouponRewardService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.couponRewardService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
