import dayjs from 'dayjs/esm';

import { ITransaction, NewTransaction } from './transaction.model';

export const sampleWithRequiredData: ITransaction = {
  id: 73739,
};

export const sampleWithPartialData: ITransaction = {
  id: 80438,
};

export const sampleWithFullData: ITransaction = {
  id: 71862,
  date: dayjs('2022-11-16'),
};

export const sampleWithNewData: NewTransaction = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
