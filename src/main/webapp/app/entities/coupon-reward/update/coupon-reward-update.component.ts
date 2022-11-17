import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { CouponRewardFormService, CouponRewardFormGroup } from './coupon-reward-form.service';
import { ICouponReward } from '../coupon-reward.model';
import { CouponRewardService } from '../service/coupon-reward.service';

@Component({
  selector: 'jhi-coupon-reward-update',
  templateUrl: './coupon-reward-update.component.html',
})
export class CouponRewardUpdateComponent implements OnInit {
  isSaving = false;
  couponReward: ICouponReward | null = null;

  editForm: CouponRewardFormGroup = this.couponRewardFormService.createCouponRewardFormGroup();

  constructor(
    protected couponRewardService: CouponRewardService,
    protected couponRewardFormService: CouponRewardFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ couponReward }) => {
      this.couponReward = couponReward;
      if (couponReward) {
        this.updateForm(couponReward);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const couponReward = this.couponRewardFormService.getCouponReward(this.editForm);
    if (couponReward.id !== null) {
      this.subscribeToSaveResponse(this.couponRewardService.update(couponReward));
    } else {
      this.subscribeToSaveResponse(this.couponRewardService.create(couponReward));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICouponReward>>): void {
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

  protected updateForm(couponReward: ICouponReward): void {
    this.couponReward = couponReward;
    this.couponRewardFormService.resetForm(this.editForm, couponReward);
  }
}
