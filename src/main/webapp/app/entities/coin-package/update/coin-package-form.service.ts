import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ICoinPackage, NewCoinPackage } from '../coin-package.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICoinPackage for edit and NewCoinPackageFormGroupInput for create.
 */
type CoinPackageFormGroupInput = ICoinPackage | PartialWithRequiredKeyOf<NewCoinPackage>;

type CoinPackageFormDefaults = Pick<NewCoinPackage, 'id'>;

type CoinPackageFormGroupContent = {
  id: FormControl<ICoinPackage['id'] | NewCoinPackage['id']>;
  coins: FormControl<ICoinPackage['coins']>;
  cost: FormControl<ICoinPackage['cost']>;
};

export type CoinPackageFormGroup = FormGroup<CoinPackageFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CoinPackageFormService {
  createCoinPackageFormGroup(coinPackage: CoinPackageFormGroupInput = { id: null }): CoinPackageFormGroup {
    const coinPackageRawValue = {
      ...this.getFormDefaults(),
      ...coinPackage,
    };
    return new FormGroup<CoinPackageFormGroupContent>({
      id: new FormControl(
        { value: coinPackageRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      coins: new FormControl(coinPackageRawValue.coins),
      cost: new FormControl(coinPackageRawValue.cost),
    });
  }

  getCoinPackage(form: CoinPackageFormGroup): ICoinPackage | NewCoinPackage {
    return form.getRawValue() as ICoinPackage | NewCoinPackage;
  }

  resetForm(form: CoinPackageFormGroup, coinPackage: CoinPackageFormGroupInput): void {
    const coinPackageRawValue = { ...this.getFormDefaults(), ...coinPackage };
    form.reset(
      {
        ...coinPackageRawValue,
        id: { value: coinPackageRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): CoinPackageFormDefaults {
    return {
      id: null,
    };
  }
}
