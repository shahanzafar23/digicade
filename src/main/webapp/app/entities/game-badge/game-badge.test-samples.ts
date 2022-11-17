import { IGameBadge, NewGameBadge } from './game-badge.model';

export const sampleWithRequiredData: IGameBadge = {
  id: 58687,
};

export const sampleWithPartialData: IGameBadge = {
  id: 50530,
  logoUrl: 'superstructure Movies',
};

export const sampleWithFullData: IGameBadge = {
  id: 55733,
  logoUrl: 'Singapore',
};

export const sampleWithNewData: NewGameBadge = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
