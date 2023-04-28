import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Recipe } from 'src/app/model/recipe.model';
import { RecipeDataService } from 'src/app/services/recipe-data.service';

/**
 * @description A component associated with a router path for displaying a single recipe with its details
 */
@Component({
  templateUrl: './view-recipe.component.html',
  styleUrls: ['./view-recipe.component.css'],
})
export class ViewRecipeComponent implements OnInit {
  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private svc: RecipeDataService
  ) {}

  errMsg: string = '';

  recipeId!: string;
  recipe!: Recipe;

  ngOnInit(): void {
    this.recipeId = this.route.snapshot.params['recipeId'];

    this.fetchRecipe()
  }

  /**
   * @description Initiates the API call for retrieving a recipe via {@link RecipeDataService}.
   */
  private fetchRecipe() {
    this.svc
      .getRecipeById(this.recipeId)
      .then((recipe) => (this.recipe = recipe))
      .catch((err) => (this.errMsg = err.error));
  }

  /**
   * @description Function to handle the clicking of the 'Update' button. Navigates to the Update Recipe path. 
   */
  updateRecipe() {
    this.router.navigate([this.svc.API_UPDATE_RECIPE, this.recipeId]);
  }
}
