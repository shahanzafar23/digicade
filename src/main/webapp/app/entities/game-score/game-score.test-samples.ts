import dayjs from 'dayjs/esm';

import { IGameScore, NewGameScore } from './game-score.model';

export const sampleWithRequiredData: IGameScore = {
  id: 51936,
};

export const sampleWithPartialData: IGameScore = {
  id: 64299,
  score: 50038,
};

export const sampleWithFullData: IGameScore = {
  id: 74618,
  score: 77688,
  date: dayjs('2022-11-17'),
};

export const sampleWithNewData: NewGameScore = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
