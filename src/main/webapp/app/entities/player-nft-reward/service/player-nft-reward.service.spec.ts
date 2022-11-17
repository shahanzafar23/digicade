import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IPlayerNftReward } from '../player-nft-reward.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../player-nft-reward.test-samples';

import { PlayerNftRewardService, RestPlayerNftReward } from './player-nft-reward.service';

const requireRestSample: RestPlayerNftReward = {
  ...sampleWithRequiredData,
  date: sampleWithRequiredData.date?.format(DATE_FORMAT),
};

describe('PlayerNftReward Service', () => {
  let service: PlayerNftRewardService;
  let httpMock: HttpTestingController;
  let expectedResult: IPlayerNftReward | IPlayerNftReward[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PlayerNftRewardService);
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

    it('should create a PlayerNftReward', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const playerNftReward = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(playerNftReward).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PlayerNftReward', () => {
      const playerNftReward = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(playerNftReward).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PlayerNftReward', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PlayerNftReward', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a PlayerNftReward', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addPlayerNftRewardToCollectionIfMissing', () => {
      it('should add a PlayerNftReward to an empty array', () => {
        const playerNftReward: IPlayerNftReward = sampleWithRequiredData;
        expectedResult = service.addPlayerNftRewardToCollectionIfMissing([], playerNftReward);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(playerNftReward);
      });

      it('should not add a PlayerNftReward to an array that contains it', () => {
        const playerNftReward: IPlayerNftReward = sampleWithRequiredData;
        const playerNftRewardCollection: IPlayerNftReward[] = [
          {
            ...playerNftReward,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPlayerNftRewardToCollectionIfMissing(playerNftRewardCollection, playerNftReward);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PlayerNftReward to an array that doesn't contain it", () => {
        const playerNftReward: IPlayerNftReward = sampleWithRequiredData;
        const playerNftRewardCollection: IPlayerNftReward[] = [sampleWithPartialData];
        expectedResult = service.addPlayerNftRewardToCollectionIfMissing(playerNftRewardCollection, playerNftReward);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(playerNftReward);
      });

      it('should add only unique PlayerNftReward to an array', () => {
        const playerNftRewardArray: IPlayerNftReward[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const playerNftRewardCollection: IPlayerNftReward[] = [sampleWithRequiredData];
        expectedResult = service.addPlayerNftRewardToCollectionIfMissing(playerNftRewardCollection, ...playerNftRewardArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const playerNftReward: IPlayerNftReward = sampleWithRequiredData;
        const playerNftReward2: IPlayerNftReward = sampleWithPartialData;
        expectedResult = service.addPlayerNftRewardToCollectionIfMissing([], playerNftReward, playerNftReward2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(playerNftReward);
        expect(expectedResult).toContain(playerNftReward2);
      });

      it('should accept null and undefined values', () => {
        const playerNftReward: IPlayerNftReward = sampleWithRequiredData;
        expectedResult = service.addPlayerNftRewardToCollectionIfMissing([], null, playerNftReward, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(playerNftReward);
      });

      it('should return initial array if no PlayerNftReward is added', () => {
        const playerNftRewardCollection: IPlayerNftReward[] = [sampleWithRequiredData];
        expectedResult = service.addPlayerNftRewardToCollectionIfMissing(playerNftRewardCollection, undefined, null);
        expect(expectedResult).toEqual(playerNftRewardCollection);
      });
    });

    describe('comparePlayerNftReward', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePlayerNftReward(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.comparePlayerNftReward(entity1, entity2);
        const compareResult2 = service.comparePlayerNftReward(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.comparePlayerNftReward(entity1, entity2);
        const compareResult2 = service.comparePlayerNftReward(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.comparePlayerNftReward(entity1, entity2);
        const compareResult2 = service.comparePlayerNftReward(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
