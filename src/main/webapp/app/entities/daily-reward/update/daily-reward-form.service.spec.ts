import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../daily-reward.test-samples';

import { DailyRewardFormService } from './daily-reward-form.service';

describe('DailyReward Form Service', () => {
  let service: DailyRewardFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DailyRewardFormService);
  });

  describe('Service methods', () => {
    describe('createDailyRewardFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createDailyRewardFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            time: expect.any(Object),
            rewardType: expect.any(Object),
            amount: expect.any(Object),
            couponReward: expect.any(Object),
            nftReward: expect.any(Object),
          })
        );
      });

      it('passing IDailyReward should create a new form with FormGroup', () => {
        const formGroup = service.createDailyRewardFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            time: expect.any(Object),
            rewardType: expect.any(Object),
            amount: expect.any(Object),
            couponReward: expect.any(Object),
            nftReward: expect.any(Object),
          })
        );
      });
    });

    describe('getDailyReward', () => {
      it('should return NewDailyReward for default DailyReward initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createDailyRewardFormGroup(sampleWithNewData);

        const dailyReward = service.getDailyReward(formGroup) as any;

        expect(dailyReward).toMatchObject(sampleWithNewData);
      });

      it('should return NewDailyReward for empty DailyReward initial value', () => {
        const formGroup = service.createDailyRewardFormGroup();

        const dailyReward = service.getDailyReward(formGroup) as any;

        expect(dailyReward).toMatchObject({});
      });

      it('should return IDailyReward', () => {
        const formGroup = service.createDailyRewardFormGroup(sampleWithRequiredData);

        const dailyReward = service.getDailyReward(formGroup) as any;

        expect(dailyReward).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IDailyReward should not enable id FormControl', () => {
        const formGroup = service.createDailyRewardFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewDailyReward should disable id FormControl', () => {
        const formGroup = service.createDailyRewardFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
