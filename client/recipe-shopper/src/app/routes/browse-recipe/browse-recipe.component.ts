import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { RecipesDisplayComponent } from 'src/app/components/recipes-display/recipes-display.component';
import { RecipeDataService } from 'src/app/services/recipe-data.service';

/**
 * @description A component associated with a router path for displaying the list of recipes
 */
@Component({
  templateUrl: './browse-recipe.component.html',
  styleUrls: ['./browse-recipe.component.css'],
})
export class BrowseRecipeComponent implements OnInit, AfterViewInit {
  constructor(private svc: RecipeDataService) {}

  @ViewChild(RecipesDisplayComponent)
  recipeDisplay!: RecipesDisplayComponent;

  // Variables
  limit: number = 10;
  offset: number = 0;

  isLoading: boolean = false;

  ngOnInit(): void {
    this.fetchRecipes();
  }

  ngAfterViewInit(): void {}

  // Function to initiate API call to fetch {@link Recipe}s from the back end
  private fetchRecipes() {
    this.isLoading = true;
    this.svc
      .getRecipes(this.limit, this.offset)
      .then((recipes) => {
        this.recipeDisplay.recipeList = recipes;
      })
      .catch((err: Error) => {
        this.recipeDisplay.errMsg = err.message;
      })
      .finally(() => {
        this.isLoading = false;
      });
  }
}
