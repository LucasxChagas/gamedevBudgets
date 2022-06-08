export interface IPayment {
  id?: number;
  paymentType?: string;
}

export class Payment implements IPayment {
  constructor(public id?: number, public paymentType?: string) {}
}

export function getPaymentIdentifier(payment: IPayment): number | undefined {
  return payment.id;
}
