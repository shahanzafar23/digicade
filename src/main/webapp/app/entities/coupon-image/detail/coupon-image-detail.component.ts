import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICouponImage } from '../coupon-image.model';

@Component({
  selector: 'jhi-coupon-image-detail',
  templateUrl: './coupon-image-detail.component.html',
})
export class CouponImageDetailComponent implements OnInit {
  couponImage: ICouponImage | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ couponImage }) => {
      this.couponImage = couponImage;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
