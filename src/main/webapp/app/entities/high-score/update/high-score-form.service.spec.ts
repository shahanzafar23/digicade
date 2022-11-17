import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../high-score.test-samples';

import { HighScoreFormService } from './high-score-form.service';

describe('HighScore Form Service', () => {
  let service: HighScoreFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(HighScoreFormService);
  });

  describe('Service methods', () => {
    describe('createHighScoreFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createHighScoreFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            highestScore: expect.any(Object),
            game: expect.any(Object),
            player: expect.any(Object),
          })
        );
      });

      it('passing IHighScore should create a new form with FormGroup', () => {
        const formGroup = service.createHighScoreFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            highestScore: expect.any(Object),
            game: expect.any(Object),
            player: expect.any(Object),
          })
        );
      });
    });

    describe('getHighScore', () => {
      it('should return NewHighScore for default HighScore initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createHighScoreFormGroup(sampleWithNewData);

        const highScore = service.getHighScore(formGroup) as any;

        expect(highScore).toMatchObject(sampleWithNewData);
      });

      it('should return NewHighScore for empty HighScore initial value', () => {
        const formGroup = service.createHighScoreFormGroup();

        const highScore = service.getHighScore(formGroup) as any;

        expect(highScore).toMatchObject({});
      });

      it('should return IHighScore', () => {
        const formGroup = service.createHighScoreFormGroup(sampleWithRequiredData);

        const highScore = service.getHighScore(formGroup) as any;

        expect(highScore).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IHighScore should not enable id FormControl', () => {
        const formGroup = service.createHighScoreFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewHighScore should disable id FormControl', () => {
        const formGroup = service.createHighScoreFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
