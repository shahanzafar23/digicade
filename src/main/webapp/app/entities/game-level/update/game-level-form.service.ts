import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IGameLevel, NewGameLevel } from '../game-level.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IGameLevel for edit and NewGameLevelFormGroupInput for create.
 */
type GameLevelFormGroupInput = IGameLevel | PartialWithRequiredKeyOf<NewGameLevel>;

type GameLevelFormDefaults = Pick<NewGameLevel, 'id'>;

type GameLevelFormGroupContent = {
  id: FormControl<IGameLevel['id'] | NewGameLevel['id']>;
  level: FormControl<IGameLevel['level']>;
  score: FormControl<IGameLevel['score']>;
  game: FormControl<IGameLevel['game']>;
};

export type GameLevelFormGroup = FormGroup<GameLevelFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class GameLevelFormService {
  createGameLevelFormGroup(gameLevel: GameLevelFormGroupInput = { id: null }): GameLevelFormGroup {
    const gameLevelRawValue = {
      ...this.getFormDefaults(),
      ...gameLevel,
    };
    return new FormGroup<GameLevelFormGroupContent>({
      id: new FormControl(
        { value: gameLevelRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      level: new FormControl(gameLevelRawValue.level),
      score: new FormControl(gameLevelRawValue.score),
      game: new FormControl(gameLevelRawValue.game),
    });
  }

  getGameLevel(form: GameLevelFormGroup): IGameLevel | NewGameLevel {
    return form.getRawValue() as IGameLevel | NewGameLevel;
  }

  resetForm(form: GameLevelFormGroup, gameLevel: GameLevelFormGroupInput): void {
    const gameLevelRawValue = { ...this.getFormDefaults(), ...gameLevel };
    form.reset(
      {
        ...gameLevelRawValue,
        id: { value: gameLevelRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): GameLevelFormDefaults {
    return {
      id: null,
    };
  }
}
