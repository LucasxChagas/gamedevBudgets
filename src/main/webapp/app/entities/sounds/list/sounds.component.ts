import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ISounds } from '../sounds.model';
import { SoundsService } from '../service/sounds.service';
import { SoundsDeleteDialogComponent } from '../delete/sounds-delete-dialog.component';

@Component({
  selector: 'jhi-sounds',
  templateUrl: './sounds.component.html',
})
export class SoundsComponent implements OnInit {
  sounds?: ISounds[];
  isLoading = false;

  constructor(protected soundsService: SoundsService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.soundsService.query().subscribe({
      next: (res: HttpResponse<ISounds[]>) => {
        this.isLoading = false;
        this.sounds = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: ISounds): number {
    return item.id!;
  }

  delete(sounds: ISounds): void {
    const modalRef = this.modalService.open(SoundsDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.sounds = sounds;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
