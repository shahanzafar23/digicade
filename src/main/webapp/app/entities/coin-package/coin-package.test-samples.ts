import { ICoinPackage, NewCoinPackage } from './coin-package.model';

export const sampleWithRequiredData: ICoinPackage = {
  id: 43077,
};

export const sampleWithPartialData: ICoinPackage = {
  id: 3101,
};

export const sampleWithFullData: ICoinPackage = {
  id: 11289,
  coins: 30612,
  cost: 70076,
};

export const sampleWithNewData: NewCoinPackage = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
