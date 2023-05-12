import { Ingredient } from './ingredient.model';

/**
 * @description Type representing the details of a recipe 
 */
export type Recipe = {
  recipeId: string;
  recipeName: string;
  recipeCreator: string;
  procedures: string;
  ingredients: Ingredient[];
  timeStamp?: Date;
};
