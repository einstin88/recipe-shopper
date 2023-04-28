import { AfterViewInit, Component, ViewChild } from '@angular/core';
import { Observable, Subject } from 'rxjs';
import { RecipeFormComponent } from 'src/app/components/recipe-form/recipe-form.component';
import { Product } from 'src/app/model/product.model';
import { RecipeFormDisplay } from 'src/app/model/recipe-form-display.interface';
import { RecipeDataService } from 'src/app/services/recipe-data.service';

/**
 * @description A component associated with a router path for creating new recipes
 */
@Component({
  templateUrl: './new-recipe.component.html',
  styleUrls: ['./new-recipe.component.css'],
})
export class NewRecipeComponent implements AfterViewInit, RecipeFormDisplay {
  constructor(private recipeSvc: RecipeDataService) {}

  @ViewChild(RecipeFormComponent)
  recipeForm!: RecipeFormComponent;

  // Variables
  ingredientToAdd = new Subject<Product>();
  isValid$!: Observable<boolean>;

  ngAfterViewInit(): void {
    this.isValid$ = this.recipeForm.isInvalid$;
  }

  processForm() {
    const formData = this.recipeForm.recipeData;
    // console.info('>>> Submitted form', formData);

    this.recipeSvc
      .postNewRecipe(formData)
      .then(() => {
        this.recipeForm.recipeErr = '';
        this.recipeForm.reset = true;
      })
      .catch((err) => {
        this.recipeForm.recipeError = err.error;
      });
  }

  addIngredient(product: Product) {
    this.ingredientToAdd.next(product);
  }
}
