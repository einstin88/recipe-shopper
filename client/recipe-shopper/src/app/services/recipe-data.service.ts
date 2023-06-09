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
import { RecipeId } from '../model/recipeId-payload.model';

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
  getRecipes(limit: number, offset: number, username: string = '') {
    const headers = new HttpHeaders().set('Accept', this.#Content_JSON);
    const params = new HttpParams().appendAll({ limit, offset });

    let url = EP_GET_RECIPES;
    if (username) {
      url += `/${username}`;
    }

    return firstValueFrom(
      this.http.get<Recipe[]>(url, { headers, params })
    ).catch((error: HttpErrorResponse) => {
      console.error(`Error response: ${error.message}`);
      throw new Error(`${error.status}: ${error.error}`);
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
        throw new Error(`${error.status}: ${error.error}`);
      }
    );
  }

  /**
   * @description Posts a new recipe to be saved by the backend
   */
  postNewRecipe(newRecipe: Recipe) {
    return firstValueFrom(
      this.http.post<RecipeId>(EP_POST_RECIPE, newRecipe)
    ).catch((error: HttpErrorResponse) => {
      console.error(`Error response: ${error.message}`);
      throw new Error(`${error.status}: ${error.error}`);
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
