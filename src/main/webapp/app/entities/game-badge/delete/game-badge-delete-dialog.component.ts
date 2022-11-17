import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IGameBadge } from '../game-badge.model';
import { GameBadgeService } from '../service/game-badge.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './game-badge-delete-dialog.component.html',
})
export class GameBadgeDeleteDialogComponent {
  gameBadge?: IGameBadge;

  constructor(protected gameBadgeService: GameBadgeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.gameBadgeService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
