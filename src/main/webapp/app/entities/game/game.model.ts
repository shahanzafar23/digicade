export interface IGame {
  id: number;
  url?: string | null;
  logoUrl?: string | null;
}

export type NewGame = Omit<IGame, 'id'> & { id: null };
