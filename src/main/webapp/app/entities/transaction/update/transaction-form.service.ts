import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ITransaction, NewTransaction } from '../transaction.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITransaction for edit and NewTransactionFormGroupInput for create.
 */
type TransactionFormGroupInput = ITransaction | PartialWithRequiredKeyOf<NewTransaction>;

type TransactionFormDefaults = Pick<NewTransaction, 'id'>;

type TransactionFormGroupContent = {
  id: FormControl<ITransaction['id'] | NewTransaction['id']>;
  date: FormControl<ITransaction['date']>;
  player: FormControl<ITransaction['player']>;
  coinPackage: FormControl<ITransaction['coinPackage']>;
};

export type TransactionFormGroup = FormGroup<TransactionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TransactionFormService {
  createTransactionFormGroup(transaction: TransactionFormGroupInput = { id: null }): TransactionFormGroup {
    const transactionRawValue = {
      ...this.getFormDefaults(),
      ...transaction,
    };
    return new FormGroup<TransactionFormGroupContent>({
      id: new FormControl(
        { value: transactionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      date: new FormControl(transactionRawValue.date),
      player: new FormControl(transactionRawValue.player),
      coinPackage: new FormControl(transactionRawValue.coinPackage),
    });
  }

  getTransaction(form: TransactionFormGroup): ITransaction | NewTransaction {
    return form.getRawValue() as ITransaction | NewTransaction;
  }

  resetForm(form: TransactionFormGroup, transaction: TransactionFormGroupInput): void {
    const transactionRawValue = { ...this.getFormDefaults(), ...transaction };
    form.reset(
      {
        ...transactionRawValue,
        id: { value: transactionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): TransactionFormDefaults {
    return {
      id: null,
    };
  }
}
