import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDigiUser } from '../digi-user.model';
import { DigiUserService } from '../service/digi-user.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './digi-user-delete-dialog.component.html',
})
export class DigiUserDeleteDialogComponent {
  digiUser?: IDigiUser;

  constructor(protected digiUserService: DigiUserService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.digiUserService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
