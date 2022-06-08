import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SoundsComponent } from '../list/sounds.component';
import { SoundsDetailComponent } from '../detail/sounds-detail.component';
import { SoundsUpdateComponent } from '../update/sounds-update.component';
import { SoundsRoutingResolveService } from './sounds-routing-resolve.service';

const soundsRoute: Routes = [
  {
    path: '',
    component: SoundsComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SoundsDetailComponent,
    resolve: {
      sounds: SoundsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SoundsUpdateComponent,
    resolve: {
      sounds: SoundsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SoundsUpdateComponent,
    resolve: {
      sounds: SoundsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(soundsRoute)],
  exports: [RouterModule],
})
export class SoundsRoutingModule {}
