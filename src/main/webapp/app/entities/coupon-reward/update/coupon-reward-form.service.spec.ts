import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../coupon-reward.test-samples';

import { CouponRewardFormService } from './coupon-reward-form.service';

describe('CouponReward Form Service', () => {
  let service: CouponRewardFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CouponRewardFormService);
  });

  describe('Service methods', () => {
    describe('createCouponRewardFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCouponRewardFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            description: expect.any(Object),
            location: expect.any(Object),
            tix: expect.any(Object),
            comp: expect.any(Object),
            expiry: expect.any(Object),
          })
        );
      });

      it('passing ICouponReward should create a new form with FormGroup', () => {
        const formGroup = service.createCouponRewardFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            description: expect.any(Object),
            location: expect.any(Object),
            tix: expect.any(Object),
            comp: expect.any(Object),
            expiry: expect.any(Object),
          })
        );
      });
    });

    describe('getCouponReward', () => {
      it('should return NewCouponReward for default CouponReward initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createCouponRewardFormGroup(sampleWithNewData);

        const couponReward = service.getCouponReward(formGroup) as any;

        expect(couponReward).toMatchObject(sampleWithNewData);
      });

      it('should return NewCouponReward for empty CouponReward initial value', () => {
        const formGroup = service.createCouponRewardFormGroup();

        const couponReward = service.getCouponReward(formGroup) as any;

        expect(couponReward).toMatchObject({});
      });

      it('should return ICouponReward', () => {
        const formGroup = service.createCouponRewardFormGroup(sampleWithRequiredData);

        const couponReward = service.getCouponReward(formGroup) as any;

        expect(couponReward).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICouponReward should not enable id FormControl', () => {
        const formGroup = service.createCouponRewardFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCouponReward should disable id FormControl', () => {
        const formGroup = service.createCouponRewardFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
