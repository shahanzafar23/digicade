import dayjs from 'dayjs/esm';

import { IPlayerNftReward, NewPlayerNftReward } from './player-nft-reward.model';

export const sampleWithRequiredData: IPlayerNftReward = {
  id: 47120,
};

export const sampleWithPartialData: IPlayerNftReward = {
  id: 76673,
};

export const sampleWithFullData: IPlayerNftReward = {
  id: 16875,
  date: dayjs('2022-11-16'),
};

export const sampleWithNewData: NewPlayerNftReward = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
