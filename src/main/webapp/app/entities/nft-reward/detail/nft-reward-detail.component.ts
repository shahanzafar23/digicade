import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { INftReward } from '../nft-reward.model';

@Component({
  selector: 'jhi-nft-reward-detail',
  templateUrl: './nft-reward-detail.component.html',
})
export class NftRewardDetailComponent implements OnInit {
  nftReward: INftReward | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ nftReward }) => {
      this.nftReward = nftReward;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
