import { IGame, NewGame } from './game.model';

export const sampleWithRequiredData: IGame = {
  id: 79326,
};

export const sampleWithPartialData: IGame = {
  id: 64112,
  url: 'http://luther.biz',
};

export const sampleWithFullData: IGame = {
  id: 82003,
  url: 'http://thomas.name',
  logoUrl: 'online reintermediate',
};

export const sampleWithNewData: NewGame = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
