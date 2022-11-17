import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPlayerNftReward } from '../player-nft-reward.model';

@Component({
  selector: 'jhi-player-nft-reward-detail',
  templateUrl: './player-nft-reward-detail.component.html',
})
export class PlayerNftRewardDetailComponent implements OnInit {
  playerNftReward: IPlayerNftReward | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ playerNftReward }) => {
      this.playerNftReward = playerNftReward;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
