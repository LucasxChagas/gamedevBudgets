import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISounds } from '../sounds.model';

@Component({
  selector: 'jhi-sounds-detail',
  templateUrl: './sounds-detail.component.html',
})
export class SoundsDetailComponent implements OnInit {
  sounds: ISounds | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sounds }) => {
      this.sounds = sounds;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
