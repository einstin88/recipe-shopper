import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Subject, Subscription, map, startWith } from 'rxjs';
import { Product } from 'src/app/model/product.model';
import { Recipe } from 'src/app/model/recipe.model';
import { RecipeDataService } from 'src/app/services/recipe-data.service';

/**
 * @description A reusable component designed to display the recipe form - a populated form if input is provided, else empty form
 */
@Component({
  selector: 'app-recipe-form',
  templateUrl: './recipe-form.component.html',
  styleUrls: ['./recipe-form.component.css'],
})
export class RecipeFormComponent implements OnInit, OnDestroy {
  constructor(private fb: FormBuilder, private recipeSvc: RecipeDataService) {}

  @Input()
  newIngredients!: Subject<Product>;

  @Input()
  recipeId: string = '';

  @Input()
  recipeCreator!: string;

  // Getters and setters to work with View Projection
  get recipeData() {
    return this.recipeForm.value as Recipe;
  }

  get isInvalid$() {
    return this.recipeForm.statusChanges.pipe(
      startWith('INVALID'),
      map((status) => {
        return status == 'INVALID';
      })
    );
  }

  get isInvalidUpdate$() {
    return this.recipeForm.statusChanges.pipe(
      map((status) => {
        return status == 'INVALID';
      })
    );
  }

  set reset(_: boolean) {
    this.recipeForm.reset();
    this.recipeIngredients.clear();
  }

  set recipeErr(msg: string) {
    this.recipeError = msg;
  }

  // Variables
  loading!: boolean;

  recipeError: string = '';
  recipeForm!: FormGroup;
  recipeIngredients!: FormArray;

  private sub$!: Subscription;

  // Interface methods
  ngOnInit(): void {
    this.initForm();

    this.sub$ = this.newIngredients.subscribe((ingredient) =>
      this.addIngredient(ingredient)
    );
  }

  ngOnDestroy(): void {
    this.sub$.unsubscribe();
  }

  /**
   * @description A function to initialize a reactive form for creating or updating recipes
   *
   */
  private async initForm() {
    let recipe!: Recipe | null;

    // Initialize the ingredients FormArray
    this.recipeIngredients = this.fb.array(
      [],
      [Validators.required, Validators.minLength(1)]
    );

    // Logic block for pulling recipe to update
    if (this.recipeId) {
      console.debug('>> Recipe ID: ', this.recipeId);

      this.loading = true;

      await this.recipeSvc
        .getRecipeById(this.recipeId)
        .then((res) => {
          // console.log(res);
          recipe = res;
          recipe.ingredients.forEach((ingredient) => {
            this.addIngredient(ingredient, ingredient.quantity);
          });
        })
        .catch((err) => {
          this.recipeError = err.error;
          return;
        })
        .finally(() => {
          this.loading = false;
        });
    } else {
      recipe = null;
    }

    // Initialize the recipe form
    // Note: some FormControls are not displayed to prohibit their values from being modified
    this.recipeForm = this.fb.group({
      recipeId: [recipe?.recipeId ?? ''],
      recipeName: [
        recipe?.recipeName ?? '',
        [
          Validators.required,
          Validators.minLength(4),
          Validators.maxLength(40),
        ],
      ],
      recipeCreator: [recipe?.recipeCreator ?? this.recipeCreator],
      procedures: [
        recipe?.procedures ?? '',
        [Validators.required, Validators.minLength(10)],
      ],
      ingredients: this.recipeIngredients,
    });
  }

  /**
   * @description A function to add neccessary attributes into the {@link recipeIngredients} FormArray
   * @param product the Product to add to the ingredient list
   * @param quantity An optional argument to populate the quantity input field
   */
  addIngredient({ productId, name, pack_size }: Product, quantity?: number) {
    const newIngredient = this.fb.group({
      productId: [productId],
      name: [name],
      pack_size: [pack_size],
      quantity: [quantity ?? 1, [Validators.required, Validators.min(1)]],
    });

    this.recipeIngredients.push(newIngredient);
  }

  /**
   * @description Removes the ingredient at the specified index from the {@link recipeIngredients}
   * @param idx Index of the ingredient to remove
   */
  removeIngredient(idx: number) {
    this.recipeIngredients.removeAt(idx);
  }

  validateFormInput(fieldName: string) {
    const field = this.recipeForm.get(fieldName)!;
    return field.invalid && field.touched;
  }
}
