import dayjs from 'dayjs/esm';
import { IGame } from 'app/entities/game/game.model';

export interface IBudget {
  id?: number;
  name?: string;
  createdAt?: dayjs.Dayjs | null;
  game?: IGame | null;
}

export class Budget implements IBudget {
  constructor(public id?: number, public name?: string, public createdAt?: dayjs.Dayjs | null, public game?: IGame | null) {}
}

export function getBudgetIdentifier(budget: IBudget): number | undefined {
  return budget.id;
}
