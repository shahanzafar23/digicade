import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../player-coupon-reward.test-samples';

import { PlayerCouponRewardFormService } from './player-coupon-reward-form.service';

describe('PlayerCouponReward Form Service', () => {
  let service: PlayerCouponRewardFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PlayerCouponRewardFormService);
  });

  describe('Service methods', () => {
    describe('createPlayerCouponRewardFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPlayerCouponRewardFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            date: expect.any(Object),
            status: expect.any(Object),
            player: expect.any(Object),
            couponReward: expect.any(Object),
          })
        );
      });

      it('passing IPlayerCouponReward should create a new form with FormGroup', () => {
        const formGroup = service.createPlayerCouponRewardFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            date: expect.any(Object),
            status: expect.any(Object),
            player: expect.any(Object),
            couponReward: expect.any(Object),
          })
        );
      });
    });

    describe('getPlayerCouponReward', () => {
      it('should return NewPlayerCouponReward for default PlayerCouponReward initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createPlayerCouponRewardFormGroup(sampleWithNewData);

        const playerCouponReward = service.getPlayerCouponReward(formGroup) as any;

        expect(playerCouponReward).toMatchObject(sampleWithNewData);
      });

      it('should return NewPlayerCouponReward for empty PlayerCouponReward initial value', () => {
        const formGroup = service.createPlayerCouponRewardFormGroup();

        const playerCouponReward = service.getPlayerCouponReward(formGroup) as any;

        expect(playerCouponReward).toMatchObject({});
      });

      it('should return IPlayerCouponReward', () => {
        const formGroup = service.createPlayerCouponRewardFormGroup(sampleWithRequiredData);

        const playerCouponReward = service.getPlayerCouponReward(formGroup) as any;

        expect(playerCouponReward).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPlayerCouponReward should not enable id FormControl', () => {
        const formGroup = service.createPlayerCouponRewardFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPlayerCouponReward should disable id FormControl', () => {
        const formGroup = service.createPlayerCouponRewardFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
