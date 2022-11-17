import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPlayerCouponReward } from '../player-coupon-reward.model';
import { PlayerCouponRewardService } from '../service/player-coupon-reward.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './player-coupon-reward-delete-dialog.component.html',
})
export class PlayerCouponRewardDeleteDialogComponent {
  playerCouponReward?: IPlayerCouponReward;

  constructor(protected playerCouponRewardService: PlayerCouponRewardService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.playerCouponRewardService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
