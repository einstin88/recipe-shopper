import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Subject, Subscription } from 'rxjs';
import { Product } from 'src/app/model/product.model';
import { Recipe } from 'src/app/model/recipe.model';
import { RecipeDataService } from 'src/app/services/recipe-data.service';

@Component({
  selector: 'app-recipe-form',
  templateUrl: './recipe-form.component.html',
  styleUrls: ['./recipe-form.component.css'],
})
export class RecipeFormComponent implements OnInit, OnDestroy {
  constructor(private fb: FormBuilder, private recipeSvc: RecipeDataService) {}

  @Input()
  newIngredients!: Subject<Product>;

  sub$!: Subscription;

  recipeError = '';
  recipeForm!: FormGroup;
  recipeIngredients!: FormArray;

  ngOnInit(): void {
    this.recipeIngredients = this.fb.array(
      [],
      [Validators.required, Validators.minLength(1)]
    );

    this.recipeForm = this.fb.group({
      recipeName: [
        '',
        [
          Validators.required,
          Validators.minLength(4),
          Validators.maxLength(40),
        ],
      ],
      recipeCreator: [
        '',
        [
          Validators.required,
          Validators.minLength(4),
          Validators.maxLength(40),
        ],
      ],
      procedures: ['', [Validators.required, Validators.minLength(10)]],
      ingredients: this.recipeIngredients,
    });

    this.sub$ = this.newIngredients.subscribe((ingredient) =>
      this.addIngredient(ingredient)
    );
  }

  ngOnDestroy(): void {
    this.sub$.unsubscribe();
  }

  processForm() {
    const formData = this.recipeForm.value as Recipe;
    // console.info(formData);

    this.recipeSvc
      .postNewRecipe(formData)
      .then(() => {
        this.recipeForm.reset();
      })
      .catch((err) => {
        this.recipeError = err.error;
      });
  }

  addIngredient({ productId, name, pack_size }: Product) {
    const newIngredient = this.fb.group({
      productId: [productId],
      name: [name],
      pack_size: [pack_size],
      quantity: [1, [Validators.required, Validators.min(1)]],
    });

    this.recipeIngredients.push(newIngredient);
  }

  removeIngredient(idx: number) {
    this.recipeIngredients.removeAt(idx);
  }
}
