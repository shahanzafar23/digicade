import dayjs from 'dayjs/esm';

export interface ICouponReward {
  id: number;
  description?: string | null;
  location?: string | null;
  tix?: number | null;
  comp?: number | null;
  expiry?: dayjs.Dayjs | null;
}

export type NewCouponReward = Omit<ICouponReward, 'id'> & { id: null };
