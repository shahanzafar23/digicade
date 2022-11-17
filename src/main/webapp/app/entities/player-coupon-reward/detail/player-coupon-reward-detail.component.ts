import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPlayerCouponReward } from '../player-coupon-reward.model';

@Component({
  selector: 'jhi-player-coupon-reward-detail',
  templateUrl: './player-coupon-reward-detail.component.html',
})
export class PlayerCouponRewardDetailComponent implements OnInit {
  playerCouponReward: IPlayerCouponReward | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ playerCouponReward }) => {
      this.playerCouponReward = playerCouponReward;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
