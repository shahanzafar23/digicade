import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICouponReward } from '../coupon-reward.model';

@Component({
  selector: 'jhi-coupon-reward-detail',
  templateUrl: './coupon-reward-detail.component.html',
})
export class CouponRewardDetailComponent implements OnInit {
  couponReward: ICouponReward | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ couponReward }) => {
      this.couponReward = couponReward;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
