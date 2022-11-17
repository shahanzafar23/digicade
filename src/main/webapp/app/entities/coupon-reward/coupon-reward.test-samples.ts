import dayjs from 'dayjs/esm';

import { ICouponReward, NewCouponReward } from './coupon-reward.model';

export const sampleWithRequiredData: ICouponReward = {
  id: 49465,
};

export const sampleWithPartialData: ICouponReward = {
  id: 46604,
  description: 'orchid Bedfordshire',
  tix: 33504,
  comp: 90794,
  expiry: dayjs('2022-11-17'),
};

export const sampleWithFullData: ICouponReward = {
  id: 63973,
  description: 'HTTP digital',
  location: 'Island',
  tix: 84196,
  comp: 17031,
  expiry: dayjs('2022-11-16'),
};

export const sampleWithNewData: NewCouponReward = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
