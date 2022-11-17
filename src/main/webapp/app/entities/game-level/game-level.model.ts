import { IGame } from 'app/entities/game/game.model';

export interface IGameLevel {
  id: number;
  level?: number | null;
  score?: number | null;
  game?: Pick<IGame, 'id'> | null;
}

export type NewGameLevel = Omit<IGameLevel, 'id'> & { id: null };
