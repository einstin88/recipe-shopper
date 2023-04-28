import { AfterViewChecked, Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Observable, Subject } from 'rxjs';
import { RecipeFormComponent } from 'src/app/components/recipe-form/recipe-form.component';
import { Product } from 'src/app/model/product.model';
import { RecipeFormDisplay } from 'src/app/model/recipe-form-display.interface';
import { RecipeDataService } from 'src/app/services/recipe-data.service';

/**
 * @description A component associated with a router path for updating recipes
 */
@Component({
  templateUrl: './update-recipe.component.html',
  styleUrls: ['./update-recipe.component.css'],
})
export class UpdateRecipeComponent
  implements OnInit, AfterViewChecked, RecipeFormDisplay
{
  constructor(
    private recipeSvc: RecipeDataService,
    private route: ActivatedRoute
  ) {}

  @ViewChild(RecipeFormComponent)
  recipeForm!: RecipeFormComponent;

  // Variables
  isValid$!: Observable<boolean>;

  ingredientToAdd = new Subject<Product>();
  recipeId!: string;

  ngOnInit(): void {
    this.recipeId = this.route.snapshot.params['recipeId'];
  }

  ngAfterViewChecked(): void {
    this.isValid$ = this.recipeForm.isInvalid$;
  }

  processForm() {
    const formData = this.recipeForm.recipeData;
    console.info('>>> Submitted form', formData);

    this.recipeSvc
      .updateRecipe(formData)
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
