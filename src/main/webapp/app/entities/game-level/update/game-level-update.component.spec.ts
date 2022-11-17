import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { GameLevelFormService } from './game-level-form.service';
import { GameLevelService } from '../service/game-level.service';
import { IGameLevel } from '../game-level.model';
import { IGame } from 'app/entities/game/game.model';
import { GameService } from 'app/entities/game/service/game.service';

import { GameLevelUpdateComponent } from './game-level-update.component';

describe('GameLevel Management Update Component', () => {
  let comp: GameLevelUpdateComponent;
  let fixture: ComponentFixture<GameLevelUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let gameLevelFormService: GameLevelFormService;
  let gameLevelService: GameLevelService;
  let gameService: GameService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [GameLevelUpdateComponent],
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
      .overrideTemplate(GameLevelUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(GameLevelUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    gameLevelFormService = TestBed.inject(GameLevelFormService);
    gameLevelService = TestBed.inject(GameLevelService);
    gameService = TestBed.inject(GameService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Game query and add missing value', () => {
      const gameLevel: IGameLevel = { id: 456 };
      const game: IGame = { id: 23093 };
      gameLevel.game = game;

      const gameCollection: IGame[] = [{ id: 65575 }];
      jest.spyOn(gameService, 'query').mockReturnValue(of(new HttpResponse({ body: gameCollection })));
      const additionalGames = [game];
      const expectedCollection: IGame[] = [...additionalGames, ...gameCollection];
      jest.spyOn(gameService, 'addGameToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ gameLevel });
      comp.ngOnInit();

      expect(gameService.query).toHaveBeenCalled();
      expect(gameService.addGameToCollectionIfMissing).toHaveBeenCalledWith(
        gameCollection,
        ...additionalGames.map(expect.objectContaining)
      );
      expect(comp.gamesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const gameLevel: IGameLevel = { id: 456 };
      const game: IGame = { id: 31006 };
      gameLevel.game = game;

      activatedRoute.data = of({ gameLevel });
      comp.ngOnInit();

      expect(comp.gamesSharedCollection).toContain(game);
      expect(comp.gameLevel).toEqual(gameLevel);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IGameLevel>>();
      const gameLevel = { id: 123 };
      jest.spyOn(gameLevelFormService, 'getGameLevel').mockReturnValue(gameLevel);
      jest.spyOn(gameLevelService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ gameLevel });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: gameLevel }));
      saveSubject.complete();

      // THEN
      expect(gameLevelFormService.getGameLevel).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(gameLevelService.update).toHaveBeenCalledWith(expect.objectContaining(gameLevel));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IGameLevel>>();
      const gameLevel = { id: 123 };
      jest.spyOn(gameLevelFormService, 'getGameLevel').mockReturnValue({ id: null });
      jest.spyOn(gameLevelService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ gameLevel: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: gameLevel }));
      saveSubject.complete();

      // THEN
      expect(gameLevelFormService.getGameLevel).toHaveBeenCalled();
      expect(gameLevelService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IGameLevel>>();
      const gameLevel = { id: 123 };
      jest.spyOn(gameLevelService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ gameLevel });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(gameLevelService.update).toHaveBeenCalled();
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
  });
});
