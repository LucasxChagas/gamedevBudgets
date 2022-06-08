import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IPayment, Payment } from '../payment.model';
import { PaymentService } from '../service/payment.service';
import { IBudget } from 'app/entities/budget/budget.model';
import { BudgetService } from 'app/entities/budget/service/budget.service';

@Component({
  selector: 'jhi-payment-update',
  templateUrl: './payment-update.component.html',
})
export class PaymentUpdateComponent implements OnInit {
  isSaving = false;

  budgetsSharedCollection: IBudget[] = [];

  editForm = this.fb.group({
    id: [],
    paymentType: [null, [Validators.required]],
    budgets: [],
  });

  constructor(
    protected paymentService: PaymentService,
    protected budgetService: BudgetService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ payment }) => {
      this.updateForm(payment);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const payment = this.createFromForm();
    if (payment.id !== undefined) {
      this.subscribeToSaveResponse(this.paymentService.update(payment));
    } else {
      this.subscribeToSaveResponse(this.paymentService.create(payment));
    }
  }

  trackBudgetById(_index: number, item: IBudget): number {
    return item.id!;
  }

  getSelectedBudget(option: IBudget, selectedVals?: IBudget[]): IBudget {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPayment>>): void {
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

  protected updateForm(payment: IPayment): void {
    this.editForm.patchValue({
      id: payment.id,
      paymentType: payment.paymentType,
      budgets: payment.budgets,
    });

    this.budgetsSharedCollection = this.budgetService.addBudgetToCollectionIfMissing(
      this.budgetsSharedCollection,
      ...(payment.budgets ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.budgetService
      .query()
      .pipe(map((res: HttpResponse<IBudget[]>) => res.body ?? []))
      .pipe(
        map((budgets: IBudget[]) =>
          this.budgetService.addBudgetToCollectionIfMissing(budgets, ...(this.editForm.get('budgets')!.value ?? []))
        )
      )
      .subscribe((budgets: IBudget[]) => (this.budgetsSharedCollection = budgets));
  }

  protected createFromForm(): IPayment {
    return {
      ...new Payment(),
      id: this.editForm.get(['id'])!.value,
      paymentType: this.editForm.get(['paymentType'])!.value,
      budgets: this.editForm.get(['budgets'])!.value,
    };
  }
}
