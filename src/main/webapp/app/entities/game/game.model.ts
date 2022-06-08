import { IBudget } from 'app/entities/budget/budget.model';
import { GameGender } from 'app/entities/enumerations/game-gender.model';

export interface IGame {
  id?: number;
  name?: string;
  thumbnailContentType?: string | null;
  thumbnail?: string | null;
  description?: string | null;
  gender?: GameGender | null;
  budgets?: IBudget[] | null;
}

export class Game implements IGame {
  constructor(
    public id?: number,
    public name?: string,
    public thumbnailContentType?: string | null,
    public thumbnail?: string | null,
    public description?: string | null,
    public gender?: GameGender | null,
    public budgets?: IBudget[] | null
  ) {}
}

export function getGameIdentifier(game: IGame): number | undefined {
  return game.id;
}
