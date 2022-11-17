import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ICouponReward } from '../coupon-reward.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../coupon-reward.test-samples';

import { CouponRewardService, RestCouponReward } from './coupon-reward.service';

const requireRestSample: RestCouponReward = {
  ...sampleWithRequiredData,
  expiry: sampleWithRequiredData.expiry?.format(DATE_FORMAT),
};

describe('CouponReward Service', () => {
  let service: CouponRewardService;
  let httpMock: HttpTestingController;
  let expectedResult: ICouponReward | ICouponReward[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CouponRewardService);
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

    it('should create a CouponReward', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const couponReward = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(couponReward).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CouponReward', () => {
      const couponReward = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(couponReward).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CouponReward', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CouponReward', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a CouponReward', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addCouponRewardToCollectionIfMissing', () => {
      it('should add a CouponReward to an empty array', () => {
        const couponReward: ICouponReward = sampleWithRequiredData;
        expectedResult = service.addCouponRewardToCollectionIfMissing([], couponReward);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(couponReward);
      });

      it('should not add a CouponReward to an array that contains it', () => {
        const couponReward: ICouponReward = sampleWithRequiredData;
        const couponRewardCollection: ICouponReward[] = [
          {
            ...couponReward,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCouponRewardToCollectionIfMissing(couponRewardCollection, couponReward);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CouponReward to an array that doesn't contain it", () => {
        const couponReward: ICouponReward = sampleWithRequiredData;
        const couponRewardCollection: ICouponReward[] = [sampleWithPartialData];
        expectedResult = service.addCouponRewardToCollectionIfMissing(couponRewardCollection, couponReward);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(couponReward);
      });

      it('should add only unique CouponReward to an array', () => {
        const couponRewardArray: ICouponReward[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const couponRewardCollection: ICouponReward[] = [sampleWithRequiredData];
        expectedResult = service.addCouponRewardToCollectionIfMissing(couponRewardCollection, ...couponRewardArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const couponReward: ICouponReward = sampleWithRequiredData;
        const couponReward2: ICouponReward = sampleWithPartialData;
        expectedResult = service.addCouponRewardToCollectionIfMissing([], couponReward, couponReward2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(couponReward);
        expect(expectedResult).toContain(couponReward2);
      });

      it('should accept null and undefined values', () => {
        const couponReward: ICouponReward = sampleWithRequiredData;
        expectedResult = service.addCouponRewardToCollectionIfMissing([], null, couponReward, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(couponReward);
      });

      it('should return initial array if no CouponReward is added', () => {
        const couponRewardCollection: ICouponReward[] = [sampleWithRequiredData];
        expectedResult = service.addCouponRewardToCollectionIfMissing(couponRewardCollection, undefined, null);
        expect(expectedResult).toEqual(couponRewardCollection);
      });
    });

    describe('compareCouponReward', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCouponReward(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareCouponReward(entity1, entity2);
        const compareResult2 = service.compareCouponReward(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareCouponReward(entity1, entity2);
        const compareResult2 = service.compareCouponReward(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareCouponReward(entity1, entity2);
        const compareResult2 = service.compareCouponReward(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
