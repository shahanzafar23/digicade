import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPlayerNftReward } from '../player-nft-reward.model';
import { PlayerNftRewardService } from '../service/player-nft-reward.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './player-nft-reward-delete-dialog.component.html',
})
export class PlayerNftRewardDeleteDialogComponent {
  playerNftReward?: IPlayerNftReward;

  constructor(protected playerNftRewardService: PlayerNftRewardService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.playerNftRewardService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
