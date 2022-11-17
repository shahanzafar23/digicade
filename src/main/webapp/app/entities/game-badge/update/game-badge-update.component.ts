import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { GameBadgeFormService, GameBadgeFormGroup } from './game-badge-form.service';
import { IGameBadge } from '../game-badge.model';
import { GameBadgeService } from '../service/game-badge.service';
import { IGame } from 'app/entities/game/game.model';
import { GameService } from 'app/entities/game/service/game.service';
import { IPlayer } from 'app/entities/player/player.model';
import { PlayerService } from 'app/entities/player/service/player.service';

@Component({
  selector: 'jhi-game-badge-update',
  templateUrl: './game-badge-update.component.html',
})
export class GameBadgeUpdateComponent implements OnInit {
  isSaving = false;
  gameBadge: IGameBadge | null = null;

  gamesSharedCollection: IGame[] = [];
  playersSharedCollection: IPlayer[] = [];

  editForm: GameBadgeFormGroup = this.gameBadgeFormService.createGameBadgeFormGroup();

  constructor(
    protected gameBadgeService: GameBadgeService,
    protected gameBadgeFormService: GameBadgeFormService,
    protected gameService: GameService,
    protected playerService: PlayerService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareGame = (o1: IGame | null, o2: IGame | null): boolean => this.gameService.compareGame(o1, o2);

  comparePlayer = (o1: IPlayer | null, o2: IPlayer | null): boolean => this.playerService.comparePlayer(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ gameBadge }) => {
      this.gameBadge = gameBadge;
      if (gameBadge) {
        this.updateForm(gameBadge);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const gameBadge = this.gameBadgeFormService.getGameBadge(this.editForm);
    if (gameBadge.id !== null) {
      this.subscribeToSaveResponse(this.gameBadgeService.update(gameBadge));
    } else {
      this.subscribeToSaveResponse(this.gameBadgeService.create(gameBadge));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IGameBadge>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(gameBadge: IGameBadge): void {
    this.gameBadge = gameBadge;
    this.gameBadgeFormService.resetForm(this.editForm, gameBadge);

    this.gamesSharedCollection = this.gameService.addGameToCollectionIfMissing<IGame>(this.gamesSharedCollection, gameBadge.game);
    this.playersSharedCollection = this.playerService.addPlayerToCollectionIfMissing<IPlayer>(
      this.playersSharedCollection,
      gameBadge.player
    );
  }

  protected loadRelationshipsOptions(): void {
    this.gameService
      .query()
      .pipe(map((res: HttpResponse<IGame[]>) => res.body ?? []))
      .pipe(map((games: IGame[]) => this.gameService.addGameToCollectionIfMissing<IGame>(games, this.gameBadge?.game)))
      .subscribe((games: IGame[]) => (this.gamesSharedCollection = games));

    this.playerService
      .query()
      .pipe(map((res: HttpResponse<IPlayer[]>) => res.body ?? []))
      .pipe(map((players: IPlayer[]) => this.playerService.addPlayerToCollectionIfMissing<IPlayer>(players, this.gameBadge?.player)))
      .subscribe((players: IPlayer[]) => (this.playersSharedCollection = players));
  }
}
