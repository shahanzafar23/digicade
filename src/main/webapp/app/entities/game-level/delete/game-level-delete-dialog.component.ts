import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IGameLevel } from '../game-level.model';
import { GameLevelService } from '../service/game-level.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './game-level-delete-dialog.component.html',
})
export class GameLevelDeleteDialogComponent {
  gameLevel?: IGameLevel;

  constructor(protected gameLevelService: GameLevelService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.gameLevelService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
