import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IDailyReward, NewDailyReward } from '../daily-reward.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDailyReward for edit and NewDailyRewardFormGroupInput for create.
 */
type DailyRewardFormGroupInput = IDailyReward | PartialWithRequiredKeyOf<NewDailyReward>;

type DailyRewardFormDefaults = Pick<NewDailyReward, 'id'>;

type DailyRewardFormGroupContent = {
  id: FormControl<IDailyReward['id'] | NewDailyReward['id']>;
  time: FormControl<IDailyReward['time']>;
  rewardType: FormControl<IDailyReward['rewardType']>;
  amount: FormControl<IDailyReward['amount']>;
  couponReward: FormControl<IDailyReward['couponReward']>;
  nftReward: FormControl<IDailyReward['nftReward']>;
};

export type DailyRewardFormGroup = FormGroup<DailyRewardFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DailyRewardFormService {
  createDailyRewardFormGroup(dailyReward: DailyRewardFormGroupInput = { id: null }): DailyRewardFormGroup {
    const dailyRewardRawValue = {
      ...this.getFormDefaults(),
      ...dailyReward,
    };
    return new FormGroup<DailyRewardFormGroupContent>({
      id: new FormControl(
        { value: dailyRewardRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      time: new FormControl(dailyRewardRawValue.time),
      rewardType: new FormControl(dailyRewardRawValue.rewardType),
      amount: new FormControl(dailyRewardRawValue.amount),
      couponReward: new FormControl(dailyRewardRawValue.couponReward),
      nftReward: new FormControl(dailyRewardRawValue.nftReward),
    });
  }

  getDailyReward(form: DailyRewardFormGroup): IDailyReward | NewDailyReward {
    return form.getRawValue() as IDailyReward | NewDailyReward;
  }

  resetForm(form: DailyRewardFormGroup, dailyReward: DailyRewardFormGroupInput): void {
    const dailyRewardRawValue = { ...this.getFormDefaults(), ...dailyReward };
    form.reset(
      {
        ...dailyRewardRawValue,
        id: { value: dailyRewardRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): DailyRewardFormDefaults {
    return {
      id: null,
    };
  }
}
