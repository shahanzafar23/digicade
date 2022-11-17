import { IDigiUser } from 'app/entities/digi-user/digi-user.model';

export interface IPlayer {
  id: number;
  gamePlayCredits?: number | null;
  tix?: number | null;
  comp?: number | null;
  level?: number | null;
  walletAddress?: string | null;
  digiUser?: Pick<IDigiUser, 'id'> | null;
}

export type NewPlayer = Omit<IPlayer, 'id'> & { id: null };
