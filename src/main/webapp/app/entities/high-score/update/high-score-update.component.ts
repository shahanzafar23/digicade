import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { HighScoreFormService, HighScoreFormGroup } from './high-score-form.service';
import { IHighScore } from '../high-score.model';
import { HighScoreService } from '../service/high-score.service';
import { IGame } from 'app/entities/game/game.model';
import { GameService } from 'app/entities/game/service/game.service';
import { IPlayer } from 'app/entities/player/player.model';
import { PlayerService } from 'app/entities/player/service/player.service';

@Component({
  selector: 'jhi-high-score-update',
  templateUrl: './high-score-update.component.html',
})
export class HighScoreUpdateComponent implements OnInit {
  isSaving = false;
  highScore: IHighScore | null = null;

  gamesSharedCollection: IGame[] = [];
  playersSharedCollection: IPlayer[] = [];

  editForm: HighScoreFormGroup = this.highScoreFormService.createHighScoreFormGroup();

  constructor(
    protected highScoreService: HighScoreService,
    protected highScoreFormService: HighScoreFormService,
    protected gameService: GameService,
    protected playerService: PlayerService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareGame = (o1: IGame | null, o2: IGame | null): boolean => this.gameService.compareGame(o1, o2);

  comparePlayer = (o1: IPlayer | null, o2: IPlayer | null): boolean => this.playerService.comparePlayer(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ highScore }) => {
      this.highScore = highScore;
      if (highScore) {
        this.updateForm(highScore);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const highScore = this.highScoreFormService.getHighScore(this.editForm);
    if (highScore.id !== null) {
      this.subscribeToSaveResponse(this.highScoreService.update(highScore));
    } else {
      this.subscribeToSaveResponse(this.highScoreService.create(highScore));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IHighScore>>): void {
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

  protected updateForm(highScore: IHighScore): void {
    this.highScore = highScore;
    this.highScoreFormService.resetForm(this.editForm, highScore);

    this.gamesSharedCollection = this.gameService.addGameToCollectionIfMissing<IGame>(this.gamesSharedCollection, highScore.game);
    this.playersSharedCollection = this.playerService.addPlayerToCollectionIfMissing<IPlayer>(
      this.playersSharedCollection,
      highScore.player
    );
  }

  protected loadRelationshipsOptions(): void {
    this.gameService
      .query()
      .pipe(map((res: HttpResponse<IGame[]>) => res.body ?? []))
      .pipe(map((games: IGame[]) => this.gameService.addGameToCollectionIfMissing<IGame>(games, this.highScore?.game)))
      .subscribe((games: IGame[]) => (this.gamesSharedCollection = games));

    this.playerService
      .query()
      .pipe(map((res: HttpResponse<IPlayer[]>) => res.body ?? []))
      .pipe(map((players: IPlayer[]) => this.playerService.addPlayerToCollectionIfMissing<IPlayer>(players, this.highScore?.player)))
      .subscribe((players: IPlayer[]) => (this.playersSharedCollection = players));
  }
}
