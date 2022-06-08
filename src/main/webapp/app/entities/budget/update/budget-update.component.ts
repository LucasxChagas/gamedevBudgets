import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IBudget, Budget } from '../budget.model';
import { BudgetService } from '../service/budget.service';
import { ISounds } from 'app/entities/sounds/sounds.model';
import { SoundsService } from 'app/entities/sounds/service/sounds.service';
import { IGame } from 'app/entities/game/game.model';
import { GameService } from 'app/entities/game/service/game.service';

@Component({
  selector: 'jhi-budget-update',
  templateUrl: './budget-update.component.html',
})
export class BudgetUpdateComponent implements OnInit {
  isSaving = false;

  soundsSharedCollection: ISounds[] = [];
  gamesSharedCollection: IGame[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    createdAt: [],
    sounds: [],
    game: [],
  });

  constructor(
    protected budgetService: BudgetService,
    protected soundsService: SoundsService,
    protected gameService: GameService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ budget }) => {
      if (budget.id === undefined) {
        const today = dayjs().startOf('day');
        budget.createdAt = today;
      }

      this.updateForm(budget);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const budget = this.createFromForm();
    if (budget.id !== undefined) {
      this.subscribeToSaveResponse(this.budgetService.update(budget));
    } else {
      this.subscribeToSaveResponse(this.budgetService.create(budget));
    }
  }

  trackSoundsById(_index: number, item: ISounds): number {
    return item.id!;
  }

  trackGameById(_index: number, item: IGame): number {
    return item.id!;
  }

  getSelectedSounds(option: ISounds, selectedVals?: ISounds[]): ISounds {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBudget>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(budget: IBudget): void {
    this.editForm.patchValue({
      id: budget.id,
      name: budget.name,
      createdAt: budget.createdAt ? budget.createdAt.format(DATE_TIME_FORMAT) : null,
      sounds: budget.sounds,
      game: budget.game,
    });

    this.soundsSharedCollection = this.soundsService.addSoundsToCollectionIfMissing(this.soundsSharedCollection, ...(budget.sounds ?? []));
    this.gamesSharedCollection = this.gameService.addGameToCollectionIfMissing(this.gamesSharedCollection, budget.game);
  }

  protected loadRelationshipsOptions(): void {
    this.soundsService
      .query()
      .pipe(map((res: HttpResponse<ISounds[]>) => res.body ?? []))
      .pipe(
        map((sounds: ISounds[]) => this.soundsService.addSoundsToCollectionIfMissing(sounds, ...(this.editForm.get('sounds')!.value ?? [])))
      )
      .subscribe((sounds: ISounds[]) => (this.soundsSharedCollection = sounds));

    this.gameService
      .query()
      .pipe(map((res: HttpResponse<IGame[]>) => res.body ?? []))
      .pipe(map((games: IGame[]) => this.gameService.addGameToCollectionIfMissing(games, this.editForm.get('game')!.value)))
      .subscribe((games: IGame[]) => (this.gamesSharedCollection = games));
  }

  protected createFromForm(): IBudget {
    return {
      ...new Budget(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      createdAt: this.editForm.get(['createdAt'])!.value ? dayjs(this.editForm.get(['createdAt'])!.value, DATE_TIME_FORMAT) : undefined,
      sounds: this.editForm.get(['sounds'])!.value,
      game: this.editForm.get(['game'])!.value,
    };
  }
}
