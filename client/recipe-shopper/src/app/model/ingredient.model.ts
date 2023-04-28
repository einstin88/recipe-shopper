import { Product } from './product.model';

/**
 * @description Type representing the ingredients in a recipe. Contains product details and its quantity for the specified recipe
 */
export type Ingredient = Product & {
  quantity: number;
};
