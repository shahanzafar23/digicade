import { RewardType } from 'app/entities/enumerations/reward-type.model';

import { IDailyReward, NewDailyReward } from './daily-reward.model';

export const sampleWithRequiredData: IDailyReward = {
  id: 62525,
};

export const sampleWithPartialData: IDailyReward = {
  id: 81870,
  time: 'Cliff withdrawal next-generation',
  rewardType: RewardType['TIX'],
  amount: 99547,
};

export const sampleWithFullData: IDailyReward = {
  id: 49272,
  time: 'Ball red',
  rewardType: RewardType['COMP'],
  amount: 62163,
};

export const sampleWithNewData: NewDailyReward = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
