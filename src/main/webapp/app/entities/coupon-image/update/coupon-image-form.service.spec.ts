import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../coupon-image.test-samples';

import { CouponImageFormService } from './coupon-image-form.service';

describe('CouponImage Form Service', () => {
  let service: CouponImageFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CouponImageFormService);
  });

  describe('Service methods', () => {
    describe('createCouponImageFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCouponImageFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            imageUrl: expect.any(Object),
            couponReward: expect.any(Object),
          })
        );
      });

      it('passing ICouponImage should create a new form with FormGroup', () => {
        const formGroup = service.createCouponImageFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            imageUrl: expect.any(Object),
            couponReward: expect.any(Object),
          })
        );
      });
    });

    describe('getCouponImage', () => {
      it('should return NewCouponImage for default CouponImage initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createCouponImageFormGroup(sampleWithNewData);

        const couponImage = service.getCouponImage(formGroup) as any;

        expect(couponImage).toMatchObject(sampleWithNewData);
      });

      it('should return NewCouponImage for empty CouponImage initial value', () => {
        const formGroup = service.createCouponImageFormGroup();

        const couponImage = service.getCouponImage(formGroup) as any;

        expect(couponImage).toMatchObject({});
      });

      it('should return ICouponImage', () => {
        const formGroup = service.createCouponImageFormGroup(sampleWithRequiredData);

        const couponImage = service.getCouponImage(formGroup) as any;

        expect(couponImage).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICouponImage should not enable id FormControl', () => {
        const formGroup = service.createCouponImageFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCouponImage should disable id FormControl', () => {
        const formGroup = service.createCouponImageFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
