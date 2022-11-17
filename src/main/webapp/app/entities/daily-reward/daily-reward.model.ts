import { ICouponReward } from 'app/entities/coupon-reward/coupon-reward.model';
import { INftReward } from 'app/entities/nft-reward/nft-reward.model';
import { RewardType } from 'app/entities/enumerations/reward-type.model';

export interface IDailyReward {
  id: number;
  time?: string | null;
  rewardType?: RewardType | null;
  amount?: number | null;
  couponReward?: Pick<ICouponReward, 'id'> | null;
  nftReward?: Pick<INftReward, 'id'> | null;
}

export type NewDailyReward = Omit<IDailyReward, 'id'> & { id: null };
