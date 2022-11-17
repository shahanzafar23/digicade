import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { GameScoreFormService, GameScoreFormGroup } from './game-score-form.service';
import { IGameScore } from '../game-score.model';
import { GameScoreService } from '../service/game-score.service';
import { IGame } from 'app/entities/game/game.model';
import { GameService } from 'app/entities/game/service/game.service';
import { IPlayer } from 'app/entities/player/player.model';
import { PlayerService } from 'app/entities/player/service/player.service';

@Component({
  selector: 'jhi-game-score-update',
  templateUrl: './game-score-update.component.html',
})
export class GameScoreUpdateComponent implements OnInit {
  isSaving = false;
  gameScore: IGameScore | null = null;

  gamesSharedCollection: IGame[] = [];
  playersSharedCollection: IPlayer[] = [];

  editForm: GameScoreFormGroup = this.gameScoreFormService.createGameScoreFormGroup();

  constructor(
    protected gameScoreService: GameScoreService,
    protected gameScoreFormService: GameScoreFormService,
    protected gameService: GameService,
    protected playerService: PlayerService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareGame = (o1: IGame | null, o2: IGame | null): boolean => this.gameService.compareGame(o1, o2);

  comparePlayer = (o1: IPlayer | null, o2: IPlayer | null): boolean => this.playerService.comparePlayer(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ gameScore }) => {
      this.gameScore = gameScore;
      if (gameScore) {
        this.updateForm(gameScore);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const gameScore = this.gameScoreFormService.getGameScore(this.editForm);
    if (gameScore.id !== null) {
      this.subscribeToSaveResponse(this.gameScoreService.update(gameScore));
    } else {
      this.subscribeToSaveResponse(this.gameScoreService.create(gameScore));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IGameScore>>): void {
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

  protected updateForm(gameScore: IGameScore): void {
    this.gameScore = gameScore;
    this.gameScoreFormService.resetForm(this.editForm, gameScore);

    this.gamesSharedCollection = this.gameService.addGameToCollectionIfMissing<IGame>(this.gamesSharedCollection, gameScore.game);
    this.playersSharedCollection = this.playerService.addPlayerToCollectionIfMissing<IPlayer>(
      this.playersSharedCollection,
      gameScore.player
    );
  }

  protected loadRelationshipsOptions(): void {
    this.gameService
      .query()
      .pipe(map((res: HttpResponse<IGame[]>) => res.body ?? []))
      .pipe(map((games: IGame[]) => this.gameService.addGameToCollectionIfMissing<IGame>(games, this.gameScore?.game)))
      .subscribe((games: IGame[]) => (this.gamesSharedCollection = games));

    this.playerService
      .query()
      .pipe(map((res: HttpResponse<IPlayer[]>) => res.body ?? []))
      .pipe(map((players: IPlayer[]) => this.playerService.addPlayerToCollectionIfMissing<IPlayer>(players, this.gameScore?.player)))
      .subscribe((players: IPlayer[]) => (this.playersSharedCollection = players));
  }
}
