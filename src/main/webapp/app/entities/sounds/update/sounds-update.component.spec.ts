import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { SoundsService } from '../service/sounds.service';
import { ISounds, Sounds } from '../sounds.model';

import { SoundsUpdateComponent } from './sounds-update.component';

describe('Sounds Management Update Component', () => {
  let comp: SoundsUpdateComponent;
  let fixture: ComponentFixture<SoundsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let soundsService: SoundsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [SoundsUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(SoundsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SoundsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    soundsService = TestBed.inject(SoundsService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const sounds: ISounds = { id: 456 };

      activatedRoute.data = of({ sounds });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(sounds));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Sounds>>();
      const sounds = { id: 123 };
      jest.spyOn(soundsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sounds });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sounds }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(soundsService.update).toHaveBeenCalledWith(sounds);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Sounds>>();
      const sounds = new Sounds();
      jest.spyOn(soundsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sounds });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sounds }));
      saveSubject.complete();

      // THEN
      expect(soundsService.create).toHaveBeenCalledWith(sounds);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Sounds>>();
      const sounds = { id: 123 };
      jest.spyOn(soundsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sounds });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(soundsService.update).toHaveBeenCalledWith(sounds);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
