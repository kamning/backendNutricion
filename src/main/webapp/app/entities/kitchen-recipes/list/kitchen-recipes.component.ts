import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IKitchenRecipes } from '../kitchen-recipes.model';
import { KitchenRecipesService } from '../service/kitchen-recipes.service';
import { KitchenRecipesDeleteDialogComponent } from '../delete/kitchen-recipes-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-kitchen-recipes',
  templateUrl: './kitchen-recipes.component.html',
})
export class KitchenRecipesComponent implements OnInit {
  kitchenRecipes?: IKitchenRecipes[];
  isLoading = false;

  constructor(protected kitchenRecipesService: KitchenRecipesService, protected dataUtils: DataUtils, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.kitchenRecipesService.query().subscribe({
      next: (res: HttpResponse<IKitchenRecipes[]>) => {
        this.isLoading = false;
        this.kitchenRecipes = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IKitchenRecipes): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(kitchenRecipes: IKitchenRecipes): void {
    const modalRef = this.modalService.open(KitchenRecipesDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.kitchenRecipes = kitchenRecipes;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
