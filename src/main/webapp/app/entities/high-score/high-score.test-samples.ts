import { IHighScore, NewHighScore } from './high-score.model';

export const sampleWithRequiredData: IHighScore = {
  id: 46185,
};

export const sampleWithPartialData: IHighScore = {
  id: 78161,
  highestScore: 15535,
};

export const sampleWithFullData: IHighScore = {
  id: 84299,
  highestScore: 90616,
};

export const sampleWithNewData: NewHighScore = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
