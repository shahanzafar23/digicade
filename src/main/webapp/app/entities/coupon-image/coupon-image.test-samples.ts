import { ICouponImage, NewCouponImage } from './coupon-image.model';

export const sampleWithRequiredData: ICouponImage = {
  id: 11329,
};

export const sampleWithPartialData: ICouponImage = {
  id: 62146,
};

export const sampleWithFullData: ICouponImage = {
  id: 36656,
  imageUrl: 'Account bandwidth Avon',
};

export const sampleWithNewData: NewCouponImage = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
