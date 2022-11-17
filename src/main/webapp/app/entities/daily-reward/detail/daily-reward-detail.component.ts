import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDailyReward } from '../daily-reward.model';

@Component({
  selector: 'jhi-daily-reward-detail',
  templateUrl: './daily-reward-detail.component.html',
})
export class DailyRewardDetailComponent implements OnInit {
  dailyReward: IDailyReward | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ dailyReward }) => {
      this.dailyReward = dailyReward;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
