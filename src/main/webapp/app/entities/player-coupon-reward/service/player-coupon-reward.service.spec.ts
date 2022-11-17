import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IPlayerCouponReward } from '../player-coupon-reward.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../player-coupon-reward.test-samples';

import { PlayerCouponRewardService, RestPlayerCouponReward } from './player-coupon-reward.service';

const requireRestSample: RestPlayerCouponReward = {
  ...sampleWithRequiredData,
  date: sampleWithRequiredData.date?.format(DATE_FORMAT),
};

describe('PlayerCouponReward Service', () => {
  let service: PlayerCouponRewardService;
  let httpMock: HttpTestingController;
  let expectedResult: IPlayerCouponReward | IPlayerCouponReward[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PlayerCouponRewardService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a PlayerCouponReward', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const playerCouponReward = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(playerCouponReward).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PlayerCouponReward', () => {
      const playerCouponReward = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(playerCouponReward).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PlayerCouponReward', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PlayerCouponReward', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a PlayerCouponReward', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addPlayerCouponRewardToCollectionIfMissing', () => {
      it('should add a PlayerCouponReward to an empty array', () => {
        const playerCouponReward: IPlayerCouponReward = sampleWithRequiredData;
        expectedResult = service.addPlayerCouponRewardToCollectionIfMissing([], playerCouponReward);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(playerCouponReward);
      });

      it('should not add a PlayerCouponReward to an array that contains it', () => {
        const playerCouponReward: IPlayerCouponReward = sampleWithRequiredData;
        const playerCouponRewardCollection: IPlayerCouponReward[] = [
          {
            ...playerCouponReward,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPlayerCouponRewardToCollectionIfMissing(playerCouponRewardCollection, playerCouponReward);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PlayerCouponReward to an array that doesn't contain it", () => {
        const playerCouponReward: IPlayerCouponReward = sampleWithRequiredData;
        const playerCouponRewardCollection: IPlayerCouponReward[] = [sampleWithPartialData];
        expectedResult = service.addPlayerCouponRewardToCollectionIfMissing(playerCouponRewardCollection, playerCouponReward);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(playerCouponReward);
      });

      it('should add only unique PlayerCouponReward to an array', () => {
        const playerCouponRewardArray: IPlayerCouponReward[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const playerCouponRewardCollection: IPlayerCouponReward[] = [sampleWithRequiredData];
        expectedResult = service.addPlayerCouponRewardToCollectionIfMissing(playerCouponRewardCollection, ...playerCouponRewardArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const playerCouponReward: IPlayerCouponReward = sampleWithRequiredData;
        const playerCouponReward2: IPlayerCouponReward = sampleWithPartialData;
        expectedResult = service.addPlayerCouponRewardToCollectionIfMissing([], playerCouponReward, playerCouponReward2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(playerCouponReward);
        expect(expectedResult).toContain(playerCouponReward2);
      });

      it('should accept null and undefined values', () => {
        const playerCouponReward: IPlayerCouponReward = sampleWithRequiredData;
        expectedResult = service.addPlayerCouponRewardToCollectionIfMissing([], null, playerCouponReward, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(playerCouponReward);
      });

      it('should return initial array if no PlayerCouponReward is added', () => {
        const playerCouponRewardCollection: IPlayerCouponReward[] = [sampleWithRequiredData];
        expectedResult = service.addPlayerCouponRewardToCollectionIfMissing(playerCouponRewardCollection, undefined, null);
        expect(expectedResult).toEqual(playerCouponRewardCollection);
      });
    });

    describe('comparePlayerCouponReward', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePlayerCouponReward(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.comparePlayerCouponReward(entity1, entity2);
        const compareResult2 = service.comparePlayerCouponReward(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.comparePlayerCouponReward(entity1, entity2);
        const compareResult2 = service.comparePlayerCouponReward(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.comparePlayerCouponReward(entity1, entity2);
        const compareResult2 = service.comparePlayerCouponReward(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
