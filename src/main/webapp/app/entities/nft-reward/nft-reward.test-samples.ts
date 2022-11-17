import { INftReward, NewNftReward } from './nft-reward.model';

export const sampleWithRequiredData: INftReward = {
  id: 22346,
};

export const sampleWithPartialData: INftReward = {
  id: 207,
  imageUrl: 'Manager',
};

export const sampleWithFullData: INftReward = {
  id: 91159,
  tix: 93246,
  comp: 68393,
  imageUrl: 'transmitter Kids Licensed',
};

export const sampleWithNewData: NewNftReward = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
