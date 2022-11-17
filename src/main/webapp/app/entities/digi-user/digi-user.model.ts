import dayjs from 'dayjs/esm';

export interface IDigiUser {
  id: number;
  firstName?: string | null;
  lastName?: string | null;
  userName?: string | null;
  email?: string | null;
  dob?: dayjs.Dayjs | null;
  age?: number | null;
  promoCode?: string | null;
}

export type NewDigiUser = Omit<IDigiUser, 'id'> & { id: null };
