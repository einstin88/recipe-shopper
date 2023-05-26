import { HttpErrorResponse } from '@angular/common/http';
import {
  AfterViewInit,
  Component,
  OnDestroy,
  OnInit,
  ViewChild,
} from '@angular/core';
import { Store } from '@ngrx/store';
import { Subject, Subscription } from 'rxjs';
import { RecipeFormComponent } from 'src/app/components/recipe-form/recipe-form.component';
import { selectCurrentUser } from 'src/app/flux/auth/auth.selector';
import { State } from 'src/app/flux/reducers';
import { Product } from 'src/app/model/product.model';
import { RecipeFormDisplay } from 'src/app/model/recipe-form-display.interface';
import { RecipeDataService } from 'src/app/services/recipe-data.service';
import { ToastNotificationService } from 'src/app/services/toast-notification.service';

/**
 * @description A component associated with a router path for creating new recipes
 */
@Component({
  templateUrl: './new-recipe.component.html',
  styleUrls: ['./new-recipe.component.css'],
})
export class NewRecipeComponent
  implements OnInit, AfterViewInit, OnDestroy, RecipeFormDisplay
{
  constructor(
    private store: Store<State>,
    private recipeSvc: RecipeDataService,
    private toastSvc: ToastNotificationService
  ) {}

  @ViewChild(RecipeFormComponent)
  recipeForm!: RecipeFormComponent;

  // Variables
  ingredientToAdd = new Subject<Product>();
  isInvalid!: boolean;

  recipeCreator!: string;

  sub$!: Subscription;
  sub1$!: Subscription;

  ngOnInit(): void {
    this.sub1$ = this.store.select(selectCurrentUser).subscribe((username) => {
      this.recipeCreator = username;
    });

    // this.toastSvc.show('Recipe', 'New recipe added', '/recipe/view', 'test');
  }

  ngAfterViewInit(): void {
    // https://stackoverflow.com/questions/54366173/how-to-trigger-toast-on-page-load
    setTimeout(() => {
      this.sub$ = this.recipeForm.isInvalid$.subscribe((isInvalid) => {
        this.isInvalid = isInvalid;
      });
    });
  }

  ngOnDestroy(): void {
    this.sub$.unsubscribe();
    this.sub1$.unsubscribe();
  }

  processForm() {
    const formData = this.recipeForm.recipeData;
    // console.info('>>> Submitted form', formData);

    this.recipeSvc
      .postNewRecipe(formData)
      .then((res) => {
        this.recipeForm.recipeErr = '';
        this.recipeForm.reset = true;

        this.toastSvc.show(
          'Recipe',
          'New recipe added: ',
          `/recipe/view/${res.recipeId}`,
          formData.recipeName
        );
      })
      .catch((err: HttpErrorResponse) => {
        console.log('error', err);
        this.recipeForm.recipeErr = err.error;
      });
  }

  addIngredient(product: Product) {
    this.ingredientToAdd.next(product);
  }
}
