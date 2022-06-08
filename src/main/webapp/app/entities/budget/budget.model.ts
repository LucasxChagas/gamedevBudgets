import dayjs from 'dayjs/esm';
import { ISounds } from 'app/entities/sounds/sounds.model';
import { IGame } from 'app/entities/game/game.model';
import { IPayment } from 'app/entities/payment/payment.model';

export interface IBudget {
  id?: number;
  name?: string;
  createdAt?: dayjs.Dayjs | null;
  sounds?: ISounds[] | null;
  game?: IGame | null;
  sounds?: ISounds[] | null;
  payments?: IPayment[] | null;
}

export class Budget implements IBudget {
  constructor(
    public id?: number,
    public name?: string,
    public createdAt?: dayjs.Dayjs | null,
    public sounds?: ISounds[] | null,
    public game?: IGame | null,
    public sounds?: ISounds[] | null,
    public payments?: IPayment[] | null
  ) {}
}

export function getBudgetIdentifier(budget: IBudget): number | undefined {
  return budget.id;
}
