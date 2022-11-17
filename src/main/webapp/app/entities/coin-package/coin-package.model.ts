export interface ICoinPackage {
  id: number;
  coins?: number | null;
  cost?: number | null;
}

export type NewCoinPackage = Omit<ICoinPackage, 'id'> & { id: null };
