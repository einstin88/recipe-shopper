import { Product } from './product.model';

export type Ingredient = Product & {
  quantity: number;
};
