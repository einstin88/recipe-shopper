import { AfterViewChecked, AfterViewInit, Component, ViewChild } from '@angular/core';
import { Observable, Subject } from 'rxjs';
import { RecipeFormComponent } from 'src/app/components/recipe-form/recipe-form.component';
import { Category } from 'src/app/model/category.model';
import { Product } from 'src/app/model/product.model';
import { ProductDataService } from 'src/app/services/product-data.service';
import { RecipeDataService } from 'src/app/services/recipe-data.service';

@Component({
  templateUrl: './new-recipe.component.html',
  styleUrls: ['./new-recipe.component.css']
})
export class NewRecipeComponent implements AfterViewInit {
  constructor(private recipeSvc: RecipeDataService) {}

  @ViewChild(RecipeFormComponent)
  recipeForm!: RecipeFormComponent

  isValid$!: Observable<boolean>;

  ingredientToAdd = new Subject<Product>();

  ngAfterViewInit(): void {
      this.isValid$ = this.recipeForm.isInvalid$
  }

  processForm() {
    const formData = this.recipeForm.recipeData;
    console.info('>>> Submitted form', formData);

    this.recipeSvc
      .postNewRecipe(formData)
      .then(() => {
        this.recipeForm.recipeErr = ''
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
