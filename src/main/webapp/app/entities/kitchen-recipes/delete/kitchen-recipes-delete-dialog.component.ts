import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IKitchenRecipes } from '../kitchen-recipes.model';
import { KitchenRecipesService } from '../service/kitchen-recipes.service';

@Component({
  templateUrl: './kitchen-recipes-delete-dialog.component.html',
})
export class KitchenRecipesDeleteDialogComponent {
  kitchenRecipes?: IKitchenRecipes;

  constructor(protected kitchenRecipesService: KitchenRecipesService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.kitchenRecipesService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
