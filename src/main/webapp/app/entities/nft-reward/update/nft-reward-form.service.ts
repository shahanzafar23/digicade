import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { INftReward, NewNftReward } from '../nft-reward.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts INftReward for edit and NewNftRewardFormGroupInput for create.
 */
type NftRewardFormGroupInput = INftReward | PartialWithRequiredKeyOf<NewNftReward>;

type NftRewardFormDefaults = Pick<NewNftReward, 'id'>;

type NftRewardFormGroupContent = {
  id: FormControl<INftReward['id'] | NewNftReward['id']>;
  tix: FormControl<INftReward['tix']>;
  comp: FormControl<INftReward['comp']>;
  imageUrl: FormControl<INftReward['imageUrl']>;
};

export type NftRewardFormGroup = FormGroup<NftRewardFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class NftRewardFormService {
  createNftRewardFormGroup(nftReward: NftRewardFormGroupInput = { id: null }): NftRewardFormGroup {
    const nftRewardRawValue = {
      ...this.getFormDefaults(),
      ...nftReward,
    };
    return new FormGroup<NftRewardFormGroupContent>({
      id: new FormControl(
        { value: nftRewardRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      tix: new FormControl(nftRewardRawValue.tix),
      comp: new FormControl(nftRewardRawValue.comp),
      imageUrl: new FormControl(nftRewardRawValue.imageUrl),
    });
  }

  getNftReward(form: NftRewardFormGroup): INftReward | NewNftReward {
    return form.getRawValue() as INftReward | NewNftReward;
  }

  resetForm(form: NftRewardFormGroup, nftReward: NftRewardFormGroupInput): void {
    const nftRewardRawValue = { ...this.getFormDefaults(), ...nftReward };
    form.reset(
      {
        ...nftRewardRawValue,
        id: { value: nftRewardRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): NftRewardFormDefaults {
    return {
      id: null,
    };
  }
}
