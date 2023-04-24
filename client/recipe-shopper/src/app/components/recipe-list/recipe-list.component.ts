import { Component, OnInit } from '@angular/core';
import { Recipe } from 'src/app/model/recipe.model';
import { RecipeDataService } from 'src/app/services/recipe-data.service';

@Component({
  selector: 'app-recipe-list',
  templateUrl: './recipe-list.component.html',
  styleUrls: ['./recipe-list.component.css'],
})
export class RecipeListComponent implements OnInit {
  constructor(private svc: RecipeDataService) {}

  recipes!: Recipe[];
  limit: number = 10;
  offset: number = 0;
  resultsLoading: string = "Loading...";

  ngOnInit(): void {
    this.svc.getRecipes(this.limit, this.offset).then((recipes) => {
      this.recipes = recipes;
      this.resultsLoading = "";
    })
    .catch(() => {
      this.resultsLoading = "Error communicating with server."
    });
  }
}
