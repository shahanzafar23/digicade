import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDailyReward } from '../daily-reward.model';
import { DailyRewardService } from '../service/daily-reward.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './daily-reward-delete-dialog.component.html',
})
export class DailyRewardDeleteDialogComponent {
  dailyReward?: IDailyReward;

  constructor(protected dailyRewardService: DailyRewardService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.dailyRewardService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
