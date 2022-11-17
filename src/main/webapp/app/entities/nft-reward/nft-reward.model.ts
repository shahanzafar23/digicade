export interface INftReward {
  id: number;
  tix?: number | null;
  comp?: number | null;
  imageUrl?: string | null;
}

export type NewNftReward = Omit<INftReward, 'id'> & { id: null };
