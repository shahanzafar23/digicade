import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ICouponImage, NewCouponImage } from '../coupon-image.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICouponImage for edit and NewCouponImageFormGroupInput for create.
 */
type CouponImageFormGroupInput = ICouponImage | PartialWithRequiredKeyOf<NewCouponImage>;

type CouponImageFormDefaults = Pick<NewCouponImage, 'id'>;

type CouponImageFormGroupContent = {
  id: FormControl<ICouponImage['id'] | NewCouponImage['id']>;
  imageUrl: FormControl<ICouponImage['imageUrl']>;
  couponReward: FormControl<ICouponImage['couponReward']>;
};

export type CouponImageFormGroup = FormGroup<CouponImageFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CouponImageFormService {
  createCouponImageFormGroup(couponImage: CouponImageFormGroupInput = { id: null }): CouponImageFormGroup {
    const couponImageRawValue = {
      ...this.getFormDefaults(),
      ...couponImage,
    };
    return new FormGroup<CouponImageFormGroupContent>({
      id: new FormControl(
        { value: couponImageRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      imageUrl: new FormControl(couponImageRawValue.imageUrl),
      couponReward: new FormControl(couponImageRawValue.couponReward),
    });
  }

  getCouponImage(form: CouponImageFormGroup): ICouponImage | NewCouponImage {
    return form.getRawValue() as ICouponImage | NewCouponImage;
  }

  resetForm(form: CouponImageFormGroup, couponImage: CouponImageFormGroupInput): void {
    const couponImageRawValue = { ...this.getFormDefaults(), ...couponImage };
    form.reset(
      {
        ...couponImageRawValue,
        id: { value: couponImageRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): CouponImageFormDefaults {
    return {
      id: null,
    };
  }
}
