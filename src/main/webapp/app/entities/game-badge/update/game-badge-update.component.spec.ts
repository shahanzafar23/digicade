import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { GameBadgeFormService } from './game-badge-form.service';
import { GameBadgeService } from '../service/game-badge.service';
import { IGameBadge } from '../game-badge.model';
import { IGame } from 'app/entities/game/game.model';
import { GameService } from 'app/entities/game/service/game.service';
import { IPlayer } from 'app/entities/player/player.model';
import { PlayerService } from 'app/entities/player/service/player.service';

import { GameBadgeUpdateComponent } from './game-badge-update.component';

describe('GameBadge Management Update Component', () => {
  let comp: GameBadgeUpdateComponent;
  let fixture: ComponentFixture<GameBadgeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let gameBadgeFormService: GameBadgeFormService;
  let gameBadgeService: GameBadgeService;
  let gameService: GameService;
  let playerService: PlayerService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [GameBadgeUpdateComponent],
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
      .overrideTemplate(GameBadgeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(GameBadgeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    gameBadgeFormService = TestBed.inject(GameBadgeFormService);
    gameBadgeService = TestBed.inject(GameBadgeService);
    gameService = TestBed.inject(GameService);
    playerService = TestBed.inject(PlayerService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Game query and add missing value', () => {
      const gameBadge: IGameBadge = { id: 456 };
      const game: IGame = { id: 37627 };
      gameBadge.game = game;

      const gameCollection: IGame[] = [{ id: 74517 }];
      jest.spyOn(gameService, 'query').mockReturnValue(of(new HttpResponse({ body: gameCollection })));
      const additionalGames = [game];
      const expectedCollection: IGame[] = [...additionalGames, ...gameCollection];
      jest.spyOn(gameService, 'addGameToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ gameBadge });
      comp.ngOnInit();

      expect(gameService.query).toHaveBeenCalled();
      expect(gameService.addGameToCollectionIfMissing).toHaveBeenCalledWith(
        gameCollection,
        ...additionalGames.map(expect.objectContaining)
      );
      expect(comp.gamesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Player query and add missing value', () => {
      const gameBadge: IGameBadge = { id: 456 };
      const player: IPlayer = { id: 67162 };
      gameBadge.player = player;

      const playerCollection: IPlayer[] = [{ id: 85949 }];
      jest.spyOn(playerService, 'query').mockReturnValue(of(new HttpResponse({ body: playerCollection })));
      const additionalPlayers = [player];
      const expectedCollection: IPlayer[] = [...additionalPlayers, ...playerCollection];
      jest.spyOn(playerService, 'addPlayerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ gameBadge });
      comp.ngOnInit();

      expect(playerService.query).toHaveBeenCalled();
      expect(playerService.addPlayerToCollectionIfMissing).toHaveBeenCalledWith(
        playerCollection,
        ...additionalPlayers.map(expect.objectContaining)
      );
      expect(comp.playersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const gameBadge: IGameBadge = { id: 456 };
      const game: IGame = { id: 42062 };
      gameBadge.game = game;
      const player: IPlayer = { id: 1744 };
      gameBadge.player = player;

      activatedRoute.data = of({ gameBadge });
      comp.ngOnInit();

      expect(comp.gamesSharedCollection).toContain(game);
      expect(comp.playersSharedCollection).toContain(player);
      expect(comp.gameBadge).toEqual(gameBadge);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IGameBadge>>();
      const gameBadge = { id: 123 };
      jest.spyOn(gameBadgeFormService, 'getGameBadge').mockReturnValue(gameBadge);
      jest.spyOn(gameBadgeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ gameBadge });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: gameBadge }));
      saveSubject.complete();

      // THEN
      expect(gameBadgeFormService.getGameBadge).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(gameBadgeService.update).toHaveBeenCalledWith(expect.objectContaining(gameBadge));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IGameBadge>>();
      const gameBadge = { id: 123 };
      jest.spyOn(gameBadgeFormService, 'getGameBadge').mockReturnValue({ id: null });
      jest.spyOn(gameBadgeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ gameBadge: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: gameBadge }));
      saveSubject.complete();

      // THEN
      expect(gameBadgeFormService.getGameBadge).toHaveBeenCalled();
      expect(gameBadgeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IGameBadge>>();
      const gameBadge = { id: 123 };
      jest.spyOn(gameBadgeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ gameBadge });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(gameBadgeService.update).toHaveBeenCalled();
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
