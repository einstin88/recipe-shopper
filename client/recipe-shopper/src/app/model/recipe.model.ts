import { Ingredient } from './ingredient.model';
import { Product } from './product.model';

export type Recipe = {
  recipeId: string;
  recipeName: string;
  recipeCreator: string;
  procedures: string;
  ingredients: Product[] | Ingredient[];
  timeStamp: Date;
};
