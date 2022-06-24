import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { KitchenRecipesService } from '../service/kitchen-recipes.service';

import { KitchenRecipesComponent } from './kitchen-recipes.component';

describe('KitchenRecipes Management Component', () => {
  let comp: KitchenRecipesComponent;
  let fixture: ComponentFixture<KitchenRecipesComponent>;
  let service: KitchenRecipesService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [KitchenRecipesComponent],
    })
      .overrideTemplate(KitchenRecipesComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(KitchenRecipesComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(KitchenRecipesService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.kitchenRecipes?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
