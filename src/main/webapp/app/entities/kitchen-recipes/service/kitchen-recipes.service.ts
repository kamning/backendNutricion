import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IKitchenRecipes, getKitchenRecipesIdentifier } from '../kitchen-recipes.model';

export type EntityResponseType = HttpResponse<IKitchenRecipes>;
export type EntityArrayResponseType = HttpResponse<IKitchenRecipes[]>;

@Injectable({ providedIn: 'root' })
export class KitchenRecipesService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/kitchen-recipes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(kitchenRecipes: IKitchenRecipes): Observable<EntityResponseType> {
    return this.http.post<IKitchenRecipes>(this.resourceUrl, kitchenRecipes, { observe: 'response' });
  }

  update(kitchenRecipes: IKitchenRecipes): Observable<EntityResponseType> {
    return this.http.put<IKitchenRecipes>(`${this.resourceUrl}/${getKitchenRecipesIdentifier(kitchenRecipes) as number}`, kitchenRecipes, {
      observe: 'response',
    });
  }

  partialUpdate(kitchenRecipes: IKitchenRecipes): Observable<EntityResponseType> {
    return this.http.patch<IKitchenRecipes>(
      `${this.resourceUrl}/${getKitchenRecipesIdentifier(kitchenRecipes) as number}`,
      kitchenRecipes,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IKitchenRecipes>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IKitchenRecipes[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addKitchenRecipesToCollectionIfMissing(
    kitchenRecipesCollection: IKitchenRecipes[],
    ...kitchenRecipesToCheck: (IKitchenRecipes | null | undefined)[]
  ): IKitchenRecipes[] {
    const kitchenRecipes: IKitchenRecipes[] = kitchenRecipesToCheck.filter(isPresent);
    if (kitchenRecipes.length > 0) {
      const kitchenRecipesCollectionIdentifiers = kitchenRecipesCollection.map(
        kitchenRecipesItem => getKitchenRecipesIdentifier(kitchenRecipesItem)!
      );
      const kitchenRecipesToAdd = kitchenRecipes.filter(kitchenRecipesItem => {
        const kitchenRecipesIdentifier = getKitchenRecipesIdentifier(kitchenRecipesItem);
        if (kitchenRecipesIdentifier == null || kitchenRecipesCollectionIdentifiers.includes(kitchenRecipesIdentifier)) {
          return false;
        }
        kitchenRecipesCollectionIdentifiers.push(kitchenRecipesIdentifier);
        return true;
      });
      return [...kitchenRecipesToAdd, ...kitchenRecipesCollection];
    }
    return kitchenRecipesCollection;
  }
}
