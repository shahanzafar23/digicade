import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../coin-package.test-samples';

import { CoinPackageFormService } from './coin-package-form.service';

describe('CoinPackage Form Service', () => {
  let service: CoinPackageFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CoinPackageFormService);
  });

  describe('Service methods', () => {
    describe('createCoinPackageFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCoinPackageFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            coins: expect.any(Object),
            cost: expect.any(Object),
          })
        );
      });

      it('passing ICoinPackage should create a new form with FormGroup', () => {
        const formGroup = service.createCoinPackageFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            coins: expect.any(Object),
            cost: expect.any(Object),
          })
        );
      });
    });

    describe('getCoinPackage', () => {
      it('should return NewCoinPackage for default CoinPackage initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createCoinPackageFormGroup(sampleWithNewData);

        const coinPackage = service.getCoinPackage(formGroup) as any;

        expect(coinPackage).toMatchObject(sampleWithNewData);
      });

      it('should return NewCoinPackage for empty CoinPackage initial value', () => {
        const formGroup = service.createCoinPackageFormGroup();

        const coinPackage = service.getCoinPackage(formGroup) as any;

        expect(coinPackage).toMatchObject({});
      });

      it('should return ICoinPackage', () => {
        const formGroup = service.createCoinPackageFormGroup(sampleWithRequiredData);

        const coinPackage = service.getCoinPackage(formGroup) as any;

        expect(coinPackage).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICoinPackage should not enable id FormControl', () => {
        const formGroup = service.createCoinPackageFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCoinPackage should disable id FormControl', () => {
        const formGroup = service.createCoinPackageFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
