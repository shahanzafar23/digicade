import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { CouponImageFormService, CouponImageFormGroup } from './coupon-image-form.service';
import { ICouponImage } from '../coupon-image.model';
import { CouponImageService } from '../service/coupon-image.service';
import { ICouponReward } from 'app/entities/coupon-reward/coupon-reward.model';
import { CouponRewardService } from 'app/entities/coupon-reward/service/coupon-reward.service';

@Component({
  selector: 'jhi-coupon-image-update',
  templateUrl: './coupon-image-update.component.html',
})
export class CouponImageUpdateComponent implements OnInit {
  isSaving = false;
  couponImage: ICouponImage | null = null;

  couponRewardsSharedCollection: ICouponReward[] = [];

  editForm: CouponImageFormGroup = this.couponImageFormService.createCouponImageFormGroup();

  constructor(
    protected couponImageService: CouponImageService,
    protected couponImageFormService: CouponImageFormService,
    protected couponRewardService: CouponRewardService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareCouponReward = (o1: ICouponReward | null, o2: ICouponReward | null): boolean =>
    this.couponRewardService.compareCouponReward(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ couponImage }) => {
      this.couponImage = couponImage;
      if (couponImage) {
        this.updateForm(couponImage);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const couponImage = this.couponImageFormService.getCouponImage(this.editForm);
    if (couponImage.id !== null) {
      this.subscribeToSaveResponse(this.couponImageService.update(couponImage));
    } else {
      this.subscribeToSaveResponse(this.couponImageService.create(couponImage));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICouponImage>>): void {
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

  protected updateForm(couponImage: ICouponImage): void {
    this.couponImage = couponImage;
    this.couponImageFormService.resetForm(this.editForm, couponImage);

    this.couponRewardsSharedCollection = this.couponRewardService.addCouponRewardToCollectionIfMissing<ICouponReward>(
      this.couponRewardsSharedCollection,
      couponImage.couponReward
    );
  }

  protected loadRelationshipsOptions(): void {
    this.couponRewardService
      .query()
      .pipe(map((res: HttpResponse<ICouponReward[]>) => res.body ?? []))
      .pipe(
        map((couponRewards: ICouponReward[]) =>
          this.couponRewardService.addCouponRewardToCollectionIfMissing<ICouponReward>(couponRewards, this.couponImage?.couponReward)
        )
      )
      .subscribe((couponRewards: ICouponReward[]) => (this.couponRewardsSharedCollection = couponRewards));
  }
}
