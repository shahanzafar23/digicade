import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { HighScoreFormService } from './high-score-form.service';
import { HighScoreService } from '../service/high-score.service';
import { IHighScore } from '../high-score.model';
import { IGame } from 'app/entities/game/game.model';
import { GameService } from 'app/entities/game/service/game.service';
import { IPlayer } from 'app/entities/player/player.model';
import { PlayerService } from 'app/entities/player/service/player.service';

import { HighScoreUpdateComponent } from './high-score-update.component';

describe('HighScore Management Update Component', () => {
  let comp: HighScoreUpdateComponent;
  let fixture: ComponentFixture<HighScoreUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let highScoreFormService: HighScoreFormService;
  let highScoreService: HighScoreService;
  let gameService: GameService;
  let playerService: PlayerService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [HighScoreUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(HighScoreUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(HighScoreUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    highScoreFormService = TestBed.inject(HighScoreFormService);
    highScoreService = TestBed.inject(HighScoreService);
    gameService = TestBed.inject(GameService);
    playerService = TestBed.inject(PlayerService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Game query and add missing value', () => {
      const highScore: IHighScore = { id: 456 };
      const game: IGame = { id: 29505 };
      highScore.game = game;

      const gameCollection: IGame[] = [{ id: 99526 }];
      jest.spyOn(gameService, 'query').mockReturnValue(of(new HttpResponse({ body: gameCollection })));
      const additionalGames = [game];
      const expectedCollection: IGame[] = [...additionalGames, ...gameCollection];
      jest.spyOn(gameService, 'addGameToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ highScore });
      comp.ngOnInit();

      expect(gameService.query).toHaveBeenCalled();
      expect(gameService.addGameToCollectionIfMissing).toHaveBeenCalledWith(
        gameCollection,
        ...additionalGames.map(expect.objectContaining)
      );
      expect(comp.gamesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Player query and add missing value', () => {
      const highScore: IHighScore = { id: 456 };
      const player: IPlayer = { id: 14086 };
      highScore.player = player;

      const playerCollection: IPlayer[] = [{ id: 29278 }];
      jest.spyOn(playerService, 'query').mockReturnValue(of(new HttpResponse({ body: playerCollection })));
      const additionalPlayers = [player];
      const expectedCollection: IPlayer[] = [...additionalPlayers, ...playerCollection];
      jest.spyOn(playerService, 'addPlayerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ highScore });
      comp.ngOnInit();

      expect(playerService.query).toHaveBeenCalled();
      expect(playerService.addPlayerToCollectionIfMissing).toHaveBeenCalledWith(
        playerCollection,
        ...additionalPlayers.map(expect.objectContaining)
      );
      expect(comp.playersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const highScore: IHighScore = { id: 456 };
      const game: IGame = { id: 82991 };
      highScore.game = game;
      const player: IPlayer = { id: 42990 };
      highScore.player = player;

      activatedRoute.data = of({ highScore });
      comp.ngOnInit();

      expect(comp.gamesSharedCollection).toContain(game);
      expect(comp.playersSharedCollection).toContain(player);
      expect(comp.highScore).toEqual(highScore);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IHighScore>>();
      const highScore = { id: 123 };
      jest.spyOn(highScoreFormService, 'getHighScore').mockReturnValue(highScore);
      jest.spyOn(highScoreService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ highScore });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: highScore }));
      saveSubject.complete();

      // THEN
      expect(highScoreFormService.getHighScore).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(highScoreService.update).toHaveBeenCalledWith(expect.objectContaining(highScore));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IHighScore>>();
      const highScore = { id: 123 };
      jest.spyOn(highScoreFormService, 'getHighScore').mockReturnValue({ id: null });
      jest.spyOn(highScoreService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ highScore: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: highScore }));
      saveSubject.complete();

      // THEN
      expect(highScoreFormService.getHighScore).toHaveBeenCalled();
      expect(highScoreService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IHighScore>>();
      const highScore = { id: 123 };
      jest.spyOn(highScoreService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ highScore });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(highScoreService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareGame', () => {
      it('Should forward to gameService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(gameService, 'compareGame');
        comp.compareGame(entity, entity2);
        expect(gameService.compareGame).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('comparePlayer', () => {
      it('Should forward to playerService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(playerService, 'comparePlayer');
        comp.comparePlayer(entity, entity2);
        expect(playerService.comparePlayer).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
