import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Recipe } from 'src/app/model/recipe.model';
import { RecipeDataService } from 'src/app/services/recipe-data.service';

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

    this.svc
      .getRecipeById(this.recipeId)
      .then((recipe) => (this.recipe = recipe))
      .catch((err) => (this.errMsg = err.error));
  }

  updateRecipe() {
    this.router.navigate([this.svc.API_UPDATE_RECIPE, this.recipeId]);
  }
}
