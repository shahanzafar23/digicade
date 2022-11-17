import dayjs from 'dayjs/esm';
import { IPlayer } from 'app/entities/player/player.model';
import { ICoinPackage } from 'app/entities/coin-package/coin-package.model';

export interface ITransaction {
  id: number;
  date?: dayjs.Dayjs | null;
  player?: Pick<IPlayer, 'id'> | null;
  coinPackage?: Pick<ICoinPackage, 'id'> | null;
}

export type NewTransaction = Omit<ITransaction, 'id'> & { id: null };
