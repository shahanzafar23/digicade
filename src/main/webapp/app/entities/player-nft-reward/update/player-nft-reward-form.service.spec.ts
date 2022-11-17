import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../player-nft-reward.test-samples';

import { PlayerNftRewardFormService } from './player-nft-reward-form.service';

describe('PlayerNftReward Form Service', () => {
  let service: PlayerNftRewardFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PlayerNftRewardFormService);
  });

  describe('Service methods', () => {
    describe('createPlayerNftRewardFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPlayerNftRewardFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            date: expect.any(Object),
            player: expect.any(Object),
            nftReward: expect.any(Object),
          })
        );
      });

      it('passing IPlayerNftReward should create a new form with FormGroup', () => {
        const formGroup = service.createPlayerNftRewardFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            date: expect.any(Object),
            player: expect.any(Object),
            nftReward: expect.any(Object),
          })
        );
      });
    });

    describe('getPlayerNftReward', () => {
      it('should return NewPlayerNftReward for default PlayerNftReward initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createPlayerNftRewardFormGroup(sampleWithNewData);

        const playerNftReward = service.getPlayerNftReward(formGroup) as any;

        expect(playerNftReward).toMatchObject(sampleWithNewData);
      });

      it('should return NewPlayerNftReward for empty PlayerNftReward initial value', () => {
        const formGroup = service.createPlayerNftRewardFormGroup();

        const playerNftReward = service.getPlayerNftReward(formGroup) as any;

        expect(playerNftReward).toMatchObject({});
      });

      it('should return IPlayerNftReward', () => {
        const formGroup = service.createPlayerNftRewardFormGroup(sampleWithRequiredData);

        const playerNftReward = service.getPlayerNftReward(formGroup) as any;

        expect(playerNftReward).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPlayerNftReward should not enable id FormControl', () => {
        const formGroup = service.createPlayerNftRewardFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPlayerNftReward should disable id FormControl', () => {
        const formGroup = service.createPlayerNftRewardFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
