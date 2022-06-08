import { IBudget } from 'app/entities/budget/budget.model';

export interface IPayment {
  id?: number;
  paymentType?: string;
  payment?: IBudget | null;
}

export class Payment implements IPayment {
  constructor(public id?: number, public paymentType?: string, public payment?: IBudget | null) {}
}

export function getPaymentIdentifier(payment: IPayment): number | undefined {
  return payment.id;
}
