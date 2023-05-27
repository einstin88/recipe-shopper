import { Component, Input } from '@angular/core';
import { Recipe } from 'src/app/model/recipe.model';

@Component({
  selector: 'app-recipes-display',
  templateUrl: './recipes-display.component.html',
  styleUrls: ['./recipes-display.component.css'],
})
export class RecipesDisplayComponent {
  set recipeList(recipes: Recipe[]) {
    this.recipes = recipes;
  }

  set errMsg(msg: string) {
    this.errorMsg = msg;
  }

  errorMsg: string = '';
  recipes: Recipe[] = [];
}
