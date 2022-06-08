import { IBudget } from 'app/entities/budget/budget.model';
import { SoundTypes } from 'app/entities/enumerations/sound-types.model';
import { SoundFormats } from 'app/entities/enumerations/sound-formats.model';

export interface ISounds {
  id?: number;
  name?: string;
  type?: SoundTypes | null;
  format?: SoundFormats | null;
  budgets?: IBudget[] | null;
}

export class Sounds implements ISounds {
  constructor(
    public id?: number,
    public name?: string,
    public type?: SoundTypes | null,
    public format?: SoundFormats | null,
    public budgets?: IBudget[] | null
  ) {}
}

export function getSoundsIdentifier(sounds: ISounds): number | undefined {
  return sounds.id;
}
