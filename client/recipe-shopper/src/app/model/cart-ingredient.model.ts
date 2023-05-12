import { Ingredient } from './ingredient.model';

export type CartIngredient = Ingredient & {
  selected: boolean;
  total?: number
};
