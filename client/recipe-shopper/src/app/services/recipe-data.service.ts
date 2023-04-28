import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { firstValueFrom } from 'rxjs';
import { Recipe } from '../model/recipe.model';

/**
 * @description Service to handle API calls related to {@link Recipe} transactions
 */
@Injectable({
  providedIn: 'root',
})
export class RecipeDataService {
  constructor(private http: HttpClient) {}

  // Private attributes
  #API_URL = 'api/';
  #API_RECIPES = 'recipes';
  API_VIEW_RECIPE = 'recipe/view/';
  #API_NEW_RECIPE = 'recipe/new';
  API_UPDATE_RECIPE = 'recipe/update';

  #Content_JSON = 'application/json';

  /**
   * @description Retrieves a list of recipes from back end
   * 
   * @param limit Max number of recipes to display
   * @param offset The starting index of recipe result to retrieve
   * @returns Promise with a {@link Recipe}[]
   */
  getRecipes(limit: number, offset: number) {
    const url = this.#API_URL + this.#API_RECIPES;
    const headers = new HttpHeaders().set('Accept', this.#Content_JSON);
    const params = new HttpParams().appendAll({ limit, offset });

    return firstValueFrom(this.http.get<Recipe[]>(url, { headers, params }));
  }

  /**
   * @description Retrieves a recipe by its Id
   * 
   * @param recipeId The recipe's id
   * @returns Promise with a {@link Recipe}
   */
  getRecipeById(recipeId: string) {
    const url = this.#API_URL + this.API_VIEW_RECIPE + recipeId;

    return firstValueFrom(this.http.get<Recipe>(url));
  }

  /**
   * @description Posts a new recipe to be saved by the backend
   * 
   * @param newRecipe The new {@link Recipe}
   * @returns Empty promise
   */
  postNewRecipe(newRecipe: Recipe) {
    const url = this.#API_URL + this.#API_NEW_RECIPE;

    return firstValueFrom(this.http.post<void>(url, newRecipe));
  }

  /**
   * @description Posts a modified recipe to be updated on the backend
   * 
   * @param recipe The updated {@link Recipe}
   * @returns Empty promise
   */
  updateRecipe(recipe: Recipe) {
    const url = this.#API_URL + this.API_UPDATE_RECIPE;

    return firstValueFrom(this.http.put<void>(url, recipe));
  }
}
