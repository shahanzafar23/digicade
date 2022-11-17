import { IPlayer, NewPlayer } from './player.model';

export const sampleWithRequiredData: IPlayer = {
  id: 24181,
};

export const sampleWithPartialData: IPlayer = {
  id: 14685,
  gamePlayCredits: 43339,
  tix: 90690,
  level: 22348,
  walletAddress: 'Cedi',
};

export const sampleWithFullData: IPlayer = {
  id: 7144,
  gamePlayCredits: 8595,
  tix: 97222,
  comp: 47305,
  level: 63886,
  walletAddress: 'synthesizing hard',
};

export const sampleWithNewData: NewPlayer = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
