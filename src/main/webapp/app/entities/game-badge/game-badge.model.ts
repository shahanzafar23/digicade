import { IGame } from 'app/entities/game/game.model';
import { IPlayer } from 'app/entities/player/player.model';

export interface IGameBadge {
  id: number;
  logoUrl?: string | null;
  game?: Pick<IGame, 'id'> | null;
  player?: Pick<IPlayer, 'id'> | null;
}

export type NewGameBadge = Omit<IGameBadge, 'id'> & { id: null };
