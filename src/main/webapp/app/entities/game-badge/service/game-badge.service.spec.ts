import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IGameBadge } from '../game-badge.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../game-badge.test-samples';

import { GameBadgeService } from './game-badge.service';

const requireRestSample: IGameBadge = {
  ...sampleWithRequiredData,
};

describe('GameBadge Service', () => {
  let service: GameBadgeService;
  let httpMock: HttpTestingController;
  let expectedResult: IGameBadge | IGameBadge[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(GameBadgeService);
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

    it('should create a GameBadge', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const gameBadge = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(gameBadge).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a GameBadge', () => {
      const gameBadge = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(gameBadge).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a GameBadge', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of GameBadge', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a GameBadge', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addGameBadgeToCollectionIfMissing', () => {
      it('should add a GameBadge to an empty array', () => {
        const gameBadge: IGameBadge = sampleWithRequiredData;
        expectedResult = service.addGameBadgeToCollectionIfMissing([], gameBadge);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(gameBadge);
      });

      it('should not add a GameBadge to an array that contains it', () => {
        const gameBadge: IGameBadge = sampleWithRequiredData;
        const gameBadgeCollection: IGameBadge[] = [
          {
            ...gameBadge,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addGameBadgeToCollectionIfMissing(gameBadgeCollection, gameBadge);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a GameBadge to an array that doesn't contain it", () => {
        const gameBadge: IGameBadge = sampleWithRequiredData;
        const gameBadgeCollection: IGameBadge[] = [sampleWithPartialData];
        expectedResult = service.addGameBadgeToCollectionIfMissing(gameBadgeCollection, gameBadge);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(gameBadge);
      });

      it('should add only unique GameBadge to an array', () => {
        const gameBadgeArray: IGameBadge[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const gameBadgeCollection: IGameBadge[] = [sampleWithRequiredData];
        expectedResult = service.addGameBadgeToCollectionIfMissing(gameBadgeCollection, ...gameBadgeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const gameBadge: IGameBadge = sampleWithRequiredData;
        const gameBadge2: IGameBadge = sampleWithPartialData;
        expectedResult = service.addGameBadgeToCollectionIfMissing([], gameBadge, gameBadge2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(gameBadge);
        expect(expectedResult).toContain(gameBadge2);
      });

      it('should accept null and undefined values', () => {
        const gameBadge: IGameBadge = sampleWithRequiredData;
        expectedResult = service.addGameBadgeToCollectionIfMissing([], null, gameBadge, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(gameBadge);
      });

      it('should return initial array if no GameBadge is added', () => {
        const gameBadgeCollection: IGameBadge[] = [sampleWithRequiredData];
        expectedResult = service.addGameBadgeToCollectionIfMissing(gameBadgeCollection, undefined, null);
        expect(expectedResult).toEqual(gameBadgeCollection);
      });
    });

    describe('compareGameBadge', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareGameBadge(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareGameBadge(entity1, entity2);
        const compareResult2 = service.compareGameBadge(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareGameBadge(entity1, entity2);
        const compareResult2 = service.compareGameBadge(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareGameBadge(entity1, entity2);
        const compareResult2 = service.compareGameBadge(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
