import { Component } from '@angular/core';
import { Recipe } from 'src/app/model/recipe.model';
import { RecipeDataService } from 'src/app/services/recipe-data.service';

/**
 * @description A component associated with a router path for displaying the list of recipes
 */
@Component({
  templateUrl: './browse-recipe.component.html',
  styleUrls: ['./browse-recipe.component.css'],
})
export class BrowseRecipeComponent {
  constructor(private svc: RecipeDataService) {}

  // Variables
  recipes!: Recipe[];
  limit: number = 10;
  offset: number = 0;

  errorMsg: string = '';
  isLoading: boolean = false;

  ngOnInit(): void {
    this.fetchRecipes();
  }

  // Function to initiate API call to fetch {@link Recipe}s from the back end
  private fetchRecipes() {
    this.isLoading = true;
    this.svc
      .getRecipes(this.limit, this.offset)
      .then((recipes) => {
        this.recipes = recipes;
      })
      .catch(() => {
        this.errorMsg = 'Error communicating with server.';
      })
      .finally(() => {
        this.isLoading = false;
      });
  }
}
