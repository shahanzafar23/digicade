import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { GameScoreFormService } from './game-score-form.service';
import { GameScoreService } from '../service/game-score.service';
import { IGameScore } from '../game-score.model';
import { IGame } from 'app/entities/game/game.model';
import { GameService } from 'app/entities/game/service/game.service';
import { IPlayer } from 'app/entities/player/player.model';
import { PlayerService } from 'app/entities/player/service/player.service';

import { GameScoreUpdateComponent } from './game-score-update.component';

describe('GameScore Management Update Component', () => {
  let comp: GameScoreUpdateComponent;
  let fixture: ComponentFixture<GameScoreUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let gameScoreFormService: GameScoreFormService;
  let gameScoreService: GameScoreService;
  let gameService: GameService;
  let playerService: PlayerService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [GameScoreUpdateComponent],
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
      .overrideTemplate(GameScoreUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(GameScoreUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    gameScoreFormService = TestBed.inject(GameScoreFormService);
    gameScoreService = TestBed.inject(GameScoreService);
    gameService = TestBed.inject(GameService);
    playerService = TestBed.inject(PlayerService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Game query and add missing value', () => {
      const gameScore: IGameScore = { id: 456 };
      const game: IGame = { id: 37644 };
      gameScore.game = game;

      const gameCollection: IGame[] = [{ id: 49603 }];
      jest.spyOn(gameService, 'query').mockReturnValue(of(new HttpResponse({ body: gameCollection })));
      const additionalGames = [game];
      const expectedCollection: IGame[] = [...additionalGames, ...gameCollection];
      jest.spyOn(gameService, 'addGameToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ gameScore });
      comp.ngOnInit();

      expect(gameService.query).toHaveBeenCalled();
      expect(gameService.addGameToCollectionIfMissing).toHaveBeenCalledWith(
        gameCollection,
        ...additionalGames.map(expect.objectContaining)
      );
      expect(comp.gamesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Player query and add missing value', () => {
      const gameScore: IGameScore = { id: 456 };
      const player: IPlayer = { id: 97256 };
      gameScore.player = player;

      const playerCollection: IPlayer[] = [{ id: 41585 }];
      jest.spyOn(playerService, 'query').mockReturnValue(of(new HttpResponse({ body: playerCollection })));
      const additionalPlayers = [player];
      const expectedCollection: IPlayer[] = [...additionalPlayers, ...playerCollection];
      jest.spyOn(playerService, 'addPlayerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ gameScore });
      comp.ngOnInit();

      expect(playerService.query).toHaveBeenCalled();
      expect(playerService.addPlayerToCollectionIfMissing).toHaveBeenCalledWith(
        playerCollection,
        ...additionalPlayers.map(expect.objectContaining)
      );
      expect(comp.playersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const gameScore: IGameScore = { id: 456 };
      const game: IGame = { id: 4901 };
      gameScore.game = game;
      const player: IPlayer = { id: 67594 };
      gameScore.player = player;

      activatedRoute.data = of({ gameScore });
      comp.ngOnInit();

      expect(comp.gamesSharedCollection).toContain(game);
      expect(comp.playersSharedCollection).toContain(player);
      expect(comp.gameScore).toEqual(gameScore);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IGameScore>>();
      const gameScore = { id: 123 };
      jest.spyOn(gameScoreFormService, 'getGameScore').mockReturnValue(gameScore);
      jest.spyOn(gameScoreService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ gameScore });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: gameScore }));
      saveSubject.complete();

      // THEN
      expect(gameScoreFormService.getGameScore).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(gameScoreService.update).toHaveBeenCalledWith(expect.objectContaining(gameScore));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IGameScore>>();
      const gameScore = { id: 123 };
      jest.spyOn(gameScoreFormService, 'getGameScore').mockReturnValue({ id: null });
      jest.spyOn(gameScoreService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ gameScore: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: gameScore }));
      saveSubject.complete();

      // THEN
      expect(gameScoreFormService.getGameScore).toHaveBeenCalled();
      expect(gameScoreService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IGameScore>>();
      const gameScore = { id: 123 };
      jest.spyOn(gameScoreService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ gameScore });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(gameScoreService.update).toHaveBeenCalled();
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
