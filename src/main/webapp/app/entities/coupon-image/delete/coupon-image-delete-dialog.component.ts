import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICouponImage } from '../coupon-image.model';
import { CouponImageService } from '../service/coupon-image.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './coupon-image-delete-dialog.component.html',
})
export class CouponImageDeleteDialogComponent {
  couponImage?: ICouponImage;

  constructor(protected couponImageService: CouponImageService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.couponImageService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
