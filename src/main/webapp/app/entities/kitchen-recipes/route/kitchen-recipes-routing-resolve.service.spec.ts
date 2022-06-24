import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IKitchenRecipes, KitchenRecipes } from '../kitchen-recipes.model';
import { KitchenRecipesService } from '../service/kitchen-recipes.service';

import { KitchenRecipesRoutingResolveService } from './kitchen-recipes-routing-resolve.service';

describe('KitchenRecipes routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: KitchenRecipesRoutingResolveService;
  let service: KitchenRecipesService;
  let resultKitchenRecipes: IKitchenRecipes | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(KitchenRecipesRoutingResolveService);
    service = TestBed.inject(KitchenRecipesService);
    resultKitchenRecipes = undefined;
  });

  describe('resolve', () => {
    it('should return IKitchenRecipes returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultKitchenRecipes = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultKitchenRecipes).toEqual({ id: 123 });
    });

    it('should return new IKitchenRecipes if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultKitchenRecipes = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultKitchenRecipes).toEqual(new KitchenRecipes());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as KitchenRecipes })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultKitchenRecipes = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultKitchenRecipes).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
