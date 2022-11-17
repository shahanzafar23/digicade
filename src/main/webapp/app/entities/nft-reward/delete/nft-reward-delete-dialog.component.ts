import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { INftReward } from '../nft-reward.model';
import { NftRewardService } from '../service/nft-reward.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './nft-reward-delete-dialog.component.html',
})
export class NftRewardDeleteDialogComponent {
  nftReward?: INftReward;

  constructor(protected nftRewardService: NftRewardService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.nftRewardService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
