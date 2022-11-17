import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IHighScore } from '../high-score.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../high-score.test-samples';

import { HighScoreService } from './high-score.service';

const requireRestSample: IHighScore = {
  ...sampleWithRequiredData,
};

describe('HighScore Service', () => {
  let service: HighScoreService;
  let httpMock: HttpTestingController;
  let expectedResult: IHighScore | IHighScore[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(HighScoreService);
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

    it('should create a HighScore', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const highScore = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(highScore).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a HighScore', () => {
      const highScore = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(highScore).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a HighScore', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of HighScore', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a HighScore', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addHighScoreToCollectionIfMissing', () => {
      it('should add a HighScore to an empty array', () => {
        const highScore: IHighScore = sampleWithRequiredData;
        expectedResult = service.addHighScoreToCollectionIfMissing([], highScore);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(highScore);
      });

      it('should not add a HighScore to an array that contains it', () => {
        const highScore: IHighScore = sampleWithRequiredData;
        const highScoreCollection: IHighScore[] = [
          {
            ...highScore,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addHighScoreToCollectionIfMissing(highScoreCollection, highScore);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a HighScore to an array that doesn't contain it", () => {
        const highScore: IHighScore = sampleWithRequiredData;
        const highScoreCollection: IHighScore[] = [sampleWithPartialData];
        expectedResult = service.addHighScoreToCollectionIfMissing(highScoreCollection, highScore);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(highScore);
      });

      it('should add only unique HighScore to an array', () => {
        const highScoreArray: IHighScore[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const highScoreCollection: IHighScore[] = [sampleWithRequiredData];
        expectedResult = service.addHighScoreToCollectionIfMissing(highScoreCollection, ...highScoreArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const highScore: IHighScore = sampleWithRequiredData;
        const highScore2: IHighScore = sampleWithPartialData;
        expectedResult = service.addHighScoreToCollectionIfMissing([], highScore, highScore2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(highScore);
        expect(expectedResult).toContain(highScore2);
      });

      it('should accept null and undefined values', () => {
        const highScore: IHighScore = sampleWithRequiredData;
        expectedResult = service.addHighScoreToCollectionIfMissing([], null, highScore, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(highScore);
      });

      it('should return initial array if no HighScore is added', () => {
        const highScoreCollection: IHighScore[] = [sampleWithRequiredData];
        expectedResult = service.addHighScoreToCollectionIfMissing(highScoreCollection, undefined, null);
        expect(expectedResult).toEqual(highScoreCollection);
      });
    });

    describe('compareHighScore', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareHighScore(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareHighScore(entity1, entity2);
        const compareResult2 = service.compareHighScore(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareHighScore(entity1, entity2);
        const compareResult2 = service.compareHighScore(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareHighScore(entity1, entity2);
        const compareResult2 = service.compareHighScore(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
