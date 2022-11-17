import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IGameBadge, NewGameBadge } from '../game-badge.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IGameBadge for edit and NewGameBadgeFormGroupInput for create.
 */
type GameBadgeFormGroupInput = IGameBadge | PartialWithRequiredKeyOf<NewGameBadge>;

type GameBadgeFormDefaults = Pick<NewGameBadge, 'id'>;

type GameBadgeFormGroupContent = {
  id: FormControl<IGameBadge['id'] | NewGameBadge['id']>;
  logoUrl: FormControl<IGameBadge['logoUrl']>;
  game: FormControl<IGameBadge['game']>;
  player: FormControl<IGameBadge['player']>;
};

export type GameBadgeFormGroup = FormGroup<GameBadgeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class GameBadgeFormService {
  createGameBadgeFormGroup(gameBadge: GameBadgeFormGroupInput = { id: null }): GameBadgeFormGroup {
    const gameBadgeRawValue = {
      ...this.getFormDefaults(),
      ...gameBadge,
    };
    return new FormGroup<GameBadgeFormGroupContent>({
      id: new FormControl(
        { value: gameBadgeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      logoUrl: new FormControl(gameBadgeRawValue.logoUrl),
      game: new FormControl(gameBadgeRawValue.game),
      player: new FormControl(gameBadgeRawValue.player),
    });
  }

  getGameBadge(form: GameBadgeFormGroup): IGameBadge | NewGameBadge {
    return form.getRawValue() as IGameBadge | NewGameBadge;
  }

  resetForm(form: GameBadgeFormGroup, gameBadge: GameBadgeFormGroupInput): void {
    const gameBadgeRawValue = { ...this.getFormDefaults(), ...gameBadge };
    form.reset(
      {
        ...gameBadgeRawValue,
        id: { value: gameBadgeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): GameBadgeFormDefaults {
    return {
      id: null,
    };
  }
}
