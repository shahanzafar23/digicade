import dayjs from 'dayjs/esm';
import { IPlayer } from 'app/entities/player/player.model';
import { INftReward } from 'app/entities/nft-reward/nft-reward.model';

export interface IPlayerNftReward {
  id: number;
  date?: dayjs.Dayjs | null;
  player?: Pick<IPlayer, 'id'> | null;
  nftReward?: Pick<INftReward, 'id'> | null;
}

export type NewPlayerNftReward = Omit<IPlayerNftReward, 'id'> & { id: null };
