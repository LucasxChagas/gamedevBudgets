import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SoundsDetailComponent } from './sounds-detail.component';

describe('Sounds Management Detail Component', () => {
  let comp: SoundsDetailComponent;
  let fixture: ComponentFixture<SoundsDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SoundsDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ sounds: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(SoundsDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(SoundsDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load sounds on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.sounds).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
