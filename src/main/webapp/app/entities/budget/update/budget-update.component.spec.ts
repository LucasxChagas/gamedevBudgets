import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { BudgetService } from '../service/budget.service';
import { IBudget, Budget } from '../budget.model';
import { IGame } from 'app/entities/game/game.model';
import { GameService } from 'app/entities/game/service/game.service';

import { BudgetUpdateComponent } from './budget-update.component';

describe('Budget Management Update Component', () => {
  let comp: BudgetUpdateComponent;
  let fixture: ComponentFixture<BudgetUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let budgetService: BudgetService;
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
    gameService = TestBed.inject(GameService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
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
      const game: IGame = { id: 75387 };
      budget.game = game;

      activatedRoute.data = of({ budget });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(budget));
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
    describe('trackGameById', () => {
      it('Should return tracked Game primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackGameById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
