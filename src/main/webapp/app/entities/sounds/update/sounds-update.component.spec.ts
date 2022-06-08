import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { SoundsService } from '../service/sounds.service';
import { ISounds, Sounds } from '../sounds.model';
import { IBudget } from 'app/entities/budget/budget.model';
import { BudgetService } from 'app/entities/budget/service/budget.service';

import { SoundsUpdateComponent } from './sounds-update.component';

describe('Sounds Management Update Component', () => {
  let comp: SoundsUpdateComponent;
  let fixture: ComponentFixture<SoundsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let soundsService: SoundsService;
  let budgetService: BudgetService;

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
    budgetService = TestBed.inject(BudgetService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Budget query and add missing value', () => {
      const sounds: ISounds = { id: 456 };
      const budgets: IBudget[] = [{ id: 8800 }];
      sounds.budgets = budgets;

      const budgetCollection: IBudget[] = [{ id: 32628 }];
      jest.spyOn(budgetService, 'query').mockReturnValue(of(new HttpResponse({ body: budgetCollection })));
      const additionalBudgets = [...budgets];
      const expectedCollection: IBudget[] = [...additionalBudgets, ...budgetCollection];
      jest.spyOn(budgetService, 'addBudgetToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ sounds });
      comp.ngOnInit();

      expect(budgetService.query).toHaveBeenCalled();
      expect(budgetService.addBudgetToCollectionIfMissing).toHaveBeenCalledWith(budgetCollection, ...additionalBudgets);
      expect(comp.budgetsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const sounds: ISounds = { id: 456 };
      const budgets: IBudget = { id: 6518 };
      sounds.budgets = [budgets];

      activatedRoute.data = of({ sounds });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(sounds));
      expect(comp.budgetsSharedCollection).toContain(budgets);
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

  describe('Tracking relationships identifiers', () => {
    describe('trackBudgetById', () => {
      it('Should return tracked Budget primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackBudgetById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });

  describe('Getting selected relationships', () => {
    describe('getSelectedBudget', () => {
      it('Should return option if no Budget is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedBudget(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected Budget for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedBudget(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this Budget is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedBudget(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });
  });
});
