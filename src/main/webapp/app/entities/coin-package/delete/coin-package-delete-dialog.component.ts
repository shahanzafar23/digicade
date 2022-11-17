import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICoinPackage } from '../coin-package.model';
import { CoinPackageService } from '../service/coin-package.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './coin-package-delete-dialog.component.html',
})
export class CoinPackageDeleteDialogComponent {
  coinPackage?: ICoinPackage;

  constructor(protected coinPackageService: CoinPackageService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.coinPackageService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
