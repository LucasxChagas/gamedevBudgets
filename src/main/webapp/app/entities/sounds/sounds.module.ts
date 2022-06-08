import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SoundsComponent } from './list/sounds.component';
import { SoundsDetailComponent } from './detail/sounds-detail.component';
import { SoundsUpdateComponent } from './update/sounds-update.component';
import { SoundsDeleteDialogComponent } from './delete/sounds-delete-dialog.component';
import { SoundsRoutingModule } from './route/sounds-routing.module';

@NgModule({
  imports: [SharedModule, SoundsRoutingModule],
  declarations: [SoundsComponent, SoundsDetailComponent, SoundsUpdateComponent, SoundsDeleteDialogComponent],
  entryComponents: [SoundsDeleteDialogComponent],
})
export class SoundsModule {}
