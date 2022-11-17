import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IDailyReward } from '../daily-reward.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../daily-reward.test-samples';

import { DailyRewardService } from './daily-reward.service';

const requireRestSample: IDailyReward = {
  ...sampleWithRequiredData,
};

describe('DailyReward Service', () => {
  let service: DailyRewardService;
  let httpMock: HttpTestingController;
  let expectedResult: IDailyReward | IDailyReward[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DailyRewardService);
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

    it('should create a DailyReward', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const dailyReward = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(dailyReward).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a DailyReward', () => {
      const dailyReward = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(dailyReward).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a DailyReward', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of DailyReward', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a DailyReward', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addDailyRewardToCollectionIfMissing', () => {
      it('should add a DailyReward to an empty array', () => {
        const dailyReward: IDailyReward = sampleWithRequiredData;
        expectedResult = service.addDailyRewardToCollectionIfMissing([], dailyReward);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(dailyReward);
      });

      it('should not add a DailyReward to an array that contains it', () => {
        const dailyReward: IDailyReward = sampleWithRequiredData;
        const dailyRewardCollection: IDailyReward[] = [
          {
            ...dailyReward,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addDailyRewardToCollectionIfMissing(dailyRewardCollection, dailyReward);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a DailyReward to an array that doesn't contain it", () => {
        const dailyReward: IDailyReward = sampleWithRequiredData;
        const dailyRewardCollection: IDailyReward[] = [sampleWithPartialData];
        expectedResult = service.addDailyRewardToCollectionIfMissing(dailyRewardCollection, dailyReward);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(dailyReward);
      });

      it('should add only unique DailyReward to an array', () => {
        const dailyRewardArray: IDailyReward[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const dailyRewardCollection: IDailyReward[] = [sampleWithRequiredData];
        expectedResult = service.addDailyRewardToCollectionIfMissing(dailyRewardCollection, ...dailyRewardArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const dailyReward: IDailyReward = sampleWithRequiredData;
        const dailyReward2: IDailyReward = sampleWithPartialData;
        expectedResult = service.addDailyRewardToCollectionIfMissing([], dailyReward, dailyReward2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(dailyReward);
        expect(expectedResult).toContain(dailyReward2);
      });

      it('should accept null and undefined values', () => {
        const dailyReward: IDailyReward = sampleWithRequiredData;
        expectedResult = service.addDailyRewardToCollectionIfMissing([], null, dailyReward, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(dailyReward);
      });

      it('should return initial array if no DailyReward is added', () => {
        const dailyRewardCollection: IDailyReward[] = [sampleWithRequiredData];
        expectedResult = service.addDailyRewardToCollectionIfMissing(dailyRewardCollection, undefined, null);
        expect(expectedResult).toEqual(dailyRewardCollection);
      });
    });

    describe('compareDailyReward', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareDailyReward(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareDailyReward(entity1, entity2);
        const compareResult2 = service.compareDailyReward(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareDailyReward(entity1, entity2);
        const compareResult2 = service.compareDailyReward(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareDailyReward(entity1, entity2);
        const compareResult2 = service.compareDailyReward(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
