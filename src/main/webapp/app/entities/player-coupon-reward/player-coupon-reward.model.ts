import dayjs from 'dayjs/esm';
import { IPlayer } from 'app/entities/player/player.model';
import { ICouponReward } from 'app/entities/coupon-reward/coupon-reward.model';
import { CouponStatus } from 'app/entities/enumerations/coupon-status.model';

export interface IPlayerCouponReward {
  id: number;
  date?: dayjs.Dayjs | null;
  status?: CouponStatus | null;
  player?: Pick<IPlayer, 'id'> | null;
  couponReward?: Pick<ICouponReward, 'id'> | null;
}

export type NewPlayerCouponReward = Omit<IPlayerCouponReward, 'id'> & { id: null };
