import { ICouponReward } from 'app/entities/coupon-reward/coupon-reward.model';

export interface ICouponImage {
  id: number;
  imageUrl?: string | null;
  couponReward?: Pick<ICouponReward, 'id'> | null;
}

export type NewCouponImage = Omit<ICouponImage, 'id'> & { id: null };
