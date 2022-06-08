import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ISounds, Sounds } from '../sounds.model';
import { SoundsService } from '../service/sounds.service';
import { IBudget } from 'app/entities/budget/budget.model';
import { BudgetService } from 'app/entities/budget/service/budget.service';
import { SoundTypes } from 'app/entities/enumerations/sound-types.model';
import { SoundFormats } from 'app/entities/enumerations/sound-formats.model';

@Component({
  selector: 'jhi-sounds-update',
  templateUrl: './sounds-update.component.html',
})
export class SoundsUpdateComponent implements OnInit {
  isSaving = false;
  soundTypesValues = Object.keys(SoundTypes);
  soundFormatsValues = Object.keys(SoundFormats);

  budgetsSharedCollection: IBudget[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    type: [],
    format: [],
    sounds: [],
  });

  constructor(
    protected soundsService: SoundsService,
    protected budgetService: BudgetService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sounds }) => {
      this.updateForm(sounds);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const sounds = this.createFromForm();
    if (sounds.id !== undefined) {
      this.subscribeToSaveResponse(this.soundsService.update(sounds));
    } else {
      this.subscribeToSaveResponse(this.soundsService.create(sounds));
    }
  }

  trackBudgetById(_index: number, item: IBudget): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISounds>>): void {
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

  protected updateForm(sounds: ISounds): void {
    this.editForm.patchValue({
      id: sounds.id,
      name: sounds.name,
      type: sounds.type,
      format: sounds.format,
      sounds: sounds.sounds,
    });

    this.budgetsSharedCollection = this.budgetService.addBudgetToCollectionIfMissing(this.budgetsSharedCollection, sounds.sounds);
  }

  protected loadRelationshipsOptions(): void {
    this.budgetService
      .query()
      .pipe(map((res: HttpResponse<IBudget[]>) => res.body ?? []))
      .pipe(map((budgets: IBudget[]) => this.budgetService.addBudgetToCollectionIfMissing(budgets, this.editForm.get('sounds')!.value)))
      .subscribe((budgets: IBudget[]) => (this.budgetsSharedCollection = budgets));
  }

  protected createFromForm(): ISounds {
    return {
      ...new Sounds(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      type: this.editForm.get(['type'])!.value,
      format: this.editForm.get(['format'])!.value,
      sounds: this.editForm.get(['sounds'])!.value,
    };
  }
}
