import { CartIngredient } from './cart-ingredient.model';

export type CartItem = {
  recipeId: string
  recipeName: string;
  recipeCreator: string;
  ingredients: CartIngredient[];
};
