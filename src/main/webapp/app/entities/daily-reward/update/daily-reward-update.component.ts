import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { DailyRewardFormService, DailyRewardFormGroup } from './daily-reward-form.service';
import { IDailyReward } from '../daily-reward.model';
import { DailyRewardService } from '../service/daily-reward.service';
import { ICouponReward } from 'app/entities/coupon-reward/coupon-reward.model';
import { CouponRewardService } from 'app/entities/coupon-reward/service/coupon-reward.service';
import { INftReward } from 'app/entities/nft-reward/nft-reward.model';
import { NftRewardService } from 'app/entities/nft-reward/service/nft-reward.service';
import { RewardType } from 'app/entities/enumerations/reward-type.model';

@Component({
  selector: 'jhi-daily-reward-update',
  templateUrl: './daily-reward-update.component.html',
})
export class DailyRewardUpdateComponent implements OnInit {
  isSaving = false;
  dailyReward: IDailyReward | null = null;
  rewardTypeValues = Object.keys(RewardType);

  couponRewardsSharedCollection: ICouponReward[] = [];
  nftRewardsSharedCollection: INftReward[] = [];

  editForm: DailyRewardFormGroup = this.dailyRewardFormService.createDailyRewardFormGroup();

  constructor(
    protected dailyRewardService: DailyRewardService,
    protected dailyRewardFormService: DailyRewardFormService,
    protected couponRewardService: CouponRewardService,
    protected nftRewardService: NftRewardService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareCouponReward = (o1: ICouponReward | null, o2: ICouponReward | null): boolean =>
    this.couponRewardService.compareCouponReward(o1, o2);

  compareNftReward = (o1: INftReward | null, o2: INftReward | null): boolean => this.nftRewardService.compareNftReward(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ dailyReward }) => {
      this.dailyReward = dailyReward;
      if (dailyReward) {
        this.updateForm(dailyReward);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const dailyReward = this.dailyRewardFormService.getDailyReward(this.editForm);
    if (dailyReward.id !== null) {
      this.subscribeToSaveResponse(this.dailyRewardService.update(dailyReward));
    } else {
      this.subscribeToSaveResponse(this.dailyRewardService.create(dailyReward));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDailyReward>>): void {
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

  protected updateForm(dailyReward: IDailyReward): void {
    this.dailyReward = dailyReward;
    this.dailyRewardFormService.resetForm(this.editForm, dailyReward);

    this.couponRewardsSharedCollection = this.couponRewardService.addCouponRewardToCollectionIfMissing<ICouponReward>(
      this.couponRewardsSharedCollection,
      dailyReward.couponReward
    );
    this.nftRewardsSharedCollection = this.nftRewardService.addNftRewardToCollectionIfMissing<INftReward>(
      this.nftRewardsSharedCollection,
      dailyReward.nftReward
    );
  }

  protected loadRelationshipsOptions(): void {
    this.couponRewardService
      .query()
      .pipe(map((res: HttpResponse<ICouponReward[]>) => res.body ?? []))
      .pipe(
        map((couponRewards: ICouponReward[]) =>
          this.couponRewardService.addCouponRewardToCollectionIfMissing<ICouponReward>(couponRewards, this.dailyReward?.couponReward)
        )
      )
      .subscribe((couponRewards: ICouponReward[]) => (this.couponRewardsSharedCollection = couponRewards));

    this.nftRewardService
      .query()
      .pipe(map((res: HttpResponse<INftReward[]>) => res.body ?? []))
      .pipe(
        map((nftRewards: INftReward[]) =>
          this.nftRewardService.addNftRewardToCollectionIfMissing<INftReward>(nftRewards, this.dailyReward?.nftReward)
        )
      )
      .subscribe((nftRewards: INftReward[]) => (this.nftRewardsSharedCollection = nftRewards));
  }
}
