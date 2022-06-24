import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'kitchen-recipes',
        data: { pageTitle: 'backendApp.kitchenRecipes.home.title' },
        loadChildren: () => import('./kitchen-recipes/kitchen-recipes.module').then(m => m.KitchenRecipesModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
