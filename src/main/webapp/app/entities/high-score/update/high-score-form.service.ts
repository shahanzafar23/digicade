import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IHighScore, NewHighScore } from '../high-score.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IHighScore for edit and NewHighScoreFormGroupInput for create.
 */
type HighScoreFormGroupInput = IHighScore | PartialWithRequiredKeyOf<NewHighScore>;

type HighScoreFormDefaults = Pick<NewHighScore, 'id'>;

type HighScoreFormGroupContent = {
  id: FormControl<IHighScore['id'] | NewHighScore['id']>;
  highestScore: FormControl<IHighScore['highestScore']>;
  game: FormControl<IHighScore['game']>;
  player: FormControl<IHighScore['player']>;
};

export type HighScoreFormGroup = FormGroup<HighScoreFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class HighScoreFormService {
  createHighScoreFormGroup(highScore: HighScoreFormGroupInput = { id: null }): HighScoreFormGroup {
    const highScoreRawValue = {
      ...this.getFormDefaults(),
      ...highScore,
    };
    return new FormGroup<HighScoreFormGroupContent>({
      id: new FormControl(
        { value: highScoreRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      highestScore: new FormControl(highScoreRawValue.highestScore),
      game: new FormControl(highScoreRawValue.game),
      player: new FormControl(highScoreRawValue.player),
    });
  }

  getHighScore(form: HighScoreFormGroup): IHighScore | NewHighScore {
    return form.getRawValue() as IHighScore | NewHighScore;
  }

  resetForm(form: HighScoreFormGroup, highScore: HighScoreFormGroupInput): void {
    const highScoreRawValue = { ...this.getFormDefaults(), ...highScore };
    form.reset(
      {
        ...highScoreRawValue,
        id: { value: highScoreRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): HighScoreFormDefaults {
    return {
      id: null,
    };
  }
}
