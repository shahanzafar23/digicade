import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { INftReward } from '../nft-reward.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../nft-reward.test-samples';

import { NftRewardService } from './nft-reward.service';

const requireRestSample: INftReward = {
  ...sampleWithRequiredData,
};

describe('NftReward Service', () => {
  let service: NftRewardService;
  let httpMock: HttpTestingController;
  let expectedResult: INftReward | INftReward[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(NftRewardService);
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

    it('should create a NftReward', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const nftReward = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(nftReward).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a NftReward', () => {
      const nftReward = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(nftReward).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a NftReward', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of NftReward', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a NftReward', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addNftRewardToCollectionIfMissing', () => {
      it('should add a NftReward to an empty array', () => {
        const nftReward: INftReward = sampleWithRequiredData;
        expectedResult = service.addNftRewardToCollectionIfMissing([], nftReward);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(nftReward);
      });

      it('should not add a NftReward to an array that contains it', () => {
        const nftReward: INftReward = sampleWithRequiredData;
        const nftRewardCollection: INftReward[] = [
          {
            ...nftReward,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addNftRewardToCollectionIfMissing(nftRewardCollection, nftReward);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a NftReward to an array that doesn't contain it", () => {
        const nftReward: INftReward = sampleWithRequiredData;
        const nftRewardCollection: INftReward[] = [sampleWithPartialData];
        expectedResult = service.addNftRewardToCollectionIfMissing(nftRewardCollection, nftReward);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(nftReward);
      });

      it('should add only unique NftReward to an array', () => {
        const nftRewardArray: INftReward[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const nftRewardCollection: INftReward[] = [sampleWithRequiredData];
        expectedResult = service.addNftRewardToCollectionIfMissing(nftRewardCollection, ...nftRewardArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const nftReward: INftReward = sampleWithRequiredData;
        const nftReward2: INftReward = sampleWithPartialData;
        expectedResult = service.addNftRewardToCollectionIfMissing([], nftReward, nftReward2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(nftReward);
        expect(expectedResult).toContain(nftReward2);
      });

      it('should accept null and undefined values', () => {
        const nftReward: INftReward = sampleWithRequiredData;
        expectedResult = service.addNftRewardToCollectionIfMissing([], null, nftReward, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(nftReward);
      });

      it('should return initial array if no NftReward is added', () => {
        const nftRewardCollection: INftReward[] = [sampleWithRequiredData];
        expectedResult = service.addNftRewardToCollectionIfMissing(nftRewardCollection, undefined, null);
        expect(expectedResult).toEqual(nftRewardCollection);
      });
    });

    describe('compareNftReward', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareNftReward(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareNftReward(entity1, entity2);
        const compareResult2 = service.compareNftReward(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareNftReward(entity1, entity2);
        const compareResult2 = service.compareNftReward(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareNftReward(entity1, entity2);
        const compareResult2 = service.compareNftReward(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
