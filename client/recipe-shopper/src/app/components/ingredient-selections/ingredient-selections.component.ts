import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CartIngredient } from 'src/app/model/cart-ingredient.model';
import { Ingredient } from 'src/app/model/ingredient.model';

@Component({
  selector: 'app-ingredient-selections',
  templateUrl: './ingredient-selections.component.html',
  styleUrls: ['./ingredient-selections.component.css'],
})
export class IngredientSelectionsComponent implements OnInit {
  constructor(private fb: FormBuilder) {}

  @Input()
  ingredients!: Ingredient[];

  @Output()
  ingredientsToAdd = new EventEmitter<CartIngredient[]>();

  errorMsg: string = '';

  ingredientsForm!: FormGroup;
  ingredientSelections!: FormArray;

  ngOnInit(): void {
    this.initForm();
  }

  initForm() {
    this.ingredientSelections = this.fb.array([]);
    this.ingredientsForm = this.fb.group({
      selectedIngredients: this.ingredientSelections,
    });

    this.ingredients.forEach((ingredient) => {
      const item = this.fb.group({
        productId: [ingredient.productId],
        name: [ingredient.name],
        url: [ingredient.url],
        pack_size: [ingredient.pack_size],
        price: [ingredient.price],
        img: [ingredient.img],
        quantity: [
          ingredient.quantity,
          [Validators.required, Validators.min(1)],
        ],
        selected: this.fb.control<boolean>(true),
      });

      this.ingredientSelections.push(item);
    });
  }

  processForm() {
    const cartItem = (
      this.ingredientsForm.value.selectedIngredients as CartIngredient[]
    )
      .filter((item) => item.selected == true)
      .map((item) => {
        const total =
          Math.round((item.quantity * item.price + Number.EPSILON) * 100) / 100;

        return { ...item, total };
      });

    if (!cartItem.length) {
      this.errorMsg = 'Select at least ONE ingredient to add to cart';
      return;
    }

    console.debug('>>> cart items to add: ', cartItem);

    this.ingredientsToAdd.emit(cartItem);
  }

  validateFormInput(fieldName: string) {
    const field = this.ingredientSelections.get(fieldName)!;
    return field.invalid && field.touched;
  }
}
