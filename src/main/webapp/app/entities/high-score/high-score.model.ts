import { IGame } from 'app/entities/game/game.model';
import { IPlayer } from 'app/entities/player/player.model';

export interface IHighScore {
  id: number;
  highestScore?: number | null;
  game?: Pick<IGame, 'id'> | null;
  player?: Pick<IPlayer, 'id'> | null;
}

export type NewHighScore = Omit<IHighScore, 'id'> & { id: null };
