import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../game-level.test-samples';

import { GameLevelFormService } from './game-level-form.service';

describe('GameLevel Form Service', () => {
  let service: GameLevelFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(GameLevelFormService);
  });

  describe('Service methods', () => {
    describe('createGameLevelFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createGameLevelFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            level: expect.any(Object),
            score: expect.any(Object),
            game: expect.any(Object),
          })
        );
      });

      it('passing IGameLevel should create a new form with FormGroup', () => {
        const formGroup = service.createGameLevelFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            level: expect.any(Object),
            score: expect.any(Object),
            game: expect.any(Object),
          })
        );
      });
    });

    describe('getGameLevel', () => {
      it('should return NewGameLevel for default GameLevel initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createGameLevelFormGroup(sampleWithNewData);

        const gameLevel = service.getGameLevel(formGroup) as any;

        expect(gameLevel).toMatchObject(sampleWithNewData);
      });

      it('should return NewGameLevel for empty GameLevel initial value', () => {
        const formGroup = service.createGameLevelFormGroup();

        const gameLevel = service.getGameLevel(formGroup) as any;

        expect(gameLevel).toMatchObject({});
      });

      it('should return IGameLevel', () => {
        const formGroup = service.createGameLevelFormGroup(sampleWithRequiredData);

        const gameLevel = service.getGameLevel(formGroup) as any;

        expect(gameLevel).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IGameLevel should not enable id FormControl', () => {
        const formGroup = service.createGameLevelFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewGameLevel should disable id FormControl', () => {
        const formGroup = service.createGameLevelFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
