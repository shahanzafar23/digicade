import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../nft-reward.test-samples';

import { NftRewardFormService } from './nft-reward-form.service';

describe('NftReward Form Service', () => {
  let service: NftRewardFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(NftRewardFormService);
  });

  describe('Service methods', () => {
    describe('createNftRewardFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createNftRewardFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            tix: expect.any(Object),
            comp: expect.any(Object),
            imageUrl: expect.any(Object),
          })
        );
      });

      it('passing INftReward should create a new form with FormGroup', () => {
        const formGroup = service.createNftRewardFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            tix: expect.any(Object),
            comp: expect.any(Object),
            imageUrl: expect.any(Object),
          })
        );
      });
    });

    describe('getNftReward', () => {
      it('should return NewNftReward for default NftReward initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createNftRewardFormGroup(sampleWithNewData);

        const nftReward = service.getNftReward(formGroup) as any;

        expect(nftReward).toMatchObject(sampleWithNewData);
      });

      it('should return NewNftReward for empty NftReward initial value', () => {
        const formGroup = service.createNftRewardFormGroup();

        const nftReward = service.getNftReward(formGroup) as any;

        expect(nftReward).toMatchObject({});
      });

      it('should return INftReward', () => {
        const formGroup = service.createNftRewardFormGroup(sampleWithRequiredData);

        const nftReward = service.getNftReward(formGroup) as any;

        expect(nftReward).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing INftReward should not enable id FormControl', () => {
        const formGroup = service.createNftRewardFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewNftReward should disable id FormControl', () => {
        const formGroup = service.createNftRewardFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
