import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IGameLevel } from '../game-level.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../game-level.test-samples';

import { GameLevelService } from './game-level.service';

const requireRestSample: IGameLevel = {
  ...sampleWithRequiredData,
};

describe('GameLevel Service', () => {
  let service: GameLevelService;
  let httpMock: HttpTestingController;
  let expectedResult: IGameLevel | IGameLevel[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(GameLevelService);
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

    it('should create a GameLevel', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const gameLevel = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(gameLevel).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a GameLevel', () => {
      const gameLevel = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(gameLevel).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a GameLevel', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of GameLevel', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a GameLevel', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addGameLevelToCollectionIfMissing', () => {
      it('should add a GameLevel to an empty array', () => {
        const gameLevel: IGameLevel = sampleWithRequiredData;
        expectedResult = service.addGameLevelToCollectionIfMissing([], gameLevel);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(gameLevel);
      });

      it('should not add a GameLevel to an array that contains it', () => {
        const gameLevel: IGameLevel = sampleWithRequiredData;
        const gameLevelCollection: IGameLevel[] = [
          {
            ...gameLevel,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addGameLevelToCollectionIfMissing(gameLevelCollection, gameLevel);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a GameLevel to an array that doesn't contain it", () => {
        const gameLevel: IGameLevel = sampleWithRequiredData;
        const gameLevelCollection: IGameLevel[] = [sampleWithPartialData];
        expectedResult = service.addGameLevelToCollectionIfMissing(gameLevelCollection, gameLevel);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(gameLevel);
      });

      it('should add only unique GameLevel to an array', () => {
        const gameLevelArray: IGameLevel[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const gameLevelCollection: IGameLevel[] = [sampleWithRequiredData];
        expectedResult = service.addGameLevelToCollectionIfMissing(gameLevelCollection, ...gameLevelArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const gameLevel: IGameLevel = sampleWithRequiredData;
        const gameLevel2: IGameLevel = sampleWithPartialData;
        expectedResult = service.addGameLevelToCollectionIfMissing([], gameLevel, gameLevel2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(gameLevel);
        expect(expectedResult).toContain(gameLevel2);
      });

      it('should accept null and undefined values', () => {
        const gameLevel: IGameLevel = sampleWithRequiredData;
        expectedResult = service.addGameLevelToCollectionIfMissing([], null, gameLevel, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(gameLevel);
      });

      it('should return initial array if no GameLevel is added', () => {
        const gameLevelCollection: IGameLevel[] = [sampleWithRequiredData];
        expectedResult = service.addGameLevelToCollectionIfMissing(gameLevelCollection, undefined, null);
        expect(expectedResult).toEqual(gameLevelCollection);
      });
    });

    describe('compareGameLevel', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareGameLevel(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareGameLevel(entity1, entity2);
        const compareResult2 = service.compareGameLevel(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareGameLevel(entity1, entity2);
        const compareResult2 = service.compareGameLevel(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareGameLevel(entity1, entity2);
        const compareResult2 = service.compareGameLevel(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
