import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { SoundsService } from '../service/sounds.service';

import { SoundsComponent } from './sounds.component';

describe('Sounds Management Component', () => {
  let comp: SoundsComponent;
  let fixture: ComponentFixture<SoundsComponent>;
  let service: SoundsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [SoundsComponent],
    })
      .overrideTemplate(SoundsComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SoundsComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(SoundsService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.sounds?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
