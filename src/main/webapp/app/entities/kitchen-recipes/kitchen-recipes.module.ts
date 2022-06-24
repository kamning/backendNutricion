import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { KitchenRecipesComponent } from './list/kitchen-recipes.component';
import { KitchenRecipesDetailComponent } from './detail/kitchen-recipes-detail.component';
import { KitchenRecipesUpdateComponent } from './update/kitchen-recipes-update.component';
import { KitchenRecipesDeleteDialogComponent } from './delete/kitchen-recipes-delete-dialog.component';
import { KitchenRecipesRoutingModule } from './route/kitchen-recipes-routing.module';

@NgModule({
  imports: [SharedModule, KitchenRecipesRoutingModule],
  declarations: [
    KitchenRecipesComponent,
    KitchenRecipesDetailComponent,
    KitchenRecipesUpdateComponent,
    KitchenRecipesDeleteDialogComponent,
  ],
  entryComponents: [KitchenRecipesDeleteDialogComponent],
})
export class KitchenRecipesModule {}
