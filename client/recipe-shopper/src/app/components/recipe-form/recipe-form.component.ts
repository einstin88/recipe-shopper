import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Subject, Subscription, map } from 'rxjs';
import { Product } from 'src/app/model/product.model';
import { Recipe } from 'src/app/model/recipe.model';
import { RecipeDataService } from 'src/app/services/recipe-data.service';

/**
 * @description A reusable component designed to display the recipe form - a populated form if {@link recipeId} input is provided, else empty form
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

  // Getters and setters to work with View Projection
  get recipeData() {
    return this.recipeForm.value as Recipe;
  }

  get isInvalid$() {
    return this.recipeForm.statusChanges.pipe(
      map((status) => status == 'INVALID')
    );
  }

  set reset(_: boolean) {
    this.recipeForm.reset();
  }

  set recipeErr(msg: string) {
    this.recipeError = msg;
  }

  // Variables
  private sub$!: Subscription;

  loading!: boolean;

  recipeError = '';
  recipeForm!: FormGroup;
  recipeIngredients!: FormArray;

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
    this.loading = true;

    // Initialize the ingredients FormArray
    this.recipeIngredients = this.fb.array(
      [],
      [Validators.required, Validators.minLength(1)]
    );

    if (this.recipeId) {
      // console.log('>> Recipe ID: ', this.recipeId);
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
      recipeCreator: [
        recipe?.recipeCreator ?? '',
        [
          Validators.required,
          Validators.minLength(4),
          Validators.maxLength(40),
        ],
      ],
      procedures: [
        recipe?.procedures ?? '',
        [Validators.required, Validators.minLength(10)],
      ],
      ingredients: this.recipeIngredients,
    });

    this.loading = false;
  }

  /**
   * @description A function to add neccessary attributes into the {@link recipeIngredients} FormArray 
   * @param {Product | Ingredient} product the Product to add to the ingredient list
   * @param {number} quantity An optional argument to populate the quantity input field
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
}
