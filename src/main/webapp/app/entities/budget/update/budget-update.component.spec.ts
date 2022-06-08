import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { BudgetService } from '../service/budget.service';
import { IBudget, Budget } from '../budget.model';
import { ISounds } from 'app/entities/sounds/sounds.model';
import { SoundsService } from 'app/entities/sounds/service/sounds.service';
import { IPayment } from 'app/entities/payment/payment.model';
import { PaymentService } from 'app/entities/payment/service/payment.service';
import { IGame } from 'app/entities/game/game.model';
import { GameService } from 'app/entities/game/service/game.service';

import { BudgetUpdateComponent } from './budget-update.component';

describe('Budget Management Update Component', () => {
  let comp: BudgetUpdateComponent;
  let fixture: ComponentFixture<BudgetUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let budgetService: BudgetService;
  let soundsService: SoundsService;
  let paymentService: PaymentService;
  let gameService: GameService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [BudgetUpdateComponent],
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
      .overrideTemplate(BudgetUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BudgetUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    budgetService = TestBed.inject(BudgetService);
    soundsService = TestBed.inject(SoundsService);
    paymentService = TestBed.inject(PaymentService);
    gameService = TestBed.inject(GameService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Sounds query and add missing value', () => {
      const budget: IBudget = { id: 456 };
      const sounds: ISounds[] = [{ id: 70909 }];
      budget.sounds = sounds;

      const soundsCollection: ISounds[] = [{ id: 47431 }];
      jest.spyOn(soundsService, 'query').mockReturnValue(of(new HttpResponse({ body: soundsCollection })));
      const additionalSounds = [...sounds];
      const expectedCollection: ISounds[] = [...additionalSounds, ...soundsCollection];
      jest.spyOn(soundsService, 'addSoundsToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ budget });
      comp.ngOnInit();

      expect(soundsService.query).toHaveBeenCalled();
      expect(soundsService.addSoundsToCollectionIfMissing).toHaveBeenCalledWith(soundsCollection, ...additionalSounds);
      expect(comp.soundsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Payment query and add missing value', () => {
      const budget: IBudget = { id: 456 };
      const payments: IPayment[] = [{ id: 2712 }];
      budget.payments = payments;

      const paymentCollection: IPayment[] = [{ id: 24363 }];
      jest.spyOn(paymentService, 'query').mockReturnValue(of(new HttpResponse({ body: paymentCollection })));
      const additionalPayments = [...payments];
      const expectedCollection: IPayment[] = [...additionalPayments, ...paymentCollection];
      jest.spyOn(paymentService, 'addPaymentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ budget });
      comp.ngOnInit();

      expect(paymentService.query).toHaveBeenCalled();
      expect(paymentService.addPaymentToCollectionIfMissing).toHaveBeenCalledWith(paymentCollection, ...additionalPayments);
      expect(comp.paymentsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Game query and add missing value', () => {
      const budget: IBudget = { id: 456 };
      const game: IGame = { id: 16763 };
      budget.game = game;

      const gameCollection: IGame[] = [{ id: 50200 }];
      jest.spyOn(gameService, 'query').mockReturnValue(of(new HttpResponse({ body: gameCollection })));
      const additionalGames = [game];
      const expectedCollection: IGame[] = [...additionalGames, ...gameCollection];
      jest.spyOn(gameService, 'addGameToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ budget });
      comp.ngOnInit();

      expect(gameService.query).toHaveBeenCalled();
      expect(gameService.addGameToCollectionIfMissing).toHaveBeenCalledWith(gameCollection, ...additionalGames);
      expect(comp.gamesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const budget: IBudget = { id: 456 };
      const sounds: ISounds = { id: 75448 };
      budget.sounds = [sounds];
      const payments: IPayment = { id: 88289 };
      budget.payments = [payments];
      const game: IGame = { id: 75387 };
      budget.game = game;

      activatedRoute.data = of({ budget });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(budget));
      expect(comp.soundsSharedCollection).toContain(sounds);
      expect(comp.paymentsSharedCollection).toContain(payments);
      expect(comp.gamesSharedCollection).toContain(game);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Budget>>();
      const budget = { id: 123 };
      jest.spyOn(budgetService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ budget });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: budget }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(budgetService.update).toHaveBeenCalledWith(budget);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Budget>>();
      const budget = new Budget();
      jest.spyOn(budgetService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ budget });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: budget }));
      saveSubject.complete();

      // THEN
      expect(budgetService.create).toHaveBeenCalledWith(budget);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Budget>>();
      const budget = { id: 123 };
      jest.spyOn(budgetService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ budget });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(budgetService.update).toHaveBeenCalledWith(budget);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackSoundsById', () => {
      it('Should return tracked Sounds primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSoundsById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackPaymentById', () => {
      it('Should return tracked Payment primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackPaymentById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackGameById', () => {
      it('Should return tracked Game primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackGameById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });

  describe('Getting selected relationships', () => {
    describe('getSelectedSounds', () => {
      it('Should return option if no Sounds is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedSounds(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected Sounds for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedSounds(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this Sounds is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedSounds(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });

    describe('getSelectedPayment', () => {
      it('Should return option if no Payment is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedPayment(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected Payment for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedPayment(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this Payment is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedPayment(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });
  });
});
