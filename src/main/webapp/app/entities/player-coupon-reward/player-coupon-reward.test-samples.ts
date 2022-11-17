import dayjs from 'dayjs/esm';

import { CouponStatus } from 'app/entities/enumerations/coupon-status.model';

import { IPlayerCouponReward, NewPlayerCouponReward } from './player-coupon-reward.model';

export const sampleWithRequiredData: IPlayerCouponReward = {
  id: 16162,
};

export const sampleWithPartialData: IPlayerCouponReward = {
  id: 53972,
  date: dayjs('2022-11-16'),
  status: CouponStatus['REDEEMED'],
};

export const sampleWithFullData: IPlayerCouponReward = {
  id: 79731,
  date: dayjs('2022-11-17'),
  status: CouponStatus['REFUNDED'],
};

export const sampleWithNewData: NewPlayerCouponReward = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
