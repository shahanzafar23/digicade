import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICoinPackage } from '../coin-package.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../coin-package.test-samples';

import { CoinPackageService } from './coin-package.service';

const requireRestSample: ICoinPackage = {
  ...sampleWithRequiredData,
};

describe('CoinPackage Service', () => {
  let service: CoinPackageService;
  let httpMock: HttpTestingController;
  let expectedResult: ICoinPackage | ICoinPackage[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CoinPackageService);
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

    it('should create a CoinPackage', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const coinPackage = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(coinPackage).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CoinPackage', () => {
      const coinPackage = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(coinPackage).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CoinPackage', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CoinPackage', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a CoinPackage', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addCoinPackageToCollectionIfMissing', () => {
      it('should add a CoinPackage to an empty array', () => {
        const coinPackage: ICoinPackage = sampleWithRequiredData;
        expectedResult = service.addCoinPackageToCollectionIfMissing([], coinPackage);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(coinPackage);
      });

      it('should not add a CoinPackage to an array that contains it', () => {
        const coinPackage: ICoinPackage = sampleWithRequiredData;
        const coinPackageCollection: ICoinPackage[] = [
          {
            ...coinPackage,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCoinPackageToCollectionIfMissing(coinPackageCollection, coinPackage);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CoinPackage to an array that doesn't contain it", () => {
        const coinPackage: ICoinPackage = sampleWithRequiredData;
        const coinPackageCollection: ICoinPackage[] = [sampleWithPartialData];
        expectedResult = service.addCoinPackageToCollectionIfMissing(coinPackageCollection, coinPackage);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(coinPackage);
      });

      it('should add only unique CoinPackage to an array', () => {
        const coinPackageArray: ICoinPackage[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const coinPackageCollection: ICoinPackage[] = [sampleWithRequiredData];
        expectedResult = service.addCoinPackageToCollectionIfMissing(coinPackageCollection, ...coinPackageArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const coinPackage: ICoinPackage = sampleWithRequiredData;
        const coinPackage2: ICoinPackage = sampleWithPartialData;
        expectedResult = service.addCoinPackageToCollectionIfMissing([], coinPackage, coinPackage2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(coinPackage);
        expect(expectedResult).toContain(coinPackage2);
      });

      it('should accept null and undefined values', () => {
        const coinPackage: ICoinPackage = sampleWithRequiredData;
        expectedResult = service.addCoinPackageToCollectionIfMissing([], null, coinPackage, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(coinPackage);
      });

      it('should return initial array if no CoinPackage is added', () => {
        const coinPackageCollection: ICoinPackage[] = [sampleWithRequiredData];
        expectedResult = service.addCoinPackageToCollectionIfMissing(coinPackageCollection, undefined, null);
        expect(expectedResult).toEqual(coinPackageCollection);
      });
    });

    describe('compareCoinPackage', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCoinPackage(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareCoinPackage(entity1, entity2);
        const compareResult2 = service.compareCoinPackage(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareCoinPackage(entity1, entity2);
        const compareResult2 = service.compareCoinPackage(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareCoinPackage(entity1, entity2);
        const compareResult2 = service.compareCoinPackage(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
