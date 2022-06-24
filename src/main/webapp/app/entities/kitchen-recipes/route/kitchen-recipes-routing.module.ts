import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { KitchenRecipesComponent } from '../list/kitchen-recipes.component';
import { KitchenRecipesDetailComponent } from '../detail/kitchen-recipes-detail.component';
import { KitchenRecipesUpdateComponent } from '../update/kitchen-recipes-update.component';
import { KitchenRecipesRoutingResolveService } from './kitchen-recipes-routing-resolve.service';

const kitchenRecipesRoute: Routes = [
  {
    path: '',
    component: KitchenRecipesComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: KitchenRecipesDetailComponent,
    resolve: {
      kitchenRecipes: KitchenRecipesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: KitchenRecipesUpdateComponent,
    resolve: {
      kitchenRecipes: KitchenRecipesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: KitchenRecipesUpdateComponent,
    resolve: {
      kitchenRecipes: KitchenRecipesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(kitchenRecipesRoute)],
  exports: [RouterModule],
})
export class KitchenRecipesRoutingModule {}
