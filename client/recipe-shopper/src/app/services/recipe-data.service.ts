import {
  HttpClient,
  HttpErrorResponse,
  HttpHeaders,
  HttpParams,
} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { firstValueFrom } from 'rxjs';
import { Recipe } from '../model/recipe.model';
import {
  EP_GET_RECIPE,
  EP_GET_RECIPES,
  EP_POST_RECIPE,
  EP_UPDATE_RECIPE,
} from '../utils/urls';

/**
 * @description Service to handle API calls related to {@link Recipe} transactions
 */
@Injectable({
  providedIn: 'root',
})
export class RecipeDataService {
  constructor(private http: HttpClient) {}

  // Private attributes
  #Content_JSON = 'application/json';

  /**
   * @description Retrieves a list of recipes from back end
   *
   * @param limit Max number of recipes to display
   * @param offset The starting index of recipe result to retrieve
   * @returns Promise with a {@link Recipe}[]
   */
  getRecipes(limit: number, offset: number) {
    const headers = new HttpHeaders().set('Accept', this.#Content_JSON);
    const params = new HttpParams().appendAll({ limit, offset });

    return firstValueFrom(
      this.http.get<Recipe[]>(EP_GET_RECIPES, { headers, params })
    ).catch((error: HttpErrorResponse) => {
      console.error(`Error response: ${error.message}`);
      throw new Error(`${error.status}: ${error.statusText}`);
    });
  }

  /**
   * @description Retrieves a recipe by its Id
   *
   * @param recipeId The recipe's id
   * @returns Promise with a {@link Recipe}
   */
  getRecipeById(recipeId: string) {
    const url = `${EP_GET_RECIPE}/${recipeId}`;

    return firstValueFrom(this.http.get<Recipe>(url)).catch(
      (error: HttpErrorResponse) => {
        console.error(`Error response: ${error.message}`);
        throw new Error(`${error.status}: ${error.statusText}`);
      }
    );
  }

  /**
   * @description Posts a new recipe to be saved by the backend
   *
   * @param newRecipe The new {@link Recipe}
   * @returns Empty promise
   */
  postNewRecipe(newRecipe: Recipe) {
    return firstValueFrom(
      this.http.post<void>(EP_POST_RECIPE, newRecipe)
    ).catch((error: HttpErrorResponse) => {
      console.error(`Error response: ${error.message}`);
      throw new Error(`${error.status}: ${error.statusText}`);
    });
  }

  /**
   * @description Posts a modified recipe to be updated on the backend
   *
   * @param recipe The updated {@link Recipe}
   * @returns Empty promise
   */
  updateRecipe(recipe: Recipe) {
    return firstValueFrom(this.http.put<void>(EP_UPDATE_RECIPE, recipe)).catch(
      (error: HttpErrorResponse) => {
        console.error(`Error response: ${error.message}`);
        throw new Error(`${error.status}: ${error.statusText}`);
      }
    );
  }
}
