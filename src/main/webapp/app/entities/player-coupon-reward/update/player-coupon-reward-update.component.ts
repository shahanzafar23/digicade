import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { PlayerCouponRewardFormService, PlayerCouponRewardFormGroup } from './player-coupon-reward-form.service';
import { IPlayerCouponReward } from '../player-coupon-reward.model';
import { PlayerCouponRewardService } from '../service/player-coupon-reward.service';
import { IPlayer } from 'app/entities/player/player.model';
import { PlayerService } from 'app/entities/player/service/player.service';
import { ICouponReward } from 'app/entities/coupon-reward/coupon-reward.model';
import { CouponRewardService } from 'app/entities/coupon-reward/service/coupon-reward.service';
import { CouponStatus } from 'app/entities/enumerations/coupon-status.model';

@Component({
  selector: 'jhi-player-coupon-reward-update',
  templateUrl: './player-coupon-reward-update.component.html',
})
export class PlayerCouponRewardUpdateComponent implements OnInit {
  isSaving = false;
  playerCouponReward: IPlayerCouponReward | null = null;
  couponStatusValues = Object.keys(CouponStatus);

  playersSharedCollection: IPlayer[] = [];
  couponRewardsSharedCollection: ICouponReward[] = [];

  editForm: PlayerCouponRewardFormGroup = this.playerCouponRewardFormService.createPlayerCouponRewardFormGroup();

  constructor(
    protected playerCouponRewardService: PlayerCouponRewardService,
    protected playerCouponRewardFormService: PlayerCouponRewardFormService,
    protected playerService: PlayerService,
    protected couponRewardService: CouponRewardService,
    protected activatedRoute: ActivatedRoute
  ) {}

  comparePlayer = (o1: IPlayer | null, o2: IPlayer | null): boolean => this.playerService.comparePlayer(o1, o2);

  compareCouponReward = (o1: ICouponReward | null, o2: ICouponReward | null): boolean =>
    this.couponRewardService.compareCouponReward(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ playerCouponReward }) => {
      this.playerCouponReward = playerCouponReward;
      if (playerCouponReward) {
        this.updateForm(playerCouponReward);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const playerCouponReward = this.playerCouponRewardFormService.getPlayerCouponReward(this.editForm);
    if (playerCouponReward.id !== null) {
      this.subscribeToSaveResponse(this.playerCouponRewardService.update(playerCouponReward));
    } else {
      this.subscribeToSaveResponse(this.playerCouponRewardService.create(playerCouponReward));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPlayerCouponReward>>): void {
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

  protected updateForm(playerCouponReward: IPlayerCouponReward): void {
    this.playerCouponReward = playerCouponReward;
    this.playerCouponRewardFormService.resetForm(this.editForm, playerCouponReward);

    this.playersSharedCollection = this.playerService.addPlayerToCollectionIfMissing<IPlayer>(
      this.playersSharedCollection,
      playerCouponReward.player
    );
    this.couponRewardsSharedCollection = this.couponRewardService.addCouponRewardToCollectionIfMissing<ICouponReward>(
      this.couponRewardsSharedCollection,
      playerCouponReward.couponReward
    );
  }

  protected loadRelationshipsOptions(): void {
    this.playerService
      .query()
      .pipe(map((res: HttpResponse<IPlayer[]>) => res.body ?? []))
      .pipe(
        map((players: IPlayer[]) => this.playerService.addPlayerToCollectionIfMissing<IPlayer>(players, this.playerCouponReward?.player))
      )
      .subscribe((players: IPlayer[]) => (this.playersSharedCollection = players));

    this.couponRewardService
      .query()
      .pipe(map((res: HttpResponse<ICouponReward[]>) => res.body ?? []))
      .pipe(
        map((couponRewards: ICouponReward[]) =>
          this.couponRewardService.addCouponRewardToCollectionIfMissing<ICouponReward>(couponRewards, this.playerCouponReward?.couponReward)
        )
      )
      .subscribe((couponRewards: ICouponReward[]) => (this.couponRewardsSharedCollection = couponRewards));
  }
}
