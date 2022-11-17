import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IHighScore } from '../high-score.model';
import { HighScoreService } from '../service/high-score.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './high-score-delete-dialog.component.html',
})
export class HighScoreDeleteDialogComponent {
  highScore?: IHighScore;

  constructor(protected highScoreService: HighScoreService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.highScoreService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
