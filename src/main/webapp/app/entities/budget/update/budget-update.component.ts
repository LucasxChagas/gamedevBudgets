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
import { IGame } from 'app/entities/game/game.model';
import { GameService } from 'app/entities/game/service/game.service';

@Component({
  selector: 'jhi-budget-update',
  templateUrl: './budget-update.component.html',
})
export class BudgetUpdateComponent implements OnInit {
  isSaving = false;

  gamesSharedCollection: IGame[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    createdAt: [],
    game: [],
  });

  constructor(
    protected budgetService: BudgetService,
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

  trackGameById(_index: number, item: IGame): number {
    return item.id!;
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
      game: budget.game,
    });

    this.gamesSharedCollection = this.gameService.addGameToCollectionIfMissing(this.gamesSharedCollection, budget.game);
  }

  protected loadRelationshipsOptions(): void {
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
      game: this.editForm.get(['game'])!.value,
    };
  }
}
