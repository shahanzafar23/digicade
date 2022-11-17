import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IDigiUser, NewDigiUser } from '../digi-user.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDigiUser for edit and NewDigiUserFormGroupInput for create.
 */
type DigiUserFormGroupInput = IDigiUser | PartialWithRequiredKeyOf<NewDigiUser>;

type DigiUserFormDefaults = Pick<NewDigiUser, 'id'>;

type DigiUserFormGroupContent = {
  id: FormControl<IDigiUser['id'] | NewDigiUser['id']>;
  firstName: FormControl<IDigiUser['firstName']>;
  lastName: FormControl<IDigiUser['lastName']>;
  userName: FormControl<IDigiUser['userName']>;
  email: FormControl<IDigiUser['email']>;
  dob: FormControl<IDigiUser['dob']>;
  age: FormControl<IDigiUser['age']>;
  promoCode: FormControl<IDigiUser['promoCode']>;
};

export type DigiUserFormGroup = FormGroup<DigiUserFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DigiUserFormService {
  createDigiUserFormGroup(digiUser: DigiUserFormGroupInput = { id: null }): DigiUserFormGroup {
    const digiUserRawValue = {
      ...this.getFormDefaults(),
      ...digiUser,
    };
    return new FormGroup<DigiUserFormGroupContent>({
      id: new FormControl(
        { value: digiUserRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      firstName: new FormControl(digiUserRawValue.firstName),
      lastName: new FormControl(digiUserRawValue.lastName),
      userName: new FormControl(digiUserRawValue.userName),
      email: new FormControl(digiUserRawValue.email),
      dob: new FormControl(digiUserRawValue.dob),
      age: new FormControl(digiUserRawValue.age),
      promoCode: new FormControl(digiUserRawValue.promoCode),
    });
  }

  getDigiUser(form: DigiUserFormGroup): IDigiUser | NewDigiUser {
    return form.getRawValue() as IDigiUser | NewDigiUser;
  }

  resetForm(form: DigiUserFormGroup, digiUser: DigiUserFormGroupInput): void {
    const digiUserRawValue = { ...this.getFormDefaults(), ...digiUser };
    form.reset(
      {
        ...digiUserRawValue,
        id: { value: digiUserRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): DigiUserFormDefaults {
    return {
      id: null,
    };
  }
}
