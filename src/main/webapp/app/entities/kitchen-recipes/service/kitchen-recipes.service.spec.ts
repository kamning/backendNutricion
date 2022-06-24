import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IKitchenRecipes, KitchenRecipes } from '../kitchen-recipes.model';

import { KitchenRecipesService } from './kitchen-recipes.service';

describe('KitchenRecipes Service', () => {
  let service: KitchenRecipesService;
  let httpMock: HttpTestingController;
  let elemDefault: IKitchenRecipes;
  let expectedResult: IKitchenRecipes | IKitchenRecipes[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(KitchenRecipesService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      name: 'AAAAAAA',
      description: 'AAAAAAA',
      ingredients: 'AAAAAAA',
      preparation: 'AAAAAAA',
      imageContentType: 'image/png',
      image: 'AAAAAAA',
      status: false,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a KitchenRecipes', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new KitchenRecipes()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a KitchenRecipes', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          description: 'BBBBBB',
          ingredients: 'BBBBBB',
          preparation: 'BBBBBB',
          image: 'BBBBBB',
          status: true,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a KitchenRecipes', () => {
      const patchObject = Object.assign(
        {
          preparation: 'BBBBBB',
          image: 'BBBBBB',
        },
        new KitchenRecipes()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of KitchenRecipes', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          description: 'BBBBBB',
          ingredients: 'BBBBBB',
          preparation: 'BBBBBB',
          image: 'BBBBBB',
          status: true,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a KitchenRecipes', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addKitchenRecipesToCollectionIfMissing', () => {
      it('should add a KitchenRecipes to an empty array', () => {
        const kitchenRecipes: IKitchenRecipes = { id: 123 };
        expectedResult = service.addKitchenRecipesToCollectionIfMissing([], kitchenRecipes);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(kitchenRecipes);
      });

      it('should not add a KitchenRecipes to an array that contains it', () => {
        const kitchenRecipes: IKitchenRecipes = { id: 123 };
        const kitchenRecipesCollection: IKitchenRecipes[] = [
          {
            ...kitchenRecipes,
          },
          { id: 456 },
        ];
        expectedResult = service.addKitchenRecipesToCollectionIfMissing(kitchenRecipesCollection, kitchenRecipes);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a KitchenRecipes to an array that doesn't contain it", () => {
        const kitchenRecipes: IKitchenRecipes = { id: 123 };
        const kitchenRecipesCollection: IKitchenRecipes[] = [{ id: 456 }];
        expectedResult = service.addKitchenRecipesToCollectionIfMissing(kitchenRecipesCollection, kitchenRecipes);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(kitchenRecipes);
      });

      it('should add only unique KitchenRecipes to an array', () => {
        const kitchenRecipesArray: IKitchenRecipes[] = [{ id: 123 }, { id: 456 }, { id: 16853 }];
        const kitchenRecipesCollection: IKitchenRecipes[] = [{ id: 123 }];
        expectedResult = service.addKitchenRecipesToCollectionIfMissing(kitchenRecipesCollection, ...kitchenRecipesArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const kitchenRecipes: IKitchenRecipes = { id: 123 };
        const kitchenRecipes2: IKitchenRecipes = { id: 456 };
        expectedResult = service.addKitchenRecipesToCollectionIfMissing([], kitchenRecipes, kitchenRecipes2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(kitchenRecipes);
        expect(expectedResult).toContain(kitchenRecipes2);
      });

      it('should accept null and undefined values', () => {
        const kitchenRecipes: IKitchenRecipes = { id: 123 };
        expectedResult = service.addKitchenRecipesToCollectionIfMissing([], null, kitchenRecipes, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(kitchenRecipes);
      });

      it('should return initial array if no KitchenRecipes is added', () => {
        const kitchenRecipesCollection: IKitchenRecipes[] = [{ id: 123 }];
        expectedResult = service.addKitchenRecipesToCollectionIfMissing(kitchenRecipesCollection, undefined, null);
        expect(expectedResult).toEqual(kitchenRecipesCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
