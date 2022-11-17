import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../digi-user.test-samples';

import { DigiUserFormService } from './digi-user-form.service';

describe('DigiUser Form Service', () => {
  let service: DigiUserFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DigiUserFormService);
  });

  describe('Service methods', () => {
    describe('createDigiUserFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createDigiUserFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            firstName: expect.any(Object),
            lastName: expect.any(Object),
            userName: expect.any(Object),
            email: expect.any(Object),
            dob: expect.any(Object),
            age: expect.any(Object),
            promoCode: expect.any(Object),
          })
        );
      });

      it('passing IDigiUser should create a new form with FormGroup', () => {
        const formGroup = service.createDigiUserFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            firstName: expect.any(Object),
            lastName: expect.any(Object),
            userName: expect.any(Object),
            email: expect.any(Object),
            dob: expect.any(Object),
            age: expect.any(Object),
            promoCode: expect.any(Object),
          })
        );
      });
    });

    describe('getDigiUser', () => {
      it('should return NewDigiUser for default DigiUser initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createDigiUserFormGroup(sampleWithNewData);

        const digiUser = service.getDigiUser(formGroup) as any;

        expect(digiUser).toMatchObject(sampleWithNewData);
      });

      it('should return NewDigiUser for empty DigiUser initial value', () => {
        const formGroup = service.createDigiUserFormGroup();

        const digiUser = service.getDigiUser(formGroup) as any;

        expect(digiUser).toMatchObject({});
      });

      it('should return IDigiUser', () => {
        const formGroup = service.createDigiUserFormGroup(sampleWithRequiredData);

        const digiUser = service.getDigiUser(formGroup) as any;

        expect(digiUser).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IDigiUser should not enable id FormControl', () => {
        const formGroup = service.createDigiUserFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewDigiUser should disable id FormControl', () => {
        const formGroup = service.createDigiUserFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
