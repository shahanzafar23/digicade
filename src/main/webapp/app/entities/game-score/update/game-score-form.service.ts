import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IGameScore, NewGameScore } from '../game-score.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IGameScore for edit and NewGameScoreFormGroupInput for create.
 */
type GameScoreFormGroupInput = IGameScore | PartialWithRequiredKeyOf<NewGameScore>;

type GameScoreFormDefaults = Pick<NewGameScore, 'id'>;

type GameScoreFormGroupContent = {
  id: FormControl<IGameScore['id'] | NewGameScore['id']>;
  score: FormControl<IGameScore['score']>;
  date: FormControl<IGameScore['date']>;
  game: FormControl<IGameScore['game']>;
  player: FormControl<IGameScore['player']>;
};

export type GameScoreFormGroup = FormGroup<GameScoreFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class GameScoreFormService {
  createGameScoreFormGroup(gameScore: GameScoreFormGroupInput = { id: null }): GameScoreFormGroup {
    const gameScoreRawValue = {
      ...this.getFormDefaults(),
      ...gameScore,
    };
    return new FormGroup<GameScoreFormGroupContent>({
      id: new FormControl(
        { value: gameScoreRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      score: new FormControl(gameScoreRawValue.score),
      date: new FormControl(gameScoreRawValue.date),
      game: new FormControl(gameScoreRawValue.game),
      player: new FormControl(gameScoreRawValue.player),
    });
  }

  getGameScore(form: GameScoreFormGroup): IGameScore | NewGameScore {
    return form.getRawValue() as IGameScore | NewGameScore;
  }

  resetForm(form: GameScoreFormGroup, gameScore: GameScoreFormGroupInput): void {
    const gameScoreRawValue = { ...this.getFormDefaults(), ...gameScore };
    form.reset(
      {
        ...gameScoreRawValue,
        id: { value: gameScoreRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): GameScoreFormDefaults {
    return {
      id: null,
    };
  }
}
