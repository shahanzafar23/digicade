import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IPlayerNftReward, NewPlayerNftReward } from '../player-nft-reward.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPlayerNftReward for edit and NewPlayerNftRewardFormGroupInput for create.
 */
type PlayerNftRewardFormGroupInput = IPlayerNftReward | PartialWithRequiredKeyOf<NewPlayerNftReward>;

type PlayerNftRewardFormDefaults = Pick<NewPlayerNftReward, 'id'>;

type PlayerNftRewardFormGroupContent = {
  id: FormControl<IPlayerNftReward['id'] | NewPlayerNftReward['id']>;
  date: FormControl<IPlayerNftReward['date']>;
  player: FormControl<IPlayerNftReward['player']>;
  nftReward: FormControl<IPlayerNftReward['nftReward']>;
};

export type PlayerNftRewardFormGroup = FormGroup<PlayerNftRewardFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PlayerNftRewardFormService {
  createPlayerNftRewardFormGroup(playerNftReward: PlayerNftRewardFormGroupInput = { id: null }): PlayerNftRewardFormGroup {
    const playerNftRewardRawValue = {
      ...this.getFormDefaults(),
      ...playerNftReward,
    };
    return new FormGroup<PlayerNftRewardFormGroupContent>({
      id: new FormControl(
        { value: playerNftRewardRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      date: new FormControl(playerNftRewardRawValue.date),
      player: new FormControl(playerNftRewardRawValue.player),
      nftReward: new FormControl(playerNftRewardRawValue.nftReward),
    });
  }

  getPlayerNftReward(form: PlayerNftRewardFormGroup): IPlayerNftReward | NewPlayerNftReward {
    return form.getRawValue() as IPlayerNftReward | NewPlayerNftReward;
  }

  resetForm(form: PlayerNftRewardFormGroup, playerNftReward: PlayerNftRewardFormGroupInput): void {
    const playerNftRewardRawValue = { ...this.getFormDefaults(), ...playerNftReward };
    form.reset(
      {
        ...playerNftRewardRawValue,
        id: { value: playerNftRewardRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): PlayerNftRewardFormDefaults {
    return {
      id: null,
    };
  }
}
