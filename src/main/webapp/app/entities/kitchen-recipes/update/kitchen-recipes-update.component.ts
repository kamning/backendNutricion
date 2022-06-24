import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IKitchenRecipes, KitchenRecipes } from '../kitchen-recipes.model';
import { KitchenRecipesService } from '../service/kitchen-recipes.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-kitchen-recipes-update',
  templateUrl: './kitchen-recipes-update.component.html',
})
export class KitchenRecipesUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    description: [],
    ingredients: [],
    preparation: [],
    image: [],
    imageContentType: [],
    status: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected kitchenRecipesService: KitchenRecipesService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ kitchenRecipes }) => {
      this.updateForm(kitchenRecipes);
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('backendApp.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const kitchenRecipes = this.createFromForm();
    if (kitchenRecipes.id !== undefined) {
      this.subscribeToSaveResponse(this.kitchenRecipesService.update(kitchenRecipes));
    } else {
      this.subscribeToSaveResponse(this.kitchenRecipesService.create(kitchenRecipes));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IKitchenRecipes>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(kitchenRecipes: IKitchenRecipes): void {
    this.editForm.patchValue({
      id: kitchenRecipes.id,
      name: kitchenRecipes.name,
      description: kitchenRecipes.description,
      ingredients: kitchenRecipes.ingredients,
      preparation: kitchenRecipes.preparation,
      image: kitchenRecipes.image,
      imageContentType: kitchenRecipes.imageContentType,
      status: kitchenRecipes.status,
    });
  }

  protected createFromForm(): IKitchenRecipes {
    return {
      ...new KitchenRecipes(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
      ingredients: this.editForm.get(['ingredients'])!.value,
      preparation: this.editForm.get(['preparation'])!.value,
      imageContentType: this.editForm.get(['imageContentType'])!.value,
      image: this.editForm.get(['image'])!.value,
      status: this.editForm.get(['status'])!.value,
    };
  }
}
