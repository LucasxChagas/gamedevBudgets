import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISounds } from '../sounds.model';
import { SoundsService } from '../service/sounds.service';

@Component({
  templateUrl: './sounds-delete-dialog.component.html',
})
export class SoundsDeleteDialogComponent {
  sounds?: ISounds;

  constructor(protected soundsService: SoundsService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.soundsService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
