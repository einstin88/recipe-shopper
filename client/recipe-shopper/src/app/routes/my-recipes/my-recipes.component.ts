import { Component, ViewChild } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { ActivatedRoute } from '@angular/router';
import { RecipesDisplayComponent } from 'src/app/components/recipes-display/recipes-display.component';
import { RecipeDataService } from 'src/app/services/recipe-data.service';

@Component({
  selector: 'user-recipes',
  templateUrl: './my-recipes.component.html',
  styleUrls: ['./my-recipes.component.css'],
})
export class MyRecipesComponent {
  constructor(
    private route: ActivatedRoute,
    private svc: RecipeDataService,
    private title: Title
  ) {}

  @ViewChild(RecipesDisplayComponent)
  recipeDisplay!: RecipesDisplayComponent;

  // Variables
  limit: number = 10;
  offset: number = 0;

  isLoading: boolean = false;

  ngOnInit(): void {
    const username = this.route.snapshot.params['username']
    this.title.setTitle(`Recipes of ${username}`)

    this.fetchRecipes(username);
  }

  ngAfterViewInit(): void {}

  private fetchRecipes(username: string) {
    this.isLoading = true;
    this.svc
      .getRecipes(this.limit, this.offset, username)
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
