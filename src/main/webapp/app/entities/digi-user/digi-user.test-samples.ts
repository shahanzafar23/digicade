import dayjs from 'dayjs/esm';

import { IDigiUser, NewDigiUser } from './digi-user.model';

export const sampleWithRequiredData: IDigiUser = {
  id: 44513,
};

export const sampleWithPartialData: IDigiUser = {
  id: 568,
  lastName: 'Kohler',
  userName: 'transmit group',
  dob: dayjs('2022-11-16'),
  age: 40273,
  promoCode: 'Officer eyeballs Account',
};

export const sampleWithFullData: IDigiUser = {
  id: 4207,
  firstName: 'Carlie',
  lastName: 'Mills',
  userName: 'Functionality',
  email: 'Lilyan_Glover4@hotmail.com',
  dob: dayjs('2022-11-16'),
  age: 14011,
  promoCode: '24/7',
};

export const sampleWithNewData: NewDigiUser = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
