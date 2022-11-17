import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { PlayerNftRewardFormService, PlayerNftRewardFormGroup } from './player-nft-reward-form.service';
import { IPlayerNftReward } from '../player-nft-reward.model';
import { PlayerNftRewardService } from '../service/player-nft-reward.service';
import { IPlayer } from 'app/entities/player/player.model';
import { PlayerService } from 'app/entities/player/service/player.service';
import { INftReward } from 'app/entities/nft-reward/nft-reward.model';
import { NftRewardService } from 'app/entities/nft-reward/service/nft-reward.service';

@Component({
  selector: 'jhi-player-nft-reward-update',
  templateUrl: './player-nft-reward-update.component.html',
})
export class PlayerNftRewardUpdateComponent implements OnInit {
  isSaving = false;
  playerNftReward: IPlayerNftReward | null = null;

  playersSharedCollection: IPlayer[] = [];
  nftRewardsSharedCollection: INftReward[] = [];

  editForm: PlayerNftRewardFormGroup = this.playerNftRewardFormService.createPlayerNftRewardFormGroup();

  constructor(
    protected playerNftRewardService: PlayerNftRewardService,
    protected playerNftRewardFormService: PlayerNftRewardFormService,
    protected playerService: PlayerService,
    protected nftRewardService: NftRewardService,
    protected activatedRoute: ActivatedRoute
  ) {}

  comparePlayer = (o1: IPlayer | null, o2: IPlayer | null): boolean => this.playerService.comparePlayer(o1, o2);

  compareNftReward = (o1: INftReward | null, o2: INftReward | null): boolean => this.nftRewardService.compareNftReward(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ playerNftReward }) => {
      this.playerNftReward = playerNftReward;
      if (playerNftReward) {
        this.updateForm(playerNftReward);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const playerNftReward = this.playerNftRewardFormService.getPlayerNftReward(this.editForm);
    if (playerNftReward.id !== null) {
      this.subscribeToSaveResponse(this.playerNftRewardService.update(playerNftReward));
    } else {
      this.subscribeToSaveResponse(this.playerNftRewardService.create(playerNftReward));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPlayerNftReward>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(playerNftReward: IPlayerNftReward): void {
    this.playerNftReward = playerNftReward;
    this.playerNftRewardFormService.resetForm(this.editForm, playerNftReward);

    this.playersSharedCollection = this.playerService.addPlayerToCollectionIfMissing<IPlayer>(
      this.playersSharedCollection,
      playerNftReward.player
    );
    this.nftRewardsSharedCollection = this.nftRewardService.addNftRewardToCollectionIfMissing<INftReward>(
      this.nftRewardsSharedCollection,
      playerNftReward.nftReward
    );
  }

  protected loadRelationshipsOptions(): void {
    this.playerService
      .query()
      .pipe(map((res: HttpResponse<IPlayer[]>) => res.body ?? []))
      .pipe(map((players: IPlayer[]) => this.playerService.addPlayerToCollectionIfMissing<IPlayer>(players, this.playerNftReward?.player)))
      .subscribe((players: IPlayer[]) => (this.playersSharedCollection = players));

    this.nftRewardService
      .query()
      .pipe(map((res: HttpResponse<INftReward[]>) => res.body ?? []))
      .pipe(
        map((nftRewards: INftReward[]) =>
          this.nftRewardService.addNftRewardToCollectionIfMissing<INftReward>(nftRewards, this.playerNftReward?.nftReward)
        )
      )
      .subscribe((nftRewards: INftReward[]) => (this.nftRewardsSharedCollection = nftRewards));
  }
}
