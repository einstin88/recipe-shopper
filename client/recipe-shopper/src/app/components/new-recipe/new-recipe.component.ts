import { Component, OnInit } from '@angular/core';
import {
  FormArray,
  FormBuilder,
  FormControl,
  FormGroup,
  Validators,
} from '@angular/forms';
import { Category } from 'src/app/model/category.model';
import { Product } from 'src/app/model/product.model';
import { Recipe } from 'src/app/model/recipe.model';
import { ProductDataService } from 'src/app/services/product-data.service';
import { Categories } from 'src/app/utils/consts';

@Component({
  selector: 'app-new-recipe',
  templateUrl: './new-recipe.component.html',
  styleUrls: ['./new-recipe.component.css'],
})
export class NewRecipeComponent implements OnInit {
  constructor(
    private fb: FormBuilder,
    private productSvc: ProductDataService
  ) {}

  category = new FormControl<Category | null>(null);
  categories = Categories;
  products!: Product[];
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
  }

  processForm() {
    const formData = this.recipeForm.value as Recipe;
    console.info(formData);
  }

  addIngredient({ productId, name, pack_size }: Product) {
    const newIngredient = this.fb.group({
      productId: [{ value: productId, disabled: true }],
      name: [{ value: name, disabled: true }],
      pack_size: [{ value: pack_size, disabled: true }],
      quantity: [0, [Validators.required, Validators.min(1)]],
    });

    this.recipeIngredients.push(newIngredient);
  }

  loadProducts() {
    const category = this.category.value as Category;
    // console.info(category);

    this.productSvc
      .getProducts(category)
      .then((products) => (this.products = products));
  }
}
