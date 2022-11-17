import { IGameLevel, NewGameLevel } from './game-level.model';

export const sampleWithRequiredData: IGameLevel = {
  id: 90140,
};

export const sampleWithPartialData: IGameLevel = {
  id: 8474,
  score: 86769,
};

export const sampleWithFullData: IGameLevel = {
  id: 29545,
  level: 73278,
  score: 42720,
};

export const sampleWithNewData: NewGameLevel = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
