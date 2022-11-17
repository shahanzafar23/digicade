import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../game-badge.test-samples';

import { GameBadgeFormService } from './game-badge-form.service';

describe('GameBadge Form Service', () => {
  let service: GameBadgeFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(GameBadgeFormService);
  });

  describe('Service methods', () => {
    describe('createGameBadgeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createGameBadgeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            logoUrl: expect.any(Object),
            game: expect.any(Object),
            player: expect.any(Object),
          })
        );
      });

      it('passing IGameBadge should create a new form with FormGroup', () => {
        const formGroup = service.createGameBadgeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            logoUrl: expect.any(Object),
            game: expect.any(Object),
            player: expect.any(Object),
          })
        );
      });
    });

    describe('getGameBadge', () => {
      it('should return NewGameBadge for default GameBadge initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createGameBadgeFormGroup(sampleWithNewData);

        const gameBadge = service.getGameBadge(formGroup) as any;

        expect(gameBadge).toMatchObject(sampleWithNewData);
      });

      it('should return NewGameBadge for empty GameBadge initial value', () => {
        const formGroup = service.createGameBadgeFormGroup();

        const gameBadge = service.getGameBadge(formGroup) as any;

        expect(gameBadge).toMatchObject({});
      });

      it('should return IGameBadge', () => {
        const formGroup = service.createGameBadgeFormGroup(sampleWithRequiredData);

        const gameBadge = service.getGameBadge(formGroup) as any;

        expect(gameBadge).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IGameBadge should not enable id FormControl', () => {
        const formGroup = service.createGameBadgeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewGameBadge should disable id FormControl', () => {
        const formGroup = service.createGameBadgeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
