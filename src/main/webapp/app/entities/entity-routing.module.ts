import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'game',
        data: { pageTitle: 'gamedevBudgetsApp.game.home.title' },
        loadChildren: () => import('./game/game.module').then(m => m.GameModule),
      },
      {
        path: 'budget',
        data: { pageTitle: 'gamedevBudgetsApp.budget.home.title' },
        loadChildren: () => import('./budget/budget.module').then(m => m.BudgetModule),
      },
      {
        path: 'payment',
        data: { pageTitle: 'gamedevBudgetsApp.payment.home.title' },
        loadChildren: () => import('./payment/payment.module').then(m => m.PaymentModule),
      },
      {
        path: 'sounds',
        data: { pageTitle: 'gamedevBudgetsApp.sounds.home.title' },
        loadChildren: () => import('./sounds/sounds.module').then(m => m.SoundsModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
