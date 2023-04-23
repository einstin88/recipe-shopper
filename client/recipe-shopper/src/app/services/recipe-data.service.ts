import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { firstValueFrom } from 'rxjs';
import { Recipe } from '../model/recipe.model';

@Injectable({
  providedIn: 'root',
})
export class RecipeDataService {
  constructor(private http: HttpClient) {}

  #API_URL = 'api/';
  #API_RECIPES = 'recipes';
  #API_NEW_RECIPE = 'recipe/new';
  #API_UPDATE_RECIPE = 'recipe/update';

  #Content_JSON = 'application/json';

  getRecipes(limit: number, offset: number) {
    const url = this.#API_URL + this.#API_RECIPES;
    const headers = new HttpHeaders().set('Accept', this.#Content_JSON);
    const params = new HttpParams().appendAll({ limit, offset });

    return firstValueFrom(this.http.get<Recipe[]>(url, { headers, params }));
  }

  postNewRecipe(newRecipe: Recipe) {
    const url = this.#API_URL + this.#API_NEW_RECIPE;

    return firstValueFrom(this.http.post<void>(url, newRecipe));
  }

  updateRecipe(recipe: Recipe) {
    const url = this.#API_URL + this.#API_UPDATE_RECIPE;

    return firstValueFrom(this.http.put<void>(url, recipe));
  }
}
