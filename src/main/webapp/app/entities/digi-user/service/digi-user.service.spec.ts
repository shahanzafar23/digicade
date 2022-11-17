import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IDigiUser } from '../digi-user.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../digi-user.test-samples';

import { DigiUserService, RestDigiUser } from './digi-user.service';

const requireRestSample: RestDigiUser = {
  ...sampleWithRequiredData,
  dob: sampleWithRequiredData.dob?.format(DATE_FORMAT),
};

describe('DigiUser Service', () => {
  let service: DigiUserService;
  let httpMock: HttpTestingController;
  let expectedResult: IDigiUser | IDigiUser[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DigiUserService);
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

    it('should create a DigiUser', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const digiUser = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(digiUser).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a DigiUser', () => {
      const digiUser = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(digiUser).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a DigiUser', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of DigiUser', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a DigiUser', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addDigiUserToCollectionIfMissing', () => {
      it('should add a DigiUser to an empty array', () => {
        const digiUser: IDigiUser = sampleWithRequiredData;
        expectedResult = service.addDigiUserToCollectionIfMissing([], digiUser);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(digiUser);
      });

      it('should not add a DigiUser to an array that contains it', () => {
        const digiUser: IDigiUser = sampleWithRequiredData;
        const digiUserCollection: IDigiUser[] = [
          {
            ...digiUser,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addDigiUserToCollectionIfMissing(digiUserCollection, digiUser);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a DigiUser to an array that doesn't contain it", () => {
        const digiUser: IDigiUser = sampleWithRequiredData;
        const digiUserCollection: IDigiUser[] = [sampleWithPartialData];
        expectedResult = service.addDigiUserToCollectionIfMissing(digiUserCollection, digiUser);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(digiUser);
      });

      it('should add only unique DigiUser to an array', () => {
        const digiUserArray: IDigiUser[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const digiUserCollection: IDigiUser[] = [sampleWithRequiredData];
        expectedResult = service.addDigiUserToCollectionIfMissing(digiUserCollection, ...digiUserArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const digiUser: IDigiUser = sampleWithRequiredData;
        const digiUser2: IDigiUser = sampleWithPartialData;
        expectedResult = service.addDigiUserToCollectionIfMissing([], digiUser, digiUser2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(digiUser);
        expect(expectedResult).toContain(digiUser2);
      });

      it('should accept null and undefined values', () => {
        const digiUser: IDigiUser = sampleWithRequiredData;
        expectedResult = service.addDigiUserToCollectionIfMissing([], null, digiUser, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(digiUser);
      });

      it('should return initial array if no DigiUser is added', () => {
        const digiUserCollection: IDigiUser[] = [sampleWithRequiredData];
        expectedResult = service.addDigiUserToCollectionIfMissing(digiUserCollection, undefined, null);
        expect(expectedResult).toEqual(digiUserCollection);
      });
    });

    describe('compareDigiUser', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareDigiUser(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareDigiUser(entity1, entity2);
        const compareResult2 = service.compareDigiUser(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareDigiUser(entity1, entity2);
        const compareResult2 = service.compareDigiUser(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareDigiUser(entity1, entity2);
        const compareResult2 = service.compareDigiUser(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
