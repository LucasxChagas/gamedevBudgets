import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IBudget } from '../budget.model';
import { BudgetService } from '../service/budget.service';
import { BudgetDeleteDialogComponent } from '../delete/budget-delete-dialog.component';

@Component({
  selector: 'jhi-budget',
  templateUrl: './budget.component.html',
})
export class BudgetComponent implements OnInit {
  budgets?: IBudget[];
  isLoading = false;

  constructor(protected budgetService: BudgetService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.budgetService.query().subscribe({
      next: (res: HttpResponse<IBudget[]>) => {
        this.isLoading = false;
        this.budgets = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IBudget): number {
    return item.id!;
  }

  delete(budget: IBudget): void {
    const modalRef = this.modalService.open(BudgetDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.budget = budget;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
