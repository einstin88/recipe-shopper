import { Component, Input, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormGroup } from '@angular/forms';
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

  ingredientsForm!: FormGroup
  ingredientSelections!: FormArray;

  ngOnInit(): void {
    this.initForm();
  }

  initForm() {
    this.ingredientSelections = this.fb.array([this.fb.control('')])
    this.ingredientsForm = this.fb.group({
      selectedIngredients: this.ingredientSelections
    })

    this.ingredients.forEach((ingredient) => {
      const item = this.fb.group({
        productId: [ingredient.productId],
        name: [ingredient.name],
        pack_size: [ingredient.pack_size],
        price: [ingredient.price],
        img: [ingredient.img],
        selected: this.fb.control<boolean>(false),
      });

      this.ingredientSelections.push(item);
    });
  }
}
