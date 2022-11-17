import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ICouponReward, NewCouponReward } from '../coupon-reward.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICouponReward for edit and NewCouponRewardFormGroupInput for create.
 */
type CouponRewardFormGroupInput = ICouponReward | PartialWithRequiredKeyOf<NewCouponReward>;

type CouponRewardFormDefaults = Pick<NewCouponReward, 'id'>;

type CouponRewardFormGroupContent = {
  id: FormControl<ICouponReward['id'] | NewCouponReward['id']>;
  description: FormControl<ICouponReward['description']>;
  location: FormControl<ICouponReward['location']>;
  tix: FormControl<ICouponReward['tix']>;
  comp: FormControl<ICouponReward['comp']>;
  expiry: FormControl<ICouponReward['expiry']>;
};

export type CouponRewardFormGroup = FormGroup<CouponRewardFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CouponRewardFormService {
  createCouponRewardFormGroup(couponReward: CouponRewardFormGroupInput = { id: null }): CouponRewardFormGroup {
    const couponRewardRawValue = {
      ...this.getFormDefaults(),
      ...couponReward,
    };
    return new FormGroup<CouponRewardFormGroupContent>({
      id: new FormControl(
        { value: couponRewardRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      description: new FormControl(couponRewardRawValue.description),
      location: new FormControl(couponRewardRawValue.location),
      tix: new FormControl(couponRewardRawValue.tix),
      comp: new FormControl(couponRewardRawValue.comp),
      expiry: new FormControl(couponRewardRawValue.expiry),
    });
  }

  getCouponReward(form: CouponRewardFormGroup): ICouponReward | NewCouponReward {
    return form.getRawValue() as ICouponReward | NewCouponReward;
  }

  resetForm(form: CouponRewardFormGroup, couponReward: CouponRewardFormGroupInput): void {
    const couponRewardRawValue = { ...this.getFormDefaults(), ...couponReward };
    form.reset(
      {
        ...couponRewardRawValue,
        id: { value: couponRewardRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): CouponRewardFormDefaults {
    return {
      id: null,
    };
  }
}
