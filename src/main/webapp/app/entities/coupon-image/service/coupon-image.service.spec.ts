import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICouponImage } from '../coupon-image.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../coupon-image.test-samples';

import { CouponImageService } from './coupon-image.service';

const requireRestSample: ICouponImage = {
  ...sampleWithRequiredData,
};

describe('CouponImage Service', () => {
  let service: CouponImageService;
  let httpMock: HttpTestingController;
  let expectedResult: ICouponImage | ICouponImage[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CouponImageService);
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

    it('should create a CouponImage', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const couponImage = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(couponImage).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CouponImage', () => {
      const couponImage = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(couponImage).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CouponImage', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CouponImage', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a CouponImage', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addCouponImageToCollectionIfMissing', () => {
      it('should add a CouponImage to an empty array', () => {
        const couponImage: ICouponImage = sampleWithRequiredData;
        expectedResult = service.addCouponImageToCollectionIfMissing([], couponImage);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(couponImage);
      });

      it('should not add a CouponImage to an array that contains it', () => {
        const couponImage: ICouponImage = sampleWithRequiredData;
        const couponImageCollection: ICouponImage[] = [
          {
            ...couponImage,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCouponImageToCollectionIfMissing(couponImageCollection, couponImage);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CouponImage to an array that doesn't contain it", () => {
        const couponImage: ICouponImage = sampleWithRequiredData;
        const couponImageCollection: ICouponImage[] = [sampleWithPartialData];
        expectedResult = service.addCouponImageToCollectionIfMissing(couponImageCollection, couponImage);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(couponImage);
      });

      it('should add only unique CouponImage to an array', () => {
        const couponImageArray: ICouponImage[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const couponImageCollection: ICouponImage[] = [sampleWithRequiredData];
        expectedResult = service.addCouponImageToCollectionIfMissing(couponImageCollection, ...couponImageArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const couponImage: ICouponImage = sampleWithRequiredData;
        const couponImage2: ICouponImage = sampleWithPartialData;
        expectedResult = service.addCouponImageToCollectionIfMissing([], couponImage, couponImage2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(couponImage);
        expect(expectedResult).toContain(couponImage2);
      });

      it('should accept null and undefined values', () => {
        const couponImage: ICouponImage = sampleWithRequiredData;
        expectedResult = service.addCouponImageToCollectionIfMissing([], null, couponImage, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(couponImage);
      });

      it('should return initial array if no CouponImage is added', () => {
        const couponImageCollection: ICouponImage[] = [sampleWithRequiredData];
        expectedResult = service.addCouponImageToCollectionIfMissing(couponImageCollection, undefined, null);
        expect(expectedResult).toEqual(couponImageCollection);
      });
    });

    describe('compareCouponImage', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCouponImage(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareCouponImage(entity1, entity2);
        const compareResult2 = service.compareCouponImage(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareCouponImage(entity1, entity2);
        const compareResult2 = service.compareCouponImage(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareCouponImage(entity1, entity2);
        const compareResult2 = service.compareCouponImage(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
