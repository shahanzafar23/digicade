import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IPlayerCouponReward, NewPlayerCouponReward } from '../player-coupon-reward.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPlayerCouponReward for edit and NewPlayerCouponRewardFormGroupInput for create.
 */
type PlayerCouponRewardFormGroupInput = IPlayerCouponReward | PartialWithRequiredKeyOf<NewPlayerCouponReward>;

type PlayerCouponRewardFormDefaults = Pick<NewPlayerCouponReward, 'id'>;

type PlayerCouponRewardFormGroupContent = {
  id: FormControl<IPlayerCouponReward['id'] | NewPlayerCouponReward['id']>;
  date: FormControl<IPlayerCouponReward['date']>;
  status: FormControl<IPlayerCouponReward['status']>;
  player: FormControl<IPlayerCouponReward['player']>;
  couponReward: FormControl<IPlayerCouponReward['couponReward']>;
};

export type PlayerCouponRewardFormGroup = FormGroup<PlayerCouponRewardFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PlayerCouponRewardFormService {
  createPlayerCouponRewardFormGroup(playerCouponReward: PlayerCouponRewardFormGroupInput = { id: null }): PlayerCouponRewardFormGroup {
    const playerCouponRewardRawValue = {
      ...this.getFormDefaults(),
      ...playerCouponReward,
    };
    return new FormGroup<PlayerCouponRewardFormGroupContent>({
      id: new FormControl(
        { value: playerCouponRewardRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      date: new FormControl(playerCouponRewardRawValue.date),
      status: new FormControl(playerCouponRewardRawValue.status),
      player: new FormControl(playerCouponRewardRawValue.player),
      couponReward: new FormControl(playerCouponRewardRawValue.couponReward),
    });
  }

  getPlayerCouponReward(form: PlayerCouponRewardFormGroup): IPlayerCouponReward | NewPlayerCouponReward {
    return form.getRawValue() as IPlayerCouponReward | NewPlayerCouponReward;
  }

  resetForm(form: PlayerCouponRewardFormGroup, playerCouponReward: PlayerCouponRewardFormGroupInput): void {
    const playerCouponRewardRawValue = { ...this.getFormDefaults(), ...playerCouponReward };
    form.reset(
      {
        ...playerCouponRewardRawValue,
        id: { value: playerCouponRewardRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): PlayerCouponRewardFormDefaults {
    return {
      id: null,
    };
  }
}
