import dayjs from 'dayjs/esm';
import { IGame } from 'app/entities/game/game.model';
import { IPlayer } from 'app/entities/player/player.model';

export interface IGameScore {
  id: number;
  score?: number | null;
  date?: dayjs.Dayjs | null;
  game?: Pick<IGame, 'id'> | null;
  player?: Pick<IPlayer, 'id'> | null;
}

export type NewGameScore = Omit<IGameScore, 'id'> & { id: null };
