import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { KitchenRecipesService } from '../service/kitchen-recipes.service';
import { IKitchenRecipes, KitchenRecipes } from '../kitchen-recipes.model';

import { KitchenRecipesUpdateComponent } from './kitchen-recipes-update.component';

describe('KitchenRecipes Management Update Component', () => {
  let comp: KitchenRecipesUpdateComponent;
  let fixture: ComponentFixture<KitchenRecipesUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let kitchenRecipesService: KitchenRecipesService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [KitchenRecipesUpdateComponent],
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
      .overrideTemplate(KitchenRecipesUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(KitchenRecipesUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    kitchenRecipesService = TestBed.inject(KitchenRecipesService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const kitchenRecipes: IKitchenRecipes = { id: 456 };

      activatedRoute.data = of({ kitchenRecipes });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(kitchenRecipes));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<KitchenRecipes>>();
      const kitchenRecipes = { id: 123 };
      jest.spyOn(kitchenRecipesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ kitchenRecipes });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: kitchenRecipes }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(kitchenRecipesService.update).toHaveBeenCalledWith(kitchenRecipes);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<KitchenRecipes>>();
      const kitchenRecipes = new KitchenRecipes();
      jest.spyOn(kitchenRecipesService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ kitchenRecipes });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: kitchenRecipes }));
      saveSubject.complete();

      // THEN
      expect(kitchenRecipesService.create).toHaveBeenCalledWith(kitchenRecipes);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<KitchenRecipes>>();
      const kitchenRecipes = { id: 123 };
      jest.spyOn(kitchenRecipesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ kitchenRecipes });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(kitchenRecipesService.update).toHaveBeenCalledWith(kitchenRecipes);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
